package xml;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import designType.subElements.SubElementType;
import window.Frame;
import window.Window;
import xml.Attribute.ValueType;

public class DesignElement {
	private final String name;
	private final TreeMap<String, Attribute> attributes;		
	private final List<DesignElement> subElements;
	private String text;
	
	private final List<Attribute> requiredAttributes;				//Must be defined
	private final List<DesignElement> requiredSingleSubElements;			//Must have 1 of each
	private final List<DesignElement> optionalSingleSubElements;			//Can have 0 or 1 of each
	private final List<SubElementType> optionalMultipleSubElements;		//Can have 0, 1, or more of each
	
	public DesignElement() {
		this.name = getClass().getSimpleName();
		
		attributes = new TreeMap<String, Attribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<Attribute>();
		requiredSingleSubElements = new ArrayList<DesignElement>();
		optionalSingleSubElements = new ArrayList<DesignElement>();
		optionalMultipleSubElements = new ArrayList<SubElementType>();
	}
	public DesignElement(String name) {
		this.name = name;
		
		attributes = new TreeMap<String, Attribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<Attribute>();
		requiredSingleSubElements = new ArrayList<DesignElement>();
		optionalSingleSubElements = new ArrayList<DesignElement>();
		optionalMultipleSubElements = new ArrayList<SubElementType>();
	}
	public DesignElement(DesignElement source, String name) {
		this.name = name;
		attributes = source.attributes;
		subElements = source.subElements;
		text = "";
		requiredAttributes = source.requiredAttributes;
		requiredSingleSubElements = source.requiredSingleSubElements;
		optionalSingleSubElements = source.optionalSingleSubElements;
		optionalMultipleSubElements = source.optionalMultipleSubElements;
	}
	public void addOptionalSingleSubElements(DesignElement...subelements) {
		optionalSingleSubElements.addAll(Arrays.asList(subelements));
	}
	public void addOptionalMultipleSubElements(SubElementType...subelements) {
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
	public void appendText(String text) {
		this.text += text;
	}
	
	public void setAttribute(String name, String value) {
		Attribute a = attributes.get(name);
		if(a == null) {
			System.out.println("Unknown attribute: " + name);
			attributes.put(name, new Attribute(name, ValueType.STRING, value));
		} else {
			attributes.get(name).setValue(value);
		}
	}
	
	public boolean validate() {
		for(Attribute a : requiredAttributes) {
			if(a == null || !a.getValue().equals("") || !a.getValueType().isValid(a.getValue())) {
				return false;
			}
		}
		for(DesignElement e : requiredSingleSubElements) {
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
	public TreeMap<String, Attribute> getAttributesMap() {
		return attributes;
	}
	public List<DesignElement> getSubElements() {
		return subElements;
	}
	public String getText() {
		return text;
	}
	
	public Attribute getAttributeByName(String name) {
		return attributes.get(name);
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
	public DesignElement getOptionalSingleByName(String name) {
		for(DesignElement e : optionalSingleSubElements) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	
	public String toString() {
		return getDisplayName();
	}
	/*
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
	*/
	public String getXML() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			doc.appendChild(getXML(doc));
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Element getXML(Document doc) {
		Element child = doc.createElement(getName());
		for(Attribute a : getAttributes()) {
			if(a.getValue().isEmpty()) {
				continue;
			}
			System.out.println("Attribute");
			System.out.println(a.getValue());
			child.setAttribute(a.getName(), a.getValue().replaceAll("\\&", "&amp;"));
		}
		for(DesignElement e : getSubElements()) {
			System.out.println("Child");
			child.appendChild(e.getXML(doc));
		}
		return child;
	}
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (DesignElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
	public ArrayList<DesignElement> getAddableElements() {
		ArrayList<DesignElement> addableElements = new ArrayList<>();
		//Check if this element already has one instance of a single-only element
		Consumer<DesignElement> singleCheck = (DesignElement e) -> {
			if(!subElements.contains(e)) {
				addableElements.add(e);
			}
		};
		requiredSingleSubElements.forEach(singleCheck);
		optionalSingleSubElements.forEach(singleCheck);
		optionalMultipleSubElements.forEach((SubElementType e) -> addableElements.add(e.create()));
		return addableElements;
	}
	public DesignElement getAddableElement(String name) {
		ArrayList<DesignElement> addableElements = new ArrayList<>();
		addableElements.addAll(requiredSingleSubElements);
		addableElements.addAll(optionalSingleSubElements);
		optionalMultipleSubElements.forEach((SubElementType s) -> {
			addableElements.add(s.create());
		});
		for(DesignElement e : addableElements) {
			if(e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
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
		ArrayList<DesignElement> addableSubElements = getAddableElements();
		
		if(addableSubElements.size() == 0) {
			JLabel label = new JLabel("No subelements");
			label.setFont(Window.FONT_LARGE);
			subElementPanel.add(label);
		} else {
			for(DesignElement addable : addableSubElements) {
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
	public DesignElement clone() {
		DesignElement result = new DesignElement(name);
		copyFields(result);
		return result;
	}
	public void copyFields(DesignElement result) {
		for(Attribute a : attributes.values()) {
			if(requiredAttributes.contains(a)) {
				result.addAttributes(a.clone());
			} else {
				result.addAttributes(a.clone());
			}
		}
		for(DesignElement e : subElements) {
			result.addSubElements(e.clone());
		}
		for(DesignElement e : requiredSingleSubElements) {
			result.addSubElements(e.clone());
		}
		for(DesignElement e : optionalSingleSubElements) {
			result.addOptionalSingleSubElements(e.clone());
		}
		for(SubElementType e : optionalMultipleSubElements) {
			result.addOptionalMultipleSubElements(e);
		}
	}
}
