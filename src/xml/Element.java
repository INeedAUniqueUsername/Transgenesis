package xml;

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
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;

import window.Frame;
import window.Window;

public class Element {
	private final String name;
	private final LinkedHashMap<String, Attribute> attributes;
	private final List<Element> subElements;
	String text;
	
	private final List<Attribute> requiredAttributes;				//These attributes must not be empty
	private final List<Element> requiredSingleSubElements;			//Must have 1 of these elements
	private final List<Element> optionalSingleSubElements;	//Can have 0 or 1 of these elements
	private final List<Element> optionalMultipleSubElements;	//Can have 0 or multiple of these elements
	public Element() {
		this.name = getClass().getSimpleName();
		
		attributes = new LinkedHashMap<String, Attribute>();
		subElements = new ArrayList<Element>();
		text = "";
		requiredAttributes = new ArrayList<Attribute>();
		requiredSingleSubElements = new ArrayList<Element>();
		optionalSingleSubElements = new ArrayList<Element>();
		optionalMultipleSubElements = new ArrayList<Element>();
	}
	public Element(String name) {
		this.name = name;
		
		attributes = new LinkedHashMap<String, Attribute>();
		subElements = new ArrayList<Element>();
		text = "";
		requiredAttributes = new ArrayList<Attribute>();
		requiredSingleSubElements = new ArrayList<Element>();
		optionalSingleSubElements = new ArrayList<Element>();
		optionalMultipleSubElements = new ArrayList<Element>();
	}
	public void addRequiredAttributes(Attribute...attributes) {
		for(Attribute s : attributes) {
			this.attributes.put(s.getName(), s);
			requiredAttributes.add(s);
		}
	}
	public void addRequiredSingleSubElements(Element...subelements) {
		for(Element s : subelements) {
			subElements.add(s);
			requiredSingleSubElements.add(s);
		}
	}
	public void addOptionalSingleSubElements(Element...subelements) {
		optionalSingleSubElements.addAll(Arrays.asList(subelements));
	}
	public void addOptionalSingleSubElements(String...subelements) {
		for(String s : subelements) {
			optionalSingleSubElements.add(new Element(s));
		}
	}
	public void addOptionalMultipleSubElements(Element...subelements) {
		optionalMultipleSubElements.addAll(Arrays.asList(subelements));
	}
	public void addOptionalMultipleSubElements(String...subelements) {
		for(String s : subelements) {
			optionalMultipleSubElements.add(new Element(s));
		}
	}
	
	public void addAttributes(Attribute...attributes) {
		for(Attribute a : attributes) {
			this.attributes.put(a.getName(), a);
		}
	}
	public void addSubElements(Element...subelements) {
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
		for(Element e : requiredSingleSubElements) {
			if(!e.validate()) {
				return false;
			}
		}
		return true;
	}
	
	public String getName() {
		return name;
	}
	public String getDisplayName() {
		return name;
	}
	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(attributes.values());
	}
	public List<Element> getSubElements() {
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
	public List<Element> getSubElementsByName(String name) {
		List<Element> result = new ArrayList<Element>();
		for(Element e : subElements) {
			if(e.getName().equals(name)) {
				result.add(e);
			}
		}
		return result;
	}
	public Element getOptionalSingleByName(String name) {
		for(Element e : optionalSingleSubElements) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	
	public String toString() {
		return getDisplayName();
	}
	public String getXML() {
		return getXML("");
	}
	public String getXML(String tabs) {
		String tabs_subelements = tabs + "\t";
		String tabs_attributes = tabs + "\t\t";
		
		String name = getName();
		List<Attribute> attributes = getAttributes();
		List<Element> subelements = getSubElements();
		String text = getText();
		
		boolean hasAttributes = attributes.size() > 0;
		boolean hasSubelements = subelements.size() > 0;
		boolean hasText = text.length() > 0;
		String result = tabs + "<" + name;
		if(hasAttributes) {
			for(Attribute e : attributes) {
				String value = e.getValue();
				if(value.equals("")) {
					continue;
				}
				result += "\n" + tabs_attributes + String.format("%-40s", e.getName() + "=") + "\"" + value + "\""; 
			}
			result += "\n" + tabs_attributes;
		}
		
		if(hasSubelements || hasText) {
			result += ">";
			result += "\n" + text;
			for(Element e : subelements) {
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
		for (Element e : getSubElements()) {
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
		Element me = this;
		ArrayList<Element> addableSubElements = new ArrayList<>();
		
		//Check if this element already has one instance of a single-only element
		Consumer<Element> singleCheck = (Element e) -> {
			if(!subElements.contains(e)) {
				addableSubElements.add(e);
			}
		};
		requiredSingleSubElements.forEach(singleCheck);
		optionalSingleSubElements.forEach(singleCheck);
		addableSubElements.addAll(optionalMultipleSubElements);	//Can always add more optional-multiple elements
		
		if(addableSubElements.size() == 0) {
			JLabel label = new JLabel("No subelements");
			label.setFont(Window.FONT_LARGE);
			subElementPanel.add(label);
		} else {
			for(Element addable : addableSubElements) {
				JButton button = new JButton(addable.getDisplayName());
				button.setFont(Window.FONT_MEDIUM);
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
		
		textArea.setText(getText());
	}
	public Element clone() {
		Element result = new Element(name);
		copyFields(result);
		return result;
	}
	public void copyFields(Element result) {
		for(Attribute a : attributes.values()) {
			if(requiredAttributes.contains(a)) {
				result.addRequiredAttributes(a.clone());
			} else {
				result.addAttributes(a.clone());
			}
		}
		for(Element e : subElements) {
			result.addSubElements(e.clone());
		}
		for(Element e : requiredSingleSubElements) {
			result.addRequiredSingleSubElements(e.clone());
		}
		for(Element e : optionalSingleSubElements) {
			result.addOptionalSingleSubElements(e.clone());
		}
		for(Element e : optionalMultipleSubElements) {
			result.addOptionalMultipleSubElements(e.clone());
		}
	}
}
