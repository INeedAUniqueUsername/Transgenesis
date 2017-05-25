package xml;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import window.Window;

public class Attribute {
	public enum ValueType {
		UNID {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			public String getInputValue(JComponent field) {
				return (String) ((JComboBox<String>) field).getSelectedItem();
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		TYPE_ANY {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		TYPE_IMAGE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
		},
		TYPE_MOD {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
		},
		TYPE_SOVEREIGN {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		TYPE_SYSTEM_MAP {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not Supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
		},
		TYPE_INHERITED {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				//WIP
				field.setEditable(true);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			public String getInputValue(JComponent field) {
				return (String) ((JComboBox<String>) field).getSelectedItem();
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		BOOLEAN {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				field.addItem("true");
				field.addItem("false");
				field.setEditable(false);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			public String getInputValue(JComponent field) {
				return (String) ((JComboBox<String>) field).getSelectedItem();
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
		               if(!Character.isDigit(c) || c != ',' || c != ' ') {
		            	   e.consume();
		               }
		            }
				});
				return field;
			}

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
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
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		STRING {
			@Override
			public String getInputValue(JComponent field) {
				// TODO Auto-generated method stub
				return ((JTextField) field).getText();
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				return true;
			}
			
		},
		ALIGNMENT {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				for(String s : new String[] {"constructive order", "constructive chaos", "neutral", "destructive order", "destructive chaos", "republic", "federation","uplifter", "foundation","competitor", "archivist", "seeker", "hermit", "collective", "empire", "sterilizer", "corrector", "megalomaniac", "cryptologue", "perversion", "solipsist", "unorganized", "subsapient", "predator"}) {
					field.addItem(s);
				}
				field.setEditable(false);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}

			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		},
		DISPOSITION {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				for(String s : new String[] {"friend", "neutral", "enemy"}) {
					field.addItem(s);
				}
				field.setEditable(false);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
		},
		BLENDING {
			public JComponent getInputField(String value) {
				JComboBox<String> field = new JComboBox<String>();
				field.addItem("brighten");
				field.addItem("");
				field.setEditable(false);
				field.setFont(Window.FONT_MEDIUM);
				field.setSelectedItem(value);
				return field;
			}

			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				try {
					throw new Exception("Not supported");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
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
		};
		public abstract boolean isValid(String value);
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
	}
	private String name;
	private ValueType valueType;
	private String value;
	public Attribute(String name, ValueType valueType) {
		this(name, valueType, "");
	}
	public Attribute(String name, ValueType valueType, String value) {
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
	public Attribute clone() {
		return new Attribute(name, valueType, value);
	}
	
}
