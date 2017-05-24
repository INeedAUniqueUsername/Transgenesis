package designType;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

import window.Frame;
import window.Window;
import xml.Attribute;

public class DesignElement {
	private final String name;
	private final LinkedHashMap<String, Attribute> attributes;
	private final List<DesignElement> subElements;
	String text;
	
	private final List<Attribute> requiredAttributes;
	private final List<DesignElement> requiredSubElements;
	private final List<DesignElement> optionalSingleSubElements;
	private final List<DesignElement> optionalMultipleSubElements;
	public DesignElement(String name) {
		this.name = name;
		
		attributes = new LinkedHashMap<String, Attribute>();
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<Attribute>();
		requiredSubElements = new ArrayList<DesignElement>();
		optionalSingleSubElements = new ArrayList<DesignElement>();
		optionalMultipleSubElements = new ArrayList<DesignElement>();
	}
	protected void addRequiredAttributes(Attribute...attributes) {
		for(Attribute s : attributes) {
			this.attributes.put(s.getName(), s);
			requiredAttributes.add(s);
		}
	}
	protected void addRequiredSubElements(DesignElement...subelements) {
		for(DesignElement s : subelements) {
			subElements.add(s);
			requiredSubElements.add(s);
		}
	}
	protected void addOptionalSingleSubElements(DesignElement...subelements) {
		optionalSingleSubElements.addAll(Arrays.asList(subelements));
	}
	protected void addOptionalMultipleSubElements(DesignElement...subelements) {
		optionalMultipleSubElements.addAll(Arrays.asList(subelements));
	}
	
	public void addAttributes(Attribute...attributes) {
		for(Attribute a : attributes) {
			this.attributes.put(a.getName(), a);
		}
	}
	public void addSubElements(DesignElement...subelements) {
		this.subElements.addAll(Arrays.asList(subelements));
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean validate() {
		for(Attribute a : requiredAttributes) {
			if(a == null || !a.getValue().equals("") || !a.getValueType().isValid(a.getValue())) {
				return false;
			}
		}
		for(DesignElement e : requiredSubElements) {
			if(!e.validate()) {
				return false;
			}
		}
		return true;
	}
	
	public String getName() {
		return name;
	}
	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(attributes.values());
	}
	public List<DesignElement> getSubElements() {
		return subElements;
	}
	public String getText() {
		return text;
	}
	
	public Attribute getAttributeByName(String name) {
		for(Attribute a : attributes.values()) {
			if(a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	public List<DesignElement> getSubElementsByName(String name) {
		List<DesignElement> result = new ArrayList<DesignElement>();
		for(DesignElement e : subElements) {
			if(e.getName().equals(name)) {
				result.add(e);
			}
		}
		return result;
	}
	
	public String toString() {
		return getName();
	}
	
	public String getXML(String tabs) {
		String tabs_subelements = tabs + "\t";
		String tabs_attributes = tabs + "\t\t";
		
		String name = getName();
		List<Attribute> attributes = getAttributes();
		List<DesignElement> subelements = getSubElements();
		String text = getText();
		
		boolean hasAttributes = attributes.size() > 0;
		boolean hasSubelements = subelements.size() > 0;
		boolean hasText = text.length() > 0;
		String result = tabs + "<" + name;
		if(hasAttributes) {
			for(Attribute e : attributes) {
				result += "\n" + tabs_attributes + e.getName() + "=\t\t\"" + e.getValue() + "\""; 
			}
			result += "\n" + tabs_attributes;
		}
		
		if(hasSubelements || hasText) {
			result += ">";
			result += "\n" + tabs_subelements + text;
			for(DesignElement e : subelements) {
				result += "\n" + e.getXML(tabs_subelements);
			}
			result += "\n" + tabs + "</" + name + ">";
		} else {
			result += "/>";
		}
		
		return result;
	}
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (DesignElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
	public void initializeFrame(Frame frame) {
		JPanel labelPanel = frame.getAttributeLabelPanel();
		JPanel fieldPanel = frame.getAttributeFieldPanel();
		JPanel subElementPanel = frame.getSubElementPanel();
		JTextArea textArea = frame.getTextArea();
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		
		List<Attribute> attributes = getAttributes();
		if(attributes.size() == 0) {
			JLabel label = new JLabel("No attributes");
			label.setFont(Window.FONT_LARGE);
			labelPanel.add(label);
		} else {
			for(Attribute a : attributes) {
				JLabel label = new JLabel(a.getName() + "=");
				label.setFont(Window.FONT_MEDIUM);
				labelPanel.add(label);
				JComponent inputField = a.getValueType().getInputField(a.getValue());
				fieldPanel.add(inputField);
			}
		}
		DesignElement me = this;
		ArrayList<DesignElement> addableSubElements = new ArrayList<>(requiredSubElements);
		addableSubElements.addAll(optionalSingleSubElements);
		if(addableSubElements.size() == 0) {
			JLabel label = new JLabel("No subelements");
			label.setFont(Window.FONT_LARGE);
			subElementPanel.add(label);
		} else {
			for(DesignElement addable : addableSubElements) {
				if(!subElements.contains(addable)) {
					JButton button = new JButton(addable.getName());
					button.setFont(Window.FONT_LARGE);
					button.addActionListener(new ActionListener() {
	
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							System.out.println("Create new element");
							subElements.add(addable);
							frame.addElement(addable);
							System.out.println("Created new element");
						}
					});
					subElementPanel.add(button);
				}
			}
		}
		
		textArea.setText(getText());
	}
	public DesignElement clone() {
		DesignElement result = new DesignElement(name);
		copyFields(result);
		return result;
	}
	public void copyFields(DesignElement result) {
		for(Attribute a : attributes.values()) {
			if(requiredAttributes.contains(a)) {
				result.addRequiredAttributes(a.clone());
			} else {
				result.addAttributes(a.clone());
			}
		}
		for(DesignElement e : subElements) {
			result.addSubElements(e.clone());
		}
		for(DesignElement e : requiredSubElements) {
			result.addRequiredSubElements(e.clone());
		}
		for(DesignElement e : optionalSingleSubElements) {
			result.addOptionalSingleSubElements(e.clone());
		}
		for(DesignElement e : optionalMultipleSubElements) {
			result.addOptionalMultipleSubElements(e.clone());
		}
	}
}
