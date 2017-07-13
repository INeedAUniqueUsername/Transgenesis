package panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static panels.XMLPanel.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import mod.TranscendenceMod;

public class UNIDManager {
	LinkedList<EntityElement> elements;
	public UNIDManager() {
		elements = new LinkedList<>();
		
		for(int i = 0; i < 100; i++) {
			elements.add(new EntityEntry(0, "Test"));
		}
	}
	public JPanel getPanel(XMLPanel editor) {
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroll);
		panel.setLayout(new GridLayout(0, 2));
		
		for(EntityElement e : elements) {
			e.initializePanel(panel);
		}
		
		JButton addEntry = new JButton("New Entry");
		addEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addEntry) {
					elements.add(new EntityEntry());
				}
			}
		});
		
		JButton addRange = new JButton("New Range");
		addRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == addRange) {
					elements.add(new EntityRange());
				}
			}
		});
		
		return panel;
	}
}

interface EntityElement {
	public void initializePanel(JPanel panel);
	public void saveData();
}
//Specifies a single entity that will get an automatically-generated UNID
class Entity implements EntityElement {
	private String comment;
	private String entity;
	
	//Retrieve user input from JTextField variables when saving
	private JTextField field_comment;
	private JTextField field_entity;
	public Entity() {
		this("");
	}
	public Entity(String entity) {
		this.entity = entity;
	}
	@Override
	public void initializePanel(JPanel panel) {
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6));
		container.add(field_comment = createTextField(comment, true));
		container.add(field_entity = createTextField(entity, true));
		panel.add(container);
	}
	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
}
//Specifies a group of entities that will get automatically-generated UNIDs
class EntityGroup implements EntityElement {
	private String comment;
	public final HashSet<String> entities;
	
	private JTextField field_comment;
	private LinkedList<JTextField> field_entities;
	
	public EntityGroup() {
		entities = new HashSet<String>();
		comment = "";
	}
	@Override
	public void initializePanel(JPanel panel) {
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6));
		container.add(field_comment = createTextField(comment, true));
		field_entities = new LinkedList<JTextField>();
		for(String entity : entities) {
			JTextField field_entity = createTextField(entity, true);
			container.add(field_entity);
			field_entities.add(field_entity);
		}
		panel.add(container);
	}
	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		
	}
}
//Specifies a group of entities bound to a range of UNIDs on an interval
class EntityRange extends EntityGroup {
	private int unid_min, unid_max;
	public EntityRange() {
		this(0, 0);
	}
	public EntityRange(int unid_min, int unid_max) {
		super();
		this.unid_min = unid_min;
		this.unid_max = unid_max;
	}
	public void initializePanel(JPanel panel) {
		
		super.initializePanel(panel);
	}
}
//Specifies a single entity bound to a UNID
class EntityEntry extends Entity {
	private int unid;
	public EntityEntry() {
		this(0, "");
	}
	public EntityEntry(int unid, String entity) {
		super(entity);
		this.unid = unid;
	}
	public void initializePanel(JPanel panel) {
		super.initializePanel(panel);
	}
}