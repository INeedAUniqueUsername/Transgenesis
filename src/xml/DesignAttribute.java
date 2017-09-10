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
		UNKNOWN {},
		UNID {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design == null;
			}
		}, TYPE_ANY {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return true;
			}
		}, TYPE_SOUND {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("Sound");
			}
		}, SCREEN_LOCAL_OR_TYPE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				
				//Get the named local dockscreens from the selected element and add them to the box
				DesignElement selected = XMLPanel.getInstance().getSelected();
				if(selected.hasSubElement("DockScreens")) {
					for(DesignElement e : selected.getSubElementsByName("DockScreens").get(0).getSubElements()) {
						field.addItem(e.getName());
					}
				}
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("DockScreen");
			}
		}, TYPE_ARMOR {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("ItemType") && design.hasSubElement("Armor");
			}
		}, TYPE_IMAGE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("Image");
			}
		}, TYPE_MOD {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("TranscendenceLibrary");
			}
		}, TYPE_SOVEREIGN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("Sovereign");
			}
		}, TYPE_SPACE_ENVIRONMENT {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("SpaceEnvironment");
			}
		}, TYPE_SYSTEM_MAP {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("SystemMap");
			}
		}, TYPE_SYSTEM_TYPE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("SystemType");
			}
		},TYPE_INHERITED {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && (design.getName().equals("Type") || (design.hasAttribute("virtual") && design.getAttributeByName("virtual").getValue().equals("true")));
			}
		}, SYSTEM_PART_TABLE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				XMLPanel.getInstance().getExtensionTypeMap().forEach((String type, DesignElement design) -> {
					if(typeIsValid(design) && design.getName().equals("SystemPartTable")) {
						for(DesignElement table : design.getSubElements()) {
							field.addItem(table.getName());
						}
					}
				});
				return field;
			}
		}, BOOLEAN {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "true", "false");
				return field;
			}
			@Override
			public boolean isValid(String value) {
				// TODO Auto-generated method stub
				return value.matches("true|false");
			}
		}, INTEGER {
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
			
		}, INTEGER_SEQUENCE {
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
		}, WHOLE {
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
		}, WHOLE_100 {
			
		}, DOUBLE {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		                String text = field.getText();
		               if((c == '.' && text.contains(".")) || (c != '.' && !Character.isDigit(c))) {
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
		}, HEX_COLOR {
		}, HEX_NUMBER {
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
		}, STRING {
		}, FILENAME {
		}, FILE_JPG {
		}, FILE_MP3 {
		}, ALIGNMENT {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "constructive order", "constructive chaos", "neutral", "destructive order", "destructive chaos", "republic", "federation","uplifter", "foundation","competitor", "archivist", "seeker", "hermit", "collective", "empire", "sterilizer", "corrector", "megalomaniac", "cryptologue", "perversion", "solipsist", "unorganized", "subsapient", "predator"
						);
			}
		}, DISPOSITION {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"", "friend", "neutral", "enemy"
						);
			}
			
		}, BLENDING {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"",
						"brighten"
						);
			}
		}, BLEND_MODE {
			public JComponent getInputField(String value) {
				return createComboBox(false, value,
						"normal",
						"multiply",
						"overlay",
						"screen",
						"hardLight",
						"composite"
						);
			}
		}, DICE_RANGE {
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
		}, RANGE_0_100 {
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					//Add a key listener that prevents the user from adding more than one d, +, or -, and allows only digits
		            public void keyTyped(KeyEvent e) {
		                char c = e.getKeyChar();
		                String text = field.getText();
		                if((c == '-' && text.contains("-")) ||
		                		(c != '-' && !Character.isDigit(c))
		                		) {
		                	e.consume();
		                }
		            }
				});
				return field;
			}
		}, CHARACTER {

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
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("ItemType") && design.hasSubElement("Weapon");
			}
		}, TYPE_EFFECT {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("EffectType");
			}
		}, TYPE_STATION {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
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
		}, BACKGROUND_ID {
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
		}, DATA_FROM {
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
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("ItemType");
			}
		}, LEVEL_VALUE {
			/*
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
			*/
		}, CURRENCY_VALUE {
			//currency:value
			
		}, CURRENCY_VALUE_SEQUENCE {
			//currency:value, currency:value, //currency:value, ... 
		}, TYPE_ITEM_TABLE {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("ItemTable");
			}
		}, TYPE_DOCKSCREEN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(true, value);
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("DockScreen");
			}
		}, STYLE_COUNTER {
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
		}, STYLE_FLARE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "fadingBlast", "flicker", "plain");
				return field;
			}
		}, STYLE_LIGHTNING_STORM {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "objectArcs");
				return field;
			}
		}, STYLE_PARTICLE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "plain", "flame", "smoke", "line");
				return field;
			}
		}, STYLE_PARTICLE_CLOUD {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value,
						"cloud",
				        "exhaust",
				        "jet",
				        "ring",
				        "splash"
						);
				return field;
			}
		}, STYLE_PARTICLE_PATTERN {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "comet", "jet");
				return field;
			}
		}, STYLE_PARTICLE_SYSTEM {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "amorphous", "comet", "exhaust", "jet", "radiate", "spray", "writhe", "brownian");
				return field;
			}
		}, STYLE_STARBURST {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "ballLightning", "flare", "plain", "lightningStar", "morningStar");
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
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("OverlayType");
			}
		}, OVERLAP_CHECK {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "true", "planetoids", "asteroids");
				return field;
			}
		}, POINT{
			public JComponent getInputField(String value) {
				JTextField field = (JTextField) super.getInputField(value);
				field.addKeyListener(new KeyAdapter() {
					public void keyTyped(KeyEvent e) {
						String text = field.getText();
						char c = e.getKeyChar();
						if(!(c == ',' && !text.contains(",")) && !Character.isDigit(c)) {
							e.consume();
						}
		            }
				});
				return field;
			}
		}, PARTITION_NODES_ORDER {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "", "random");
				return field;
			}
		}, TYPE_SHIPCLASS {
			public JComponent getInputField(String value) {
				//WIP
				JComboBox<String> field = createComboBox(false, value, "");
				addValidTypes(field);
				return field;
			}
			public boolean typeIsValid(DesignElement design) {
				return design != null && design.getName().equals("ShipClass");
			}
		}, EFFECT_INSTANCE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "game", "owner", "creator");
				return field;
			}
		}, BEAM_TYPE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "heavyBlaster", "jaggedBolt", "laser", "lightning", "lightningBolt", "particle", "starblaster", "greenParticle", "blueParticle", "blaster", "greenLightning");
				return field;
			}
		}, STYLE_LINE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value, "", "dashed", "solid");
				return field;
			}
		}, STYLE_RAY {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"", 
						"wavy",
						"smooth",
						"jagged",
						"grainy",
						"lightning",
						"whiptail");
				return field;
			}
		}, SHAPE_RAY {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"diamond",
						"oval",
						"straight",
						"tapered",
						"cone");
				return field;
			}
		}, ANIMATE_RAY {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"fade",
						"flicker",
						"cycle");
				return field;
			}
		}, ANIMATE_ORB {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"dissipate",
						"explode",
						"fade",
						"flicker");
				return field;
			}
		}, STYLE_ORB {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"smooth",
						"flare",
						"cloud",
						"fireblast",
						"smoke",
						"diffraction",
						"firecloud");
				return field;
			}
		}, STYLE_SHOCKWAVE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",

						"glowRing",
						"image",
						"cloud");
				return field;
			}
		}, WEAPON_CONFIGURATION {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"dual",
						"wall",
						"spread2",
						"spread3",
						"spread5",
						"alternating");
				return field;
			}
		}, WEAPON_COUNTER {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"temperature",
						"capacitor"
						);
				return field;
			}
		}, FAILURE_TYPE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"custom",
						"explosion",
						"heatDamage",
						"jammed",
						"misfire",
						"noFailure",
						"safeMode"
						);
				return field;
			}
		}, VARIANT_TYPE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"charges",
						"missiles",
						"levels"
						);
				return field;
			}
		}, FIRE_TYPE {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"",
						"missile",
						"beam",
						"area",
						"continuousBeam",
						"particles",
						"radius"
						);
				return field;
			}
		}, ARMOR_NONCRITICAL {
			public JComponent getInputField(String value) {
				JComboBox<String> field = createComboBox(false, value,
						"dev0",
						"dev1",
						"dev2",
						"dev3",
						"dev4",
						"dev5",
						"dev6",
						"dev7",
						"maneuver",
						"drive",
						"scanners",
						"tactical",
						"cargo",
						""
						);
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
			//int count = box.getItemCount();
			XMLPanel.getInstance().getExtensionTypeMap().forEach((String type, DesignElement design) -> {
				if(typeIsValid(design)) {
					box.addItem("&" + type + ";");
				}
			});
			//System.out.println(box.getItemCount() - count + " valid Types found.");
		}
		public boolean typeIsValid(DesignElement design) {
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
