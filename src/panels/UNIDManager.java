package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static panels.XMLPanel.*;

import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import mod.TranscendenceMod;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class UNIDManager {
	XMLPanel editor;
	JScrollPane pane = null;
	ArrayList<EntityElement> elements;
	public UNIDManager() {
		elements = new ArrayList<>();
	}
	public void setEditor(XMLPanel editor) {
		this.editor = editor;
	}
	public JScrollPane initializeFrame() {
		System.out.println("Initialize Frame");
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		//panel.setPreferredSize(frame.getSize());
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		JScrollPane result = createScrollPane(container);
		JButton exitButton = createJButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAllData();
				JFrame frame = ((JFrame) SwingUtilities.getWindowAncestor(container));
				frame.remove(result);
				frame.add(editor);
				frame.pack();
			}
		});
		container.add(exitButton);
		JButton addEntry = createJButton("New Type Entry");
		addEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addEntry) {
					elements.add(new EntityEntry());
					refreshFrame();
				}
			}
		});
		container.add(addEntry);
		JButton addRange = createJButton("New Type Range");
		addRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addRange) {
					elements.add(new EntityRange());
					refreshFrame();
				}
			}
		});
		container.add(addRange);
		for(int i = 0; i < elements.size(); i++) {
			System.out.println("Preparing Element " + i);
			int index = i;
			JPanel buttons = new JPanel();
			buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
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
			buttons.add(moveUpButton);
			
			JButton deleteButton = createJButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == deleteButton) {
						elements.remove(index);
						refreshFrame();
					}
				}
			});
			buttons.add(deleteButton);
			
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
			buttons.add(moveDownButton);
			
			JPanel row = new JPanel();
			row.add(buttons);
			row.add(elements.get(i).initializePanel());
			container.add(row, new CC().wrap());
		}
		return result;
	}
	public void refreshFrame() {
		System.out.println("Refreshing Frame");
		saveAllData();
		if(pane != null) {
			editor.frame.remove(pane);
		}
		editor.frame.add(pane = initializeFrame());
		System.out.println("Packing");
		editor.frame.pack();
		
	}
	public void saveAllData() {
		for(EntityElement e : elements) {
			e.saveData();
		}
	}
	public String getXMLOutput() {
		//UNID, Entity
		BidiMap<String, String> map = new TreeBidiMap<>();
		LinkedList<EntityElement> definedUNIDs = new LinkedList<>();
		LinkedList<EntityElement> generatedUNIDs = new LinkedList<>();
		for(EntityElement e : elements) {
			if(e instanceof EntityEntry || e instanceof EntityRange) {
				definedUNIDs.add(e);
			} else {
				generatedUNIDs.add(e);
			}
		}
		definedUNIDs.forEach((EntityElement e) -> {
			e.output(map);
		});
		generatedUNIDs.forEach((EntityElement e) -> {
			e.output(map);
		});
		String result = "";
		for(Entry<String, String> e : map.entrySet()) {
			result += String.format("\t<!ENTITY %-32s \"%s\">", e.getValue(), e.getKey()) + "\n";
		}
		return result;
	}
}

