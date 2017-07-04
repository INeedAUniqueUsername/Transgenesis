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
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.synth.SynthSpinnerUI;
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
import xml.DesignAttribute.ValueType;

public class DesignElement {
	private String name;
	private final TreeMap<String, DesignAttribute> attributes;		
	private final List<DesignElement> subElements;
	private String text;
	
	private final List<DesignAttribute> requiredAttributes;				//Must be defined
	private final List<DesignElement> requiredSingleSubElements;			//Must have 1 of each
	private final List<DesignElement> optionalSingleSubElements;			//Can have 0 or 1 of each
	private final List<SubElementType> optionalMultipleSubElements;		//Can have 0, 1, or more of each
	
	public static DesignElement ele(String name) {
		return new DesignElement(name);
	}
	
	public DesignElement() {
		this.name = getClass().getSimpleName();
		
		attributes = new TreeMap<String, DesignAttribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<DesignAttribute>();
		requiredSingleSubElements = new ArrayList<DesignElement>();
		optionalSingleSubElements = new ArrayList<DesignElement>();
		optionalMultipleSubElements = new ArrayList<SubElementType>();
	}
	public DesignElement(String name) {
		this.name = name;
		
		attributes = new TreeMap<String, DesignAttribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<DesignAttribute>();
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
	public void setName(String name) {
		this.name = name;
	}
	public void addOptionalSingleSubElements(DesignElement...subelements) {
		optionalSingleSubElements.addAll(Arrays.asList(subelements));
	}
	public void addOptionalMultipleSubElements(SubElementType...subelements) {
		optionalMultipleSubElements.addAll(Arrays.asList(subelements));
	}
	
	public void addAttributes(DesignAttribute...attributes) {
		for(DesignAttribute a : attributes) {
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
		DesignAttribute a = attributes.get(name);
		if(a == null) {
			System.out.println("Unknown attribute: " + name);
			attributes.put(name, new DesignAttribute(name, ValueType.STRING, value));
		} else {
			attributes.get(name).setValue(value);
		}
	}
	
	public boolean validate() {
		for(DesignAttribute a : requiredAttributes) {
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
	public List<DesignAttribute> getAttributes() {
		return new ArrayList<DesignAttribute>(attributes.values());
	}
	public TreeMap<String, DesignAttribute> getAttributesMap() {
		return attributes;
	}
	public List<DesignElement> getSubElements() {
		return subElements;
	}
	public String getText() {
		return text;
	}
	
	public DesignAttribute getAttributeByName(String name) {
		return attributes.get(name);
	}
	public List<DesignElement> getSubElementsByName(String name) {
		List<DesignElement> result = new ArrayList<DesignElement>();
		for(DesignElement e : subElements) {
			if(e.getName().equalsIgnoreCase(name)) {
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
	public String getXMLOutput() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			doc.appendChild(getOutput(doc));
			
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
	public String getXMLDefinition() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			doc.appendChild(getDefinition(doc));
			
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
	public Element getDefinition(Document doc) {
		return getDefinition(doc, "", new HashMap<DesignElement, String>());
	}
	//Format of seen: element, id
	public Element getDefinition(Document doc, String parentName, HashMap<DesignElement, String> seen) {
		String id = seen.get(this);
		Element result;
		//Check if we already defined an element just like this one
		if(id == null) {
			//Add a definition of this element
			id = parentName + "_" + name;
			seen.put(this, id);
			
			System.out.println("-Defining: " + id);
			System.out.println("--Hashcode: " + hashCode());
			System.out.println("---Attributes: " + attributes);
			System.out.println("----Subelements (RequiredSingle): " + subElements);
			System.out.println("-----Subelements (OptionalSingle): " + optionalSingleSubElements);
			System.out.println("------Subelements (OptionalMultiple: " + optionalMultipleSubElements);
			
			result = doc.createElement("ElementDefinition");
			result.setAttribute("id", id);
			result.setAttribute("name", name);
			Element child;
			for(DesignAttribute a : getAttributes()) {
				child = a.getDefinition(doc);
				result.appendChild(child);
			}
			for(DesignElement e : subElements) {
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "requiredSingle");
				result.appendChild(child);
			}
			for(DesignElement e : optionalSingleSubElements) {
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "optionalSingle");
				result.appendChild(child);
			}
			//Recursion issue, probably due to the fact that attributes that are equal still have different hash codes
			for(SubElementType s : optionalMultipleSubElements) {
				DesignElement e = s.get();
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "optionalMultiple");
				result.appendChild(child);
			}
		} else {
			System.out.println("--Referencing: " + id);
			//Make a reference to the original element
			result = doc.createElement("ElementReference");
			result.setAttribute("id", id);
			result.setAttribute("name", name);
		}
		return result;
	}
	public Element getOutput(Document doc) {
		Element child = doc.createElement(getName());
		for(DesignAttribute a : getAttributes()) {
			if(a.getValue().isEmpty()) {
				continue;
			}
			System.out.println("Attribute");
			System.out.println(a.getValue());
			child.setAttribute(a.getName(), a.getValue().replaceAll("\\&", "&amp;"));
		}
		for(DesignElement e : getSubElements()) {
			System.out.println("Child");
			child.appendChild(e.getOutput(doc));
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
		optionalMultipleSubElements.forEach((SubElementType e) -> addableElements.add(e.get()));
		return addableElements;
	}
	public DesignElement getAddableElement(String name) {
		ArrayList<DesignElement> addableElements = new ArrayList<>();
		addableElements.addAll(requiredSingleSubElements);
		addableElements.addAll(optionalSingleSubElements);
		optionalMultipleSubElements.forEach((SubElementType s) -> {
			addableElements.add(s.get());
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
		
		List<DesignAttribute> attributes = getAttributes();
		if(attributes.size() == 0) {
			JLabel label = new JLabel("No attributes");
			label.setFont(Window.FONT_LARGE);
			labelPanel.add(label);
		} else {
			for(DesignAttribute a : attributes) {
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
		for(DesignAttribute a : attributes.values()) {
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
	public static ArrayList<DesignElement> seen = new ArrayList<>();
	public String toMinistryMarkdown() {
		return bulletWiki(1) + toMinistryMarkdown(1);
	}
	public String toMinistryMarkdown(int level) {
		String result = "";
		result += /*bullet(level) +*/ name;
		//Make sure that we don't get infinite recursion
		if(seen.indexOf(this) != -1) {
			return result;
		}
		System.out.println(name);
		seen.add(this);
		for(DesignAttribute a : attributes.values()) {
			result += line(bulletWiki(level+1) + a.toMinistryMarkdown());
		}
		for(DesignElement e : subElements) {
			result += line(bulletWiki(level+1) + "1" + " " + e.toMinistryMarkdown(level+1));
		}
		for(DesignElement e : optionalSingleSubElements) {
			result += line(bulletWiki(level+1) + "0|1" + " " + e.toMinistryMarkdown(level+1));
		}
		for(SubElementType e : optionalMultipleSubElements) {
			result += line(bulletWiki(level+1) + "0|+" + " " + e.get().toMinistryMarkdown(level+1));
		}
		return result;
	}
	public static String line(String line) {
		return line.isEmpty() ? "" : "\n" + line;
	}
	public static String bulletMinistry(int level) {
		String result = "";
		for(int i = 0; i < level; i++) {
			result += "*";
		}
		return result + " ";
	}
	public static String bulletWiki(int level) {
		String result = "";
		for(int i = 0; i < level; i++) {
			result += "  ";
		}
		return result + "* ";
	}
	public boolean equals(Object o) {
		if(o instanceof DesignElement) {
			DesignElement e = (DesignElement) o;
			return
					name.equals(e.name) &&
					attributes.equals(e.attributes) &&
					subElements.equals(e.subElements) &&
					optionalSingleSubElements.equals(e.optionalSingleSubElements) &&
					optionalMultipleSubElements.equals(e.optionalMultipleSubElements);
		}
		return false;
	}
	public int hashCode() {
		return Objects.hash(
				name,
				attributes,
				subElements,
				optionalSingleSubElements,
				optionalMultipleSubElements
				);
	}
}
