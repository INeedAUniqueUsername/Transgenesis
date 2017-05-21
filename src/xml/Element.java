package xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public interface Element {
	public static enum ValueType {
		UNID {
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				System.out.println(new Exception("Not supported"));
				return false;
			}
		},
		TYPE_ANY {

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				System.out.println(new Exception("Not supported"));
				return false;
			}
		},
		TYPE_INHERITED {

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				System.out.println(new Exception("Not supported"));
				return false;
			}
		},
		BOOLEAN {
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				return value.matches("true|false");
			}
		},
		INTEGER {

			@Override
			public boolean isValid(String value) {
				try {
					Integer.parseInt(value);
					return true;
				} catch(Exception e) {
					System.out.println(new Exception("Bad input"));
					return false;
				}
			}
			
		},
		WHOLE {

			@Override
			public boolean isValid(String value) {
				try {
					return Integer.parseInt(value) >= 0;
				} catch(Exception e) {
					return false;
				}
			}
		},
		DECIMAL {

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				/*
				return value.matches(
						"\\d+" +	//Integer
						"(" +		//End of string OR Fractional
							"$" +		//End of string
							"|" +		//OR
							"\\d+" +	//Fractional
							")"
						);
				*/
				try {
					Double.parseDouble(value);
					return true;
				} catch(Exception e) {
					return false;
				}
			}
		},
		STRING {

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				return true;
			}
			
		},
		DICE_RANGE {
			@Override
			public boolean isValid(String value) {
				return value.matches(
						//Dice Range or Range or Constant
						"\\d+" +	//Rolls
						"d" +		//'d'
						"\\d+" +	//Sides
						"(" +		//Check for end of string OR sign with integer
							"$"	+		//End of string
							"|" +		//OR
							"(" +		//Plus sign OR minus sign
								"\\+" +		//Addition sign
								"|" +		//OR
								"-" +		//Minus sign
								")" +
							"\\d+" +	//Check for bonus integer
							")" +
						"|"	+		//OR
						//Range
						"\\d+" +	//Min
						"-" +		//Range
						"\\d+" +	//Max
						"|" +		//OR
						"\\d+"		//Constant
						);
			}
		};
		private String value;
		public void setValue(String value) {
			if(isValid(value)) {
				this.value = value;
			} else {
				System.out.println("Bad input");
				System.exit(1);
			}
		}
		public String getValue() {
			return value;
		}
		public abstract boolean isValid(String value);
	}
	public default String getXML() {
		return getXML("");
	}
	public default String getXML(String tabs) {
		String tabs_subelements = tabs + "\t";
		String tabs_attributes = tabs + "\t\t";
		
		String name = getClass().getName();
		Set<Entry<String, String>> attributes = getAttributeValues().entrySet();
		ArrayList<Element> subelements = getSubElements();
		
		boolean hasAttributes = attributes.size() > 0;
		boolean hasSubelements = subelements.size() > 0;
		String result = tabs + "<" + name;
		if(hasAttributes) {
			for(Entry<String, String> e : attributes) {
				result += "\n" + tabs_attributes + e.getKey() + "=\t\t\"" + e.getValue() + "\""; 
			}
			result += "\n" + tabs_attributes;
		}
		
		if(hasSubelements) {
			result += ">";
			for(Element e : subelements) {
				result += "\n" + e.getXML(tabs_subelements);
			}
			result += "\n" + tabs + "</" + name + ">";
		} else {
			result += "/>";
		}
		
		return result;
	}
	public HashMap<String, ValueType> getAttributeList();
	public HashMap<String, String> getAttributeValues();
	public ArrayList<Element> getSubElements();
}
