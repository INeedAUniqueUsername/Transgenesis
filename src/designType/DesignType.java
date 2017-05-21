package designType;

import java.util.ArrayList;
import java.util.HashMap;

import xml.Element;

public class DesignType implements Element {

	private HashMap<String, ValueType> attribute_types;
	private HashMap<String, String> attribute_values;
	public DesignType() {
		attribute_types = new HashMap<String, ValueType>() {{
			put("UNID", ValueType.UNID);
			put("attributes", ValueType.STRING);
			put("inherit", ValueType.TYPE_INHERITED);
		}};
	}
	protected void addAttributeType(String name, ValueType type) {
		attribute_types.put(name, type);
	}
	@Override
	public HashMap<String, ValueType> getAttributeList() {
		// TODO Auto-generated method stub
		return attribute_types;
	}
	@Override
	public HashMap<String, String> getAttributeValues() {
		// TODO Auto-generated method stub
		return attribute_values;
	}

	@Override
	public ArrayList<Element> getSubElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
