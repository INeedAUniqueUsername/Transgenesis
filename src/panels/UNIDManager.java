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

import static panels.XMLPanel.*;
import static java.awt.event.KeyEvent.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
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
import javax.xml.stream.events.StartElement;
import javax.xml.transform.TransformerException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mod.TranscendenceMod;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import xml.DesignElementOld;

public class UNIDManager {
	XMLPanel editor;
	JScrollPane pane = null;
	ArrayList<TypeElement> elements;
	public UNIDManager() {
		elements = new ArrayList<>();
	}
	public void createFromXML(StartElement event) {
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
					event.getAttributeByName(new QName("types")).getValue().split("; ")
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
			elementButtons.setLayout(new GridLayout(3, 0));
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
			
			JButton deleteButton = createJButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == deleteButton) {
						elements.remove(index);
						refreshFrame();
					}
				}
			});
			elementButtons.add(deleteButton);
			
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
			row.add(elementButtons, new CC().width("20%"));
			row.add(elements.get(i).initializePanel(), new CC().width("80%"));
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
	public String getXMLOutput() {
		//UNID, Entity
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
			e.output(map);
		});
		generatedUNIDs.forEach((TypeElement e) -> {
			e.output(map);
		});
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
			Element data = doc.createElement("Data");
			data.setAttribute("id", "TransGenesis");
			doc.appendChild(metadata);
			metadata.appendChild(data);
			for(TypeElement e : elements) {
				metadata.appendChild(e.getXMLOutput(doc));
			}
			return DesignElementOld.docToString(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}

interface TypeElement {
	public JPanel initializePanel();
	public Element getXMLOutput(Document doc);
	public void saveData();
	public void output(BidiMap<String, String> entryMap);
	public static String COMMENT_DEFAULT = "[Comment]";
	public static String UNID_DEFAULT = "[UNID]";
	public static String ENTITY_DEFAULT = "[Entity]";
	public static void store(BidiMap<String, String> entryMap, String unid, String entry) {
		if(entryMap.containsKey(unid)) {
			JOptionPane.showMessageDialog(null, "UNID Conflict: " + unid);
		} else if(entryMap.containsValue(entry)) {
			JOptionPane.showMessageDialog(null, "Type Conflict: " + entry);
		} else {
			entryMap.put(unid, entry);
		}
	}
	public static JPanel createContainerPanel() {
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		return container;
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
	public Type(String type, String comment) {
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
	public void output(BidiMap<String, String> entryMap) {
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
		field_unid = createTextField(unid, true);
	}
	public TypeEntry(String comment, String unid, String type) {
		super(comment, type);
		this.unid = unid;
		field_unid = createTextField(unid, true);
	}
	public JPanel initializePanel() {
		JPanel container = TypeElement.createContainerPanel();
		container.add((field_comment = createTextArea(comment, true)));
		JPanel subrow = new JPanel();
		subrow.setLayout(new GridLayout(0, 2));
		subrow.add(field_unid = createTextField(unid, true));
		subrow.add(field_type = TypeElement.createEntityField(type, true));
		container.add(subrow);
		return container;
	}
	@Override
	public void saveData() {
		super.saveData();
		unid = field_unid.getText();
	}
	public void output(BidiMap<String, String> entryMap) {
		TypeElement.store(entryMap, unid, type);
	}
	public Element getXMLOutput(Document doc) {
		Element result = doc.createElement("TypeEntry");
		result.setAttribute("comment", comment);
		result.setAttribute("unid", unid);
		result.setAttribute("type", type);
		return result;
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
		field_types.clear();;
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
		for(int i = 0; i < field_types.size(); i++) {
			types.set(i, field_types.get(i).getText());
		}
	}
	@Override
	public void output(BidiMap<String, String> entryMap) {}
	@Override
	public Element getXMLOutput(Document doc) {
		Element result = doc.createElement("TypeGroup");
		result.setAttribute("comment", comment);
		result.setAttribute("types", listToString(types));
		return result;
	}
	public String listToString(List<String> list) {
		String result = "";
		String last = list.remove(list.size()-1);
		for(String s : list) {
			result += s + " ";
		}
		result += last;
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
		field_unid_min = createTextField(UNID_DEFAULT, true);
		field_unid_max = createTextField(UNID_DEFAULT, true);
	}
	public TypeRange(String comment, String unid_min, String unid_max, String... types) {
		super(comment, types);
		this.unid_min = unid_min;
		this.unid_max = unid_max;
		field_unid_min = createTextField(UNID_DEFAULT, true);
		field_unid_max = createTextField(UNID_DEFAULT, true);
	}
	public JPanel initializePanel() {
		JPanel container = TypeElement.createContainerPanel();
		container.add((field_comment = createTextArea(comment, true)));
		JPanel subrow = new JPanel();
		subrow.setLayout(new GridLayout(0, 2));
		subrow.add(field_unid_min = createTextField(unid_min, true));
		subrow.add(field_unid_max = createTextField(unid_max, true));
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
	public void output(BidiMap<String, String> entryMap) {
		int min = -1, max = -1;
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
		for(int i = 0; i < types.size() && i < (max - min) + 1; i++) {
			TypeElement.store(entryMap, "" + (min + i), types.get(i));
		}
	}
}