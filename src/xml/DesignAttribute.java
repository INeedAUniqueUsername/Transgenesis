package xml;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import window.Window;

public class DesignAttribute {
	public enum ValueType {
		UNID {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
		},
		TYPE_ANY {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
		},
		TYPE_IMAGE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
			
		},
		TYPE_MOD {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
			
		},
		TYPE_SOVEREIGN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
		},
		TYPE_SYSTEM_MAP {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
			}
			
		},
		TYPE_INHERITED {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
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
		}, SCALE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "star", "world", "structure", "ship", "flotsam"
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
		}, PRICE_ADJ {
			
		}, TYPE_WEAPON {
			public JComponent getInputField(String value) {
				return createComboBox(true, value);
			}
		}, TYPE_EFFECT {
			public JComponent getInputField(String value) {
				return createComboBox(true, value);
			}
		}, TYPE_STATION {
			public JComponent getInputField(String value) {
				return createComboBox(true, value);
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
				return field;
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
				return field;
			}
		}, TYPE_DOCKSCREEN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				return field;
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
				return field;
			}
		}, SATELLITE_OVERLAP_CHECK {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "true", "planetoids", "asteroids");
				return field;
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
			field.setFont(Window.FONT_MEDIUM);
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
			field.setFont(Window.FONT_MEDIUM);
			field.setSelectedItem(value);
			return field;
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
		return "**" + name + "=**" + " " + "[" + valueType.toString().toLowerCase() + "]" + " " + value;
	}
	public boolean equals(Object o) {
		if(o instanceof DesignAttribute) {
			DesignAttribute a = (DesignAttribute) o;
			return name.equals(a.getName()) && valueType.equals(a.getValueType()) && value.equals(a.getValue());
		}
		return false;
	}
	
}
