package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

import static panels.XMLPanel.*;
import static java.awt.event.KeyEvent.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import designType.Types;
import mod.TranscendenceMod;
import mod.ExtensionFactory.Extensions;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import xml.DesignElement;
public class TypeManager {
	//Add search function for elements and Types
	//When a Type is renamed, then all instances of it in elements should be renamed too
	XMLPanel editor;
	JScrollPane pane = null;
	ArrayList<TypeElement> elements; //TO DO: Make sure that entries and ranges are correctly sorted at extension loading time if no working metadata file is available
	public TypeManager() {
		elements = new ArrayList<>();
	}
	public TypeManager(File modFile) {
		this();
		initializeElements(modFile);
	}
	public void initializeElements(File modFile) {
		try {
			File read = new File(modFile.getAbsolutePath() + ".dat");
			if(!read.exists()) {
				System.out.println("Generating metadata using extension");
				read = modFile;
			}
			System.out.println("Metadata file: " + read.getAbsolutePath());
			byte[] bytes = Files.readAllBytes(read.toPath());
			String lines = new String(bytes);
			lines = lines.replace("&", "&amp;");
			bytes = lines.getBytes();
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
			inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
			inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
			XMLEventReader reader = inputFactory.createXMLEventReader(new ByteArrayInputStream(bytes));
			boolean active = false;
			Read: while (reader.hasNext()) {
			    XMLEvent event = reader.nextEvent();
			    
			    EventType: switch(event.getEventType()) {
			    case XMLStreamConstants.DTD:
			    	//System.out.println("DTD");
			    	for(Object o : ((DTD) event).getEntities()) {
			    		createFromXML((EntityDeclaration) o);
			    	}
			    	break EventType;
			    case XMLStreamConstants.START_ELEMENT:
			    	//System.out.println("StartElement");
			    	StartElement start = event.asStartElement();
			    	if(
			    			"TransGenesis".equals(start.getName().getLocalPart())
			    			) {
			    		System.out.println("Found Data");
			    		active = true;
			    		continue Read;
			    	} else if(active) {
			    		System.out.println("Reading Data");
			    		createFromXML(start);
			    	}
			    	break EventType;
			    case XMLStreamConstants.END_ELEMENT:
			    	EndElement end = event.asEndElement();
			    	if(
			    			active &&
			    			"Data".equals(end.getName().getLocalPart())
			    			) {
			    		System.out.println("Finished Reading");
			    		active = false;
			    	}
			    	break EventType;
			    }
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("sorting elements");
		Collections.sort(elements);
		System.out.println("sorting done");
	}
	public void createFromXML(EntityDeclaration e) {
		System.out.println("Found Declaration");
		TypeElement entry = new TypeEntry(
				"",
				e.getReplacementText(),
				e.getName()
				);
		elements.add(entry);
		/*
		int index = Collections.binarySearch(elements, entry);
		if(index == -1) {
			index = 0;
		}
		elements.add(index, entry);
		*/
	}
	public void createFromXML(StartElement event) {
		System.out.println("Found Entry");
		switch(event.getName().getLocalPart()) {
		case "TypeEntry":
			elements.add(new TypeEntry(
					event.getAttributeByName(new QName("comment")).getValue(),
					event.getAttributeByName(new QName("unid")).getValue(),
					event.getAttributeByName(new QName("type")).getValue()
					));
			break;
		case "TypeRange":
			elements.add(new TypeRange(
					event.getAttributeByName(new QName("comment")).getValue(),
					event.getAttributeByName(new QName("unid_min")).getValue(),
					event.getAttributeByName(new QName("unid_max")).getValue(),
					event.getAttributeByName(new QName("types")).getValue().split(" ")
					));
			break;
		}
	}
	public void setEditor(XMLPanel editor) {
		this.editor = editor;
	}
	public JScrollPane initializeFrame() {
		System.out.println("Initialize Frame");
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		//panel.setPreferredSize(frame.getSize());
		container.setLayout(new MigLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(3, 1));
		JScrollPane result = createScrollPane(container);
		JButton exitButton = createJButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAllData();
				//Source Question: https://stackoverflow.com/questions/9650874/java-swing-obtain-window-jframe-from-inside-a-jpanel
				//Source Answer: https://stackoverflow.com/a/9650915
				JFrame frame = ((JFrame) SwingUtilities.getWindowAncestor(container));
				frame.remove(result);
				frame.add(editor);
				editor.selectElement(editor.getSelected());
				frame.pack();
			}
		});
		buttons.add(exitButton);
		JButton addEntry = createJButton("New Type Entry");
		addEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addEntry) {
					elements.add(new TypeEntry());
					refreshFrame();
				}
			}
		});
		buttons.add(addEntry);
		JButton addRange = createJButton("New Type Range");
		addRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addRange) {
					elements.add(new TypeRange());
					refreshFrame();
				}
			}
		});
		buttons.add(addRange);
		container.add(buttons, new CC().x("5%").width("90%").wrap());
		for(int i = 0; i < elements.size(); i++) {
			int index = i;
			JPanel elementButtons = new JPanel();
			//buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
			elementButtons.setLayout(new GridLayout(2, 0));
			JButton moveUpButton = createJButton("Move Up");
			moveUpButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getSource() == moveUpButton) {
						Collections.swap(elements, index, index-1);
						refreshFrame();
					}
				}
			});
			moveUpButton.setEnabled(index > 0);
			elementButtons.add(moveUpButton);
			JButton moveDownButton = createJButton("Move Down");
			moveDownButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getSource() == moveDownButton) {
						Collections.swap(elements, index, index+1);
						refreshFrame();
					}
				}
			});
			moveDownButton.setEnabled(index < elements.size() - 1);
			elementButtons.add(moveDownButton);
			
			JPanel row = new JPanel();
			row.setLayout(new MigLayout());
			row.add(elementButtons, new CC().width("10%"));
			row.add(elements.get(i).initializePanel(), new CC().width("80%"));
			JButton deleteButton = createJButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == deleteButton) {
						elements.remove(index);
						refreshFrame();
					}
				}
			});
			container.add(row, new CC().x("5%").width("90%").wrap());
		}
		return result;
	}
	public void refreshFrame() {
		System.out.println("Refreshing Frame");
		saveAllData();
		
		Point scrolling = new Point(0, 0);
		Dimension size = editor.getSize();
		if(pane != null) {
			editor.frame.remove(pane);
			scrolling = pane.getViewport().getViewPosition();
			System.out.println(scrolling.toString());
			size = pane.getSize();
		}
		
		editor.frame.add(pane = initializeFrame());
		pane.setPreferredSize(size);
		
		editor.frame.pack();
		System.out.println("Packing");
		pane.getViewport().setViewPosition(scrolling);
	}
	public void saveAllData() {
		for(TypeElement e : elements) {
			e.saveData();
		}
	}
	public BidiMap<String, String> bindAll() {
		BidiMap<String, String> map = new TreeBidiMap<>();
		LinkedList<TypeElement> definedUNIDs = new LinkedList<>();
		LinkedList<TypeElement> generatedUNIDs = new LinkedList<>();
		for(TypeElement e : elements) {
			if(e instanceof TypeEntry || e instanceof TypeRange) {
				definedUNIDs.add(e);
			} else {
				generatedUNIDs.add(e);
			}
		}
		definedUNIDs.forEach((TypeElement e) -> {
			e.bindAll(map);
		});
		generatedUNIDs.forEach((TypeElement e) -> {
			e.bindAll(map);
		});
		return map;
	}
	public String getXMLOutput() {
		//UNID, Entity
		BidiMap<String, String> map = bindAll();
		String result = "";
		for(Entry<String, String> e : map.entrySet()) {
			result += String.format("\t<!ENTITY %-32s \"%s\">", e.getValue(), e.getKey()) + "\n";
		}
		return result;
	}
	public String getXMLMetaData() {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element metadata = doc.createElement("MetaData");
			Element data = doc.createElement("TransGenesis");
			doc.appendChild(metadata);
			metadata.appendChild(data);
			for(TypeElement e : elements) {
				data.appendChild(e.getXMLOutput(doc));
			}
			return DesignElement.docToString(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	interface TypeElement extends Comparable<TypeElement> {
		public JPanel initializePanel();
		public Element getXMLOutput(Document doc);
		public void saveData();
		public void bindAll(BidiMap<String, String> entryMap);
		public static String COMMENT_DEFAULT = "[Comment]";
		public static String UNID_DEFAULT = "[UNID]";
		public static String ENTITY_DEFAULT = "[Entity]";
		public static String fixHex(String hex) {
			while(hex.length() < 8) {
				hex = "0" + hex;
			}
			hex = "0x" + hex;
			return hex;
		}
		public static void bindEntry(BidiMap<String, String> entryMap, String unid, String type) {
			//Attempt to make the unid into a hex string, if it is not already one.
			try {
				//8-digit hex is too cool for Integer
				unid = fixHex(Long.toHexString(Long.decode(unid.toLowerCase())));
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid UNID: " + unid + "[" + type + "]");
				return;
			}
			if(!(StringUtils.isAlphanumeric(type) || type.contains("-") || type.contains("_"))) {
				JOptionPane.showMessageDialog(null, "Invalid Type: " + type + "[" + unid + "]");
			} else if(entryMap.containsValue(type)) {
				JOptionPane.showMessageDialog(null, "Type Conflict: " + type + " [" + unid + " " + entryMap.getKey(type) + "]");
			} else {
				entryMap.put(unid, type);
			}
			/*
			if(entryMap.containsKey(unid)) {
				JOptionPane.showMessageDialog(null, "UNID Conflict: " + unid + " [" + type + " " + entryMap.get(unid) + "]");
			}
			*/
		}
		public static JPanel createContainerPanel() {
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			return container;
		}
		public static JTextField createUNIDField(String unid, boolean editable) {
			JTextField result = createTextField(/*(unid.startsWith("0x") ? "" : "0x") + */ unid, editable);
			
			/*result.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent arg0) {
					if(result.getCaretPosition() < 3 && arg0.getKeyCode() != arg0.VK_RIGHT) {
						arg0.consume();
					}
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {
					if(result.getCaretPosition() < 2) {
						e.consume();
					}
				}
			});
			*/
			return result;
		}
		public static JTextField createEntityField(String type, boolean editable) {
			JTextField result = createTextField(type, editable);
			result.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
	                char c = e.getKeyChar();
	                if(
	                		//Do not allow types to start with a digit
	                		(result.getText().isEmpty() && Character.isDigit(c)) ||
	                		//Do not allow special characters
	                		!(Character.isAlphabetic(c) || Character.isDigit(c))
	                		) {
	                	e.consume();
	                	}
				}
			});
			return result;
		}
		public default int compareTo(TypeElement o) {
			return 0;
		}
	}
	//Specifies a single type that will get an automatically-generated UNID
	class Type implements TypeElement {
		protected String comment;
		protected String type;
		
		//Retrieve user input from JTextField variables when saving
		protected JTextArea field_comment;
		protected JTextField field_type;
		public Type() {
			this(ENTITY_DEFAULT, COMMENT_DEFAULT);
		}
		public Type(String comment, String type) {
			this.comment = comment;
			this.type = type;
			field_comment = createTextArea(comment, true);
			field_type = createTextField(type, true);
		}
		@Override
		public JPanel initializePanel() {
			JPanel container = TypeElement.createContainerPanel();
			container.add((field_comment = createTextArea(comment, true)));
			container.add(field_type = TypeElement.createEntityField(type, true));
			return container;
		}
		@Override
		public void saveData() {
			comment = field_comment.getText();
			type = field_type.getText();
		}
		@Override
		public void bindAll(BidiMap<String, String> entryMap) {
		}
		@Override
		public Element getXMLOutput(Document doc) {
			Element result = doc.createElement("Type");
			result.setAttribute("comment", comment);
			result.setAttribute("type", type);
			return result;
		}
	}
	//Specifies a single type bound to a UNID
	class TypeEntry extends Type {
		private String unid;
		JTextField field_unid;
		public TypeEntry() {
			this(UNID_DEFAULT);
		}
		public TypeEntry(String unid) {
			super();
			this.unid = unid;
			field_unid = TypeElement.createUNIDField(unid, true);
		}
		public TypeEntry(String comment, String unid, String type) {
			super(comment, type);
			this.unid = unid;
			field_unid = TypeElement.createUNIDField(unid, true);
		}
		public JPanel initializePanel() {
			JPanel container = TypeElement.createContainerPanel();
			container.add((field_comment = createTextArea(comment, true)));
			JPanel subrow = new JPanel();
			subrow.setLayout(new GridLayout(0, 2));
			subrow.add(field_unid = TypeElement.createUNIDField(unid, true));
			subrow.add(field_type = TypeElement.createEntityField(type, true));
			container.add(subrow);
			return container;
		}
		@Override
		public void saveData() {
			super.saveData();
			unid = field_unid.getText();
		}
		public void bindAll(BidiMap<String, String> entryMap) {
			TypeElement.bindEntry(entryMap, unid, type);
		}
		public Element getXMLOutput(Document doc) {
			Element result = doc.createElement("TypeEntry");
			result.setAttribute("comment", comment);
			result.setAttribute("unid", unid);
			result.setAttribute("type", type);
			return result;
		}
		public String getUNID() {
			return unid;
		}
		public int compareTo(TypeElement e) {
			try {
				long unid = Long.decode(this.unid);
				if(e instanceof TypeEntry) {
					long unid_e = Long.decode(((TypeEntry) e).unid);
					if(unid < unid_e) {
						return -1;
					} else if(unid > unid_e) {
						return 1;
					}
				} else if(e instanceof TypeRange) {
					long unid_min_e = Long.decode(((TypeRange) e).getUNIDMin());
					long unid_max_e = Long.decode(((TypeRange) e).getUNIDMax());
					if(unid < unid_min_e) {
						return -1;
					} else if(unid > unid_max_e) {
						return 1;
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return 0;
		}
	}
	//Specifies a group of types that will get automatically-generated UNIDs
	class TypeGroup implements TypeElement {
		protected String comment;
		public final List<String> types;
		
		protected JTextArea field_comment;
		protected List<JTextField> field_types;
		
		public TypeGroup() {
			this(COMMENT_DEFAULT, new String[] {"[Insert Type here. Press Enter to add another Type and Backspace to remove the Type.]"});
		}
		public TypeGroup(String comment, String[] types) {
			this.comment = comment;
			this.types = new ArrayList<String>();
			this.types.addAll(Arrays.asList(types));
			
			field_comment = new JTextArea(comment);
			field_types = new LinkedList<JTextField>();
			for(String s : types) {
				field_types.add(createEntityField(null, s, true));
			}
		}
		/*
		public void createAddEntityButton(JPanel container) {
			JButton addEntity = createJButton("New Sub-Entity");
			addEntity.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					types.add(ENTITY_DEFAULT);
					JTextField field_type = createEntityField(container, ENTITY_DEFAULT, true);
					field_types.add(field_type);
					container.add(field_type);
					//Source Question: https://stackoverflow.com/questions/9650874/java-swing-obtain-window-jframe-from-inside-a-jpanel
					//Source Answer: https://stackoverflow.com/a/9650915
					((JFrame) SwingUtilities.getWindowAncestor(container)).pack();
				}
			});
			container.add(addEntity);
		}
		*/
		@Override
		public JPanel initializePanel() {
			JPanel container = TypeElement.createContainerPanel();
			container.add((field_comment = createTextArea(comment, true)));
			field_types.clear();
			for(String type : types) {
				JTextField field_type = createEntityField(container, type, true);
				field_types.add(field_type);
				container.add(field_type);
			}
			//createAddEntityButton(container);
			
			return container;
		}
		public JTextField createEntityField(JPanel panel, String type, boolean editable) {
			JTextField result = TypeElement.createEntityField(type, editable);
			result.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					int index = field_types.indexOf(result);
					System.out.println("Field Index: " + index);
					//Fields are stacked with 0 at the bottom
	                switch(e.getKeyCode()) {
	                case VK_UP:
	                	//Move focus up
	                	field_types.get((index-1)%field_types.size()).requestFocus();
	                	break;
	                case VK_DOWN:
	                	field_types.get((index+1)%field_types.size()).requestFocus();
	                	break;
	                case VK_BACK_SPACE:
	                	if(result.getText().length() == 0 && index > 0) {
	                		System.out.println("Backspace");
	                    	//If empty and pressing backspace, delete
	                    	field_types.remove(index);
	                    	types.remove(index);
	                    	panel.remove(result);
	                    	//If we are second to last or earlier, then shift focus to the component that took this index. Otherwise, shift focus to previous component
	                    	field_types.get(
	                    			(index < field_types.size() - 1) ? index : index - 1
	                    			).requestFocus();
	        				//Source Question: https://stackoverflow.com/questions/9650874/java-swing-obtain-window-jframe-from-inside-a-jpanel
	        				//Source Answer: https://stackoverflow.com/a/9650915
	                    	((JFrame) SwingUtilities.getWindowAncestor(panel)).pack();
	                	}
	                	break;
	                case VK_ENTER:
	                	System.out.println("Enter");
	                	//If pressing enter, insert a new type entry and field after this
	                	JTextField field = createEntityField(panel, "", true);
	                	int index_next = index+1;
	                	field_types.add(index_next, field);
	                	types.add(index_next, "");
	                	panel.add(field);
	                	//Move focus to the next component
	                	field.requestFocus();
	    				//Source Question: https://stackoverflow.com/questions/9650874/java-swing-obtain-window-jframe-from-inside-a-jpanel
	    				//Source Answer: https://stackoverflow.com/a/9650915
	                	((JFrame) SwingUtilities.getWindowAncestor(panel)).pack();
	                	break;
	                }
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent e) {
					char c = e.getKeyChar();
					if(
	                		//Do not allow types to start with a digit
	                		(result.getText().isEmpty() && Character.isDigit(c)) ||
	                		//Do not allow special characters
	                		!(Character.isAlphabetic(c) || Character.isDigit(c))
	                		) {
	                	e.consume();
	                	System.out.println("Consumed");
	                }
				}
			});
			return result;
		}
		@Override
		public void saveData() {
			comment = field_comment.getText();
			for(int i = 0; i < Math.min(types.size(), field_types.size()); i++) {
				types.set(i, field_types.get(i).getText());
			}
		}
		@Override
		public void bindAll(BidiMap<String, String> entryMap) {}
		@Override
		public Element getXMLOutput(Document doc) {
			Element result = doc.createElement("TypeGroup");
			result.setAttribute("comment", comment);
			result.setAttribute("types", listToString(types));
			return result;
		}
		public String listToString(List<String> list) {
			String result = "";
			int last = list.size() - 1;
			for(String s : list.subList(0, last)) {
				result += s + " ";
			}
			result += list.get(last);
			return result;
		}
	}
	//Specifies a group of types bound to a range of UNIDs on an interval
	class TypeRange extends TypeGroup {
		private String unid_min, unid_max;
		private JTextField field_unid_min, field_unid_max;
		public TypeRange() {
			this("[Min UNID]", "[Max UNID]");
		}
		public TypeRange(String unid_min, String unid_max) {
			super();
			this.unid_min = unid_min;
			this.unid_max = unid_max;
			field_unid_min = TypeElement.createUNIDField(unid_min, true);
			field_unid_max = TypeElement.createUNIDField(unid_max, true);
		}
		public TypeRange(String comment, String unid_min, String unid_max, String... types) {
			super(comment, types);
			this.unid_min = unid_min;
			this.unid_max = unid_max;
			field_unid_min = TypeElement.createUNIDField(unid_min, true);
			field_unid_max = TypeElement.createUNIDField(unid_max, true);
		}
		public JPanel initializePanel() {
			JPanel container = TypeElement.createContainerPanel();
			container.add((field_comment = createTextArea(comment, true)));
			JPanel subrow = new JPanel();
			subrow.setLayout(new GridLayout(0, 2));
			subrow.add(field_unid_min = TypeElement.createUNIDField(unid_min, true));
			subrow.add(field_unid_max = TypeElement.createUNIDField(unid_max, true));
			container.add(subrow);
			System.out.println("Entity Count: " + types.size());
			field_types.clear();
			for(String type : types) {
				JTextField field_type = createEntityField(container, type, true);
				field_types.add(field_type);
				container.add(field_type);
			}
			//createAddEntityButton(container);
			return container;
		}
		public String getUNIDMin() {
			return unid_min;
		}
		public String getUNIDMax() {
			return unid_max;
		}
		public void saveData() {
			super.saveData();
			unid_min = (field_unid_min.getText());
			unid_max = (field_unid_max.getText());
		}
		public Element getXMLOutput(Document doc) {
			Element result = doc.createElement("TypeRange");
			result.setAttribute("comment", comment);
			result.setAttribute("unid_min", unid_min);
			result.setAttribute("unid_max", unid_max);
			result.setAttribute("types", listToString(types));
			return result;
		}
		public void bindAll(BidiMap<String, String> entryMap) {
			Integer min = null, max = null;
			try {
				min = Integer.parseInt(unid_min);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid Minimum UNID: " + unid_min);
			}
			try {
				max = Integer.parseInt(unid_max);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid Maximum UNID: " + unid_max);
			}
			if(min != null || max != null) {
				int maxCount = (max - min) + 1;
				if(types.size() > maxCount) {
					JOptionPane.showMessageDialog(null, "Not enough UNIDs within range");
				}
				for(int i = 0; i < types.size() && i < maxCount; i++) {
					TypeElement.bindEntry(entryMap, "" + (min + i), types.get(i));
				}
			}
		}
		public int compareTo(TypeElement e) {
			try {
				long unid_min = Long.decode(this.unid_min);
				long unid_max = Long.decode(this.unid_max);
				if(e instanceof TypeEntry) {
					long unid_e = Long.decode(((TypeEntry) e).getUNID());
					if(unid_max < unid_e) {
						return -1;
					} else if(unid_min > unid_e) {
						return 1;
					}
				} else if(e instanceof TypeRange) {
					long unid_min_e = Long.decode(((TypeRange) e).getUNIDMin());
					long unid_max_e = Long.decode(((TypeRange) e).getUNIDMax());
					if(unid_max < unid_min_e) {
						return -1;
					} else if(unid_min > unid_max_e) {
						return 1;
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return 0;
		}
	}
}
/*
class Hex implements Comparable {
	private String value;
	public Hex(String value) {
		this.value = value;
	}
	public int compareTo(Object o) {
		if(o instanceof Hex) {
			String value_o = ((Hex) o).value;
			try {
				Long number = Long.decode(value);
				Long number_o = Long.decode(value_o);
				if(number < number_o) {
					return -1;
				} else if(number.equals(number_o)) {
					return 0;
				} else if(number > number_o) {
					return 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
*/