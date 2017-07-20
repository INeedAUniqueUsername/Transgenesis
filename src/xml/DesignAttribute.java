package xml;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import panels.XMLPanel;
import window.Window;
import static window.Window.Fonts.*;
public class DesignAttribute {
	public enum ValueType {
		UNID {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design == null;
			}
		},
		TYPE_ANY {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return true;
			}
		},
		DOCKSCREEN_LOCAL_OR_TYPE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				
				//Get the named local dockscreens from the selected element and add them to the box
				DesignElementOld selected = XMLPanel.getSelected();
				if(selected.hasSubElement("DockScreens")) {
					for(DesignElementOld e : selected.getSubElementsByName("DockScreens").get(0).getSubElements()) {
						field.addItem(e.getName());
					}
				}
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("DockScreen");
			}
		},
		TYPE_ARMOR {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("ItemType") && design.hasSubElement("Armor");
			}
		},
		TYPE_IMAGE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("Image");
			}
		},
		TYPE_MOD {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("TranscendenceLibrary");
			}
		},
		TYPE_SOVEREIGN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("Sovereign");
			}
		},
		TYPE_SPACE_ENVIRONMENT {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("SpaceEnvironment");
			}
		},
		TYPE_SYSTEM_MAP {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("SystemMap");
			}
		},
		TYPE_INHERITED {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && (design.getName().equals("Type") || (design.hasAttribute("virtual") && design.getAttributeByName("virtual").getValue().equals("true")));
			}
		},
		BOOLEAN {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "true", "false");
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				return value.matches("true|false");
			}
		},
		INTEGER {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		               if(!Character.isDigit(c) || (c == '-' && field.getText().contains("-"))) {
		            	   e.consume();
		            	   
		               }
		            }
				});
				return field;
			}
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
		INTEGER_SEQUENCE {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		               char c = e.getKeyChar();
		               if(!Character.isDigit(c) && c != ',' && c != ';' && c != ' ') {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}
		},
		WHOLE {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		               if(!Character.isDigit(c)) {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}
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
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		                String text = field.getText();
		               if(!Character.isDigit(c) || (c == '.' && text.contains("."))) {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}
			@Override
			public boolean isValid(String value) {
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
		HEX_COLOR {
		},
		HEX_NUMBER {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		               if(!Character.isDigit(c) || (c == 'x' && field.getText().contains("x"))) {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}
		},
		STRING {
			
		},
		FILENAME {
		},
		ALIGNMENT {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "constructive order", "constructive chaos", "neutral", "destructive order", "destructive chaos", "republic", "federation","uplifter", "foundation","competitor", "archivist", "seeker", "hermit", "collective", "empire", "sterilizer", "corrector", "megalomaniac", "cryptologue", "perversion", "solipsist", "unorganized", "subsapient", "predator"
						);
			}
		},
		DISPOSITION {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "friend", "neutral", "enemy"
						);
			}
			
		},
		BLENDING {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"brighten"
						);
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
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					//Add a key listener that prevents the user from adding more than one d, +, or -, and allows only digits
		            public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		                String text = field.getText();
		                switch(c) {
		                case 'd':
		                	if(text.contains("d")) {
		                		e.consume();
		                	}
		                	break;
		                case '+':
		                case '-':
		                	if(text.contains("+") || text.contains("-")) {
		                		e.consume();
		                	}
		                	break;
		                default:
		                	if(!Character.isDigit(c)) {
		                		e.consume();
		                	}
		                	break;
		                }
		            }
				});
				return field;
			}
		},
		CHARACTER {

			@Override
			public boolean isValid(String value) {
				return value.length() == 1 && Character.isLetter(value.charAt(0));
			}
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		               if(field.getText().length() > 0 || !Character.isLetter(c)) {
		            	   e.consume();
		               }
		            }
				});
				field.setColumns(1);
				return field;
			}
		}, LEVEL_FREQUENCY {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                String c = "" + e.getKeyChar();
		               if(c.matches("c|u|r|v| ")) {
		            	   e.consume();
		               }
		            }
				});
				field.setColumns(1);
				return field;
			}
		}, FREQUENCY {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "common", "uncommon", "rare", "veryRare", "notRandom"
						);
			}
		}, UNIQUE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "inSystem", "inUniverse"
						);
			}
		}, SCALE_SIZE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "star", "world", "structure", "ship", "flotsam"
						);
			}
		}, SCALE_DISTANCE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "pixel", "light-second", "light-minute"
						);
			}
		}, TILE_SIZE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"small",
						"medium",
						"large",
						"huge"
						);
			}
		}, SYSTEMTYPE_BACKGROUND {
			public JComponent getInputField(String value) {
				return createComboBox(true, value,
						"",
						"none"
						);
			}
		}, SHAPE_SPACE_ENVIRONMENT {
			public JComponent getInputField(String value) {
				return createComboBox(true, value,
						"",
						"circular",
						"arc"
						);
			}
		}, PAINT_LAYER {
			public JComponent getInputField(String value) {
				return createComboBox(true, value,
						"",
						"overhang"
						);
			}
		},PRICE_ADJ {
			
		}, TYPE_WEAPON {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("ItemType") && design.hasSubElement("Weapon");
			}
		}, TYPE_EFFECT {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("EffectType");
			}
		}, TYPE_STATION {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("StationType");
			}
		}, DOCKSCREEN_TYPE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"canvas",
						"itemPicker",
						"deviceSelector",
						"weaponsSelector",
						"armorSelector",
						"miscSelector",
						"customItemPicker",
						"customPicker",
						"subjugateMinigame"
						);
			}
		}, DOCKSCREEN_BACKGROUND_ID {
			public JComponent getInputField(String value) {
				return createComboBox(true, value,
						"",
						"hero",
						"image",
						"none",
						"object",
						"schematic"
						);
			}
		}, DOCKSCREEN_DATA_FROM {
			public JComponent getInputField(String value) {
				return createComboBox(true, value,
						"",
						"player",
						"source",
						"station"
						);
			}
		}, FONT {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"Small",
						"SmallBold",
						"Medium",
						"MediumBold",
						"Large",
						"LargeBold",
						"Header",
						"HeaderBold",
						"SubTitle",
						"SubTitleBold",
						"SubTitleHeavyBold",
						"Title",
						"LogoTitle",
						"ConsoleMediumHeavy"
						);
			}
		}, ALIGN_HORIZONTAL {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"left",
						"center",
						"right"
						);
			}
		}, ALIGN_VERTICAL {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"top",
						"center",
						"bottom"
						);
			}
		}, TYPE_ITEM {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("ItemType");
			}
		}, LEVEL_VALUE {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		               if(!Character.isDigit(c) || c != ',') {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}
		}, TYPE_ITEM_TABLE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("ItemTable");
			}
		}, TYPE_DOCKSCREEN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("DockScreen");
			}
		}, OVERLAY_COUNTER_STTYLE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "commandBarProgress", "flag", "progress", "radius");
				return field;
			}
		}, LABEL_TYPE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "advantage", "neutral", "disadvantage");
				return field;
			}
		}, LAYOUT {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "bottomBar", "left", "right");
				return field;
			}
		}, STYLE_CONTROLS {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "default", "warning");
				return field;
			}
		}, PROGRAM {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "ShieldsDown", "Disarm", "Reboot");
				return field;
			}
		}, LINKED_FIRE_OPTIONS {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "always", "targetInRange", "whenInFireArc");
				return field;
			}
		}, TYPE_DEVICE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value, "");
				return field;
			}
		}, CATEGORY_DEVICE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "cargoHold", "drive", "launcher", "miscDevice", "reactor", "shields", "weapon");
				return field;
			}
		}, TYPE_OVERLAY {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("OverlayType");
			}
		}, SATELLITE_OVERLAP_CHECK {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "true", "planetoids", "asteroids");
				return field;
			}
		}, TYPE_SHIPCLASS {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "");
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElementOld design) {
				return design != null && design.getName().equals("ShipClass");
			}
		}
		
		;
		public boolean isValid(String value) {
			// TODO Auto-generated method stub
			try {
				throw new Exception("Not supported");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		};
		public JComponent getInputField(String value) {
			JTextField field = new JTextField();
			field.setFont(Medium.f);
			field.setText(value);
			return field;
		}
		public String getInputValue(JComponent field) {
			if(field instanceof JTextField) {
				return ((JTextField) field).getText();
			} else if(field instanceof JComboBox) {
				return (String) ((JComboBox<String>) field).getSelectedItem();
			}
			return null;
		}
		public JComboBox<String> createComboBox(boolean editable, String value, String... choices) {
			JComboBox<String> field = new JComboBox<String>();
			for(String s : choices) {
				field.addItem(s);
			}
			field.setEditable(editable);
			field.setFont(Medium.f);
			field.setSelectedItem(value);
			return field;
		}
		/*
		public List<String> getValidTypes() {
			List<String> result = new LinkedList<>();
			XMLPanel.getExtensionAvailableTypes().forEach((String type, DesignElementOld design) -> {
				if(typeIsValid(design)) {
					result.add(type);
				}
			});
			return result;
		}
		*/
		public void addValidTypes(JComboBox<String> box) {
			XMLPanel.getExtensionTypeMap().forEach((String type, DesignElementOld design) -> {
				if(typeIsValid(design)) {
					box.addItem("&" + type + ";");
				}
			});
		}
		public boolean typeIsValid(DesignElementOld design) {
			return true;
		}
	}
	private String name;
	private ValueType valueType;
	private String value;
	public static DesignAttribute att(String name, ValueType valueType) {
		return new DesignAttribute(name, valueType);
	}
	public static DesignAttribute att(String name, ValueType valueType, String value) {
		return new DesignAttribute(name, valueType, value);
	}
	public DesignAttribute(String name, ValueType valueType) {
		this(name, valueType, "");
	}
	public DesignAttribute(String name, ValueType valueType, String value) {
		this.name = name;
		this.valueType = valueType;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public JComponent createInputField() {
		return valueType.getInputField(value);
	}
	public String getValue() {
		return value;
	}
	public boolean setValue(String value) {
		if(Window.DEBUG) {
			this.value = value;
			return true;
		}
		
		if(valueType.isValid(value)) {
			this.value = value;
			return true;
		}
		
		return false;
	}
	public DesignAttribute clone() {
		return new DesignAttribute(name, valueType, value);
	}
	public String toMinistryMarkdown() {
		return "**" + name + "=**" + " " + "[" + valueType.toString().toLowerCase() + "]" + " " + "\"" + value + "\"";
	}
	public boolean equals(Object o) {
		if(o instanceof DesignAttribute) {
			DesignAttribute a = (DesignAttribute) o;
			return name.equals(a.getName()) && valueType.equals(a.getValueType()) && value.equals(a.getValue());
		}
		return false;
	}
	public int hashCode() {
		return Objects.hash(name, valueType, value);
	}
	public String toString() {
		return toMinistryMarkdown() + " " + hashCode();
	}
	public Element getDefinition(Document doc) {
		Element result = doc.createElement("Attribute");
		result.setAttribute("name", name);
		result.setAttribute("valueType", valueType.name());
		if(!value.isEmpty()) {
			result.setAttribute("value", value);
		}
		
		return result;
	}
	
}