interface EntityElement {
	public JPanel initializePanel();
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
	public default JTextField createEntityField(String entity, boolean editable) {
		JTextField result = createTextField(entity, editable);
		result.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if(
                		//Do not allow entities to start with a digit
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
//Specifies a single entity that will get an automatically-generated UNID
class Entity implements EntityElement {
	protected String comment;
	protected String entity;
	
	//Retrieve user input from JTextField variables when saving
	protected JTextArea field_comment;
	protected JTextField field_entity;
	public Entity() {
		this(ENTITY_DEFAULT);
	}
	public Entity(String entity) {
		comment = COMMENT_DEFAULT;
		this.entity = entity;
		field_comment = createTextArea(COMMENT_DEFAULT, true);
		field_entity = createTextField(entity, true);
	}
	@Override
	public JPanel initializePanel() {
		JPanel container = EntityElement.createContainerPanel();
		container.add(createScrollPane(field_comment = createTextArea(comment, true)));
		container.add(field_entity = createEntityField(entity, true));
		return container;
	}
	@Override
	public void saveData() {
		comment = field_comment.getText();
		entity = field_entity.getText();
	}
	@Override
	public void output(BidiMap<String, String> entryMap) {
	}
}
//Specifies a single entity bound to a UNID
class EntityEntry extends Entity {
	private String unid;
	JTextField field_unid;
	public EntityEntry() {
		this(UNID_DEFAULT, ENTITY_DEFAULT);
	}
	public EntityEntry(String unid, String entity) {
		super(entity);
		this.unid = unid;
		field_unid = createTextField(unid, true);
	}
	public JPanel initializePanel() {
		JPanel container = EntityElement.createContainerPanel();
		container.add(createScrollPane(field_comment = createTextArea(comment, true)));
		JPanel subrow = new JPanel();
		subrow.setLayout(new GridLayout(0, 2));
		subrow.add(field_unid = createTextField(unid, true));
		subrow.add(field_entity = createEntityField(entity, true));
		container.add(subrow);
		return container;
	}
	@Override
	public void saveData() {
		super.saveData();
		unid = field_unid.getText();
	}
	public void output(BidiMap<String, String> entryMap) {
		EntityElement.store(entryMap, unid, entity);
	}
}
//Specifies a group of entities that will get automatically-generated UNIDs
class EntityGroup implements EntityElement {
	protected String comment;
	public final List<String> entities;
	
	protected JTextArea field_comment;
	protected List<JTextField> field_entities;
	
	public EntityGroup() {
		entities = new LinkedList<String>();
		comment = COMMENT_DEFAULT;
		field_comment = new JTextArea(comment);
		field_entities = new LinkedList<JTextField>();
	}
	public void createAddEntityButton(JPanel container) {
		JButton addEntity = createJButton("New Sub-Entity");
		addEntity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				entities.add(ENTITY_DEFAULT);
				JTextField field_entity = createEntityField("[Entity]", true);
				field_entities.add(field_entity);
				container.add(field_entity);
				((JFrame) SwingUtilities.getWindowAncestor(container)).pack();
			}
		});
		container.add(addEntity);
	}
	@Override
	public JPanel initializePanel() {
		JPanel container = EntityElement.createContainerPanel();
		container.add(createScrollPane(field_comment = createTextArea(comment, true)));
		field_entities.clear();;
		for(String entity : entities) {
			JTextField field_entity = createTextField(entity, true);
			field_entities.add(field_entity);
			container.add(field_entity);
		}
		createAddEntityButton(container);
		
		return container;
	}
	@Override
	public void saveData() {
		comment = field_comment.getText();
		entities.clear();
		field_entities.forEach((JTextField field_entity) -> {
			entities.add(field_entity.getText());
		});
	}
	@Override
	public void output(BidiMap<String, String> entryMap) {
		
	}
}
//Specifies a group of entities bound to a range of UNIDs on an interval
class EntityRange extends EntityGroup {
	private String unid_min, unid_max;
	private JTextField field_unid_min, field_unid_max;
	public EntityRange() {
		this("[Min UNID]", "[Max UNID]");
	}
	public EntityRange(String unid_min, String unid_max) {
		super();
		this.unid_min = unid_min;
		this.unid_max = unid_max;
		field_unid_min = createTextField(UNID_DEFAULT, true);
		field_unid_max = createTextField(UNID_DEFAULT, true);
	}
	public JPanel initializePanel() {
		JPanel container = EntityElement.createContainerPanel();
		container.add(createScrollPane(field_comment = createTextArea(comment, true)));
		JPanel subrow = new JPanel();
		subrow.setLayout(new GridLayout(0, 2));
		subrow.add(field_unid_min = createTextField(unid_min, true));
		subrow.add(field_unid_max = createTextField(unid_max, true));
		container.add(subrow);
		field_entities.clear();
		for(String entity : entities) {
			JTextField field_entity = createEntityField(entity, true);
			field_entities.add(field_entity);
			container.add(field_entity);
		}
		createAddEntityButton(container);
		return container;
	}
	public void saveData() {
		super.saveData();
		unid_min = (field_unid_min.getText());
		unid_max = (field_unid_max.getText());
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
		for(int i = 0; i < entities.size() && i < (max - min) + 1; i++) {
			EntityElement.store(entryMap, "" + (min + i), entities.get(i));
		}
	}
}