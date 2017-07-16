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
import javax.swing.JTextField;
import javax.swing.plaf.synth.SynthSpinnerUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.EntityDeclaration;
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
import panels.XMLPanel;
import window.FrameOld;
import window.Window;
import xml.DesignAttribute.ValueType;
import static window.Window.Fonts.*;
public class DesignElementOld {
	private String name;
	private final TreeMap<String, DesignAttribute> attributes;		
	private final List<DesignElementOld> subElements;
	private String text;
	
	private final List<DesignAttribute> requiredAttributes;				//Must be defined
	private final List<DesignElementOld> requiredSingleSubElements;			//Must have 1 of each
	private final List<DesignElementOld> optionalSingleSubElements;			//Can have 0 or 1 of each
	private final List<SubElementType> optionalMultipleSubElements;		//Can have 0, 1, or more of each
	
	public static DesignElementOld ele(String name) {
		return new DesignElementOld(name);
	}
	
	public DesignElementOld() {
		this.name = getClass().getSimpleName();
		
		attributes = new TreeMap<String, DesignAttribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElementOld>();
		text = "";
		requiredAttributes = new ArrayList<DesignAttribute>();
		requiredSingleSubElements = new ArrayList<DesignElementOld>();
		optionalSingleSubElements = new ArrayList<DesignElementOld>();
		optionalMultipleSubElements = new ArrayList<SubElementType>();
	}
	public DesignElementOld(String name) {
		this.name = name;
		
		attributes = new TreeMap<String, DesignAttribute>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<DesignElementOld>();
		text = "";
		requiredAttributes = new ArrayList<DesignAttribute>();
		requiredSingleSubElements = new ArrayList<DesignElementOld>();
		optionalSingleSubElements = new ArrayList<DesignElementOld>();
		optionalMultipleSubElements = new ArrayList<SubElementType>();
	}
	public DesignElementOld(DesignElementOld source, String name) {
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
	public void addOptionalSingleSubElements(DesignElementOld...subelements) {
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
	public void addSubElements(DesignElementOld...subelements) {
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
		for(DesignElementOld e : requiredSingleSubElements) {
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
	public List<DesignElementOld> getSubElements() {
		return subElements;
	}
	public String getText() {
		return text;
	}
	
	public DesignAttribute getAttributeByName(String name) {
		return attributes.get(name);
	}
	public List<DesignElementOld> getSubElementsByName(String name) {
		List<DesignElementOld> result = new ArrayList<DesignElementOld>();
		for(DesignElementOld e : subElements) {
			if(e.getName().equalsIgnoreCase(name)) {
				result.add(e);
			}
		}
		return result;
	}
	public DesignElementOld getOptionalSingleByName(String name) {
		for(DesignElementOld e : optionalSingleSubElements) {
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
			return docToString(doc).replaceAll("\\&amp;", "&");
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
			return docToString(doc);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String docToString(Document doc) throws TransformerException {
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
	}
	public Element getDefinition(Document doc) {
		return getDefinition(doc, "", new HashMap<DesignElementOld, String>());
	}
	public boolean isCodeBlock() {
		return attributes.isEmpty() && subElements.isEmpty() && optionalSingleSubElements.isEmpty() && optionalMultipleSubElements.isEmpty();
	}
	public boolean isSubSet(DesignElementOld e) {
		return
				attributes.values().containsAll(e.getAttributes()) &&
				subElements.containsAll(e.subElements) &&
				optionalSingleSubElements.containsAll(e.optionalSingleSubElements) &&
				optionalMultipleSubElements.containsAll(e.optionalMultipleSubElements);
	}
	//Format of seen: element, id
	public Element getDefinition(Document doc, String parentName, HashMap<DesignElementOld, String> seen) {
		String inherit = null;
		//Prevent code blocks from inheriting from other code blocks
		if(!isCodeBlock()) {
			//See if we can inherit from an element with identical attributes/subelements
			inherit = seen.get(this);
			/*
			if(inherit != null) {
				subElements.clear();
				optionalSingleSubElements.clear();
				optionalMultipleSubElements.clear();
			} else {
				//See if we can inherit from a partial match
				for(DesignElementOld other : seen.keySet()) {
					if(!other.isCodeBlock() && isSubSet(other)) {
						inherit = seen.get(other);
						//Remove duplicate attributes/subelements
						subElements.removeIf((DesignElementOld sub) -> {
							return other.subElements.contains(sub);
						});
						optionalSingleSubElements.removeIf((DesignElementOld sub) -> {
							return other.optionalSingleSubElements.contains(sub);
						});
						optionalMultipleSubElements.removeIf((SubElementType sub) -> {
							return other.optionalMultipleSubElements.contains(sub);
						});
					}
				}
			}
			*/
			
		}
		Element result = doc.createElement("Element");
		//Check if we already defined an element just like this one
		result.setAttribute("name", name);
		if(inherit == null) {
			//Add a definition of this element
			String id = parentName + "_" + name;
			seen.put(this, id);
			
			Element child;
			for(DesignAttribute a : getAttributes()) {
				child = a.getDefinition(doc);
				result.appendChild(child);
			}
			for(DesignElementOld e : subElements) {
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "1");
				result.appendChild(child);
			}
			for(DesignElementOld e : optionalSingleSubElements) {
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "?");
				result.appendChild(child);
			}
			//Recursion issue, probably due to the fact that attributes that are equal still have different hash codes
			for(SubElementType s : optionalMultipleSubElements) {
				DesignElementOld e = s.get();
				child = e.getDefinition(doc, name, seen);
				child.setAttribute("category", "*");
				result.appendChild(child);
			}
		} else {
			result.setAttribute("inherit", inherit);
		}
		return result;
	}
	public Element getOutput(Document doc) {
		Element child = doc.createElement(getName());
		child.setTextContent(text);
		for(DesignAttribute a : getAttributes()) {
			if(a.getValue().isEmpty()) {
				continue;
			}
			System.out.println("Attribute");
			System.out.println(a.getValue());
			child.setAttribute(a.getName(), a.getValue());//.replaceAll("\\&", "&amp;")
		}
		for(DesignElementOld e : getSubElements()) {
			System.out.println("Child");
			child.appendChild(e.getOutput(doc));
		}
		return child;
	}
	public DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (DesignElementOld e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
	public ArrayList<DesignElementOld> getAddableElements() {
		ArrayList<DesignElementOld> addableElements = new ArrayList<>();
		//Check if this element already has one instance of a single-only element
		Consumer<DesignElementOld> singleCheck = (DesignElementOld e) -> {
			if(!subElements.contains(e)) {
				addableElements.add(e);
			}
		};
		requiredSingleSubElements.forEach(singleCheck);
		optionalSingleSubElements.forEach(singleCheck);
		optionalMultipleSubElements.forEach((SubElementType e) -> addableElements.add(e.get()));
		return addableElements;
	}
	public DesignElementOld getAddableElement(String name) {
		ArrayList<DesignElementOld> addableElements = new ArrayList<>();
		addableElements.addAll(requiredSingleSubElements);
		addableElements.addAll(optionalSingleSubElements);
		optionalMultipleSubElements.forEach((SubElementType s) -> {
			addableElements.add(s.get());
		});
		for(DesignElementOld e : addableElements) {
			if(e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}
	public void initializeFrame(XMLPanel panel) {
		JTextField nameField = panel.nameField;
		JPanel labelPanel = panel.labelPanel;
		JPanel fieldPanel = panel.fieldPanel;
		JPanel subElementPanel = panel.subElementPanel;
		JTextArea textArea = panel.textArea;
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		
		nameField.setText(name);
		nameField.setEditable(false);
		
		List<DesignAttribute> attributes = getAttributes();
		if(attributes.size() == 0) {
			JLabel label = new JLabel("No attributes");
			label.setFont(Large.f);
			labelPanel.add(label);
		} else {
			for(DesignAttribute a : attributes) {
				JLabel label = new JLabel(a.getName() + "=");
				label.setFont(Medium.f);
				labelPanel.add(label);
				JComponent inputField = a.getValueType().getInputField(a.getValue());
				fieldPanel.add(inputField);
			}
		}
		DesignElementOld me = this;
		ArrayList<DesignElementOld> addableSubElements = getAddableElements();
		
		if(addableSubElements.size() == 0) {
			JLabel label = new JLabel("No subelements");
			label.setFont(Large.f);
			subElementPanel.add(label);
		} else {
			for(DesignElementOld addable : addableSubElements) {
				JButton button = new JButton(addable.getDisplayName());
				button.setFont(Medium.f);
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						System.out.println("Create new element");
						subElements.add(addable);
						panel.addElement(addable);
						System.out.println("Created new element");
					}
				});
				subElementPanel.add(button);
			}
		}
		
		textArea.setText(getText());
	}
	public DesignElementOld clone() {
		DesignElementOld result = new DesignElementOld(name);
		copyFields(result);
		return result;
	}
	public void copyFields(DesignElementOld result) {
		for(DesignAttribute a : attributes.values()) {
			if(requiredAttributes.contains(a)) {
				result.addAttributes(a.clone());
			} else {
				result.addAttributes(a.clone());
			}
		}
		for(DesignElementOld e : subElements) {
			result.addSubElements(e.clone());
		}
		for(DesignElementOld e : requiredSingleSubElements) {
			result.addSubElements(e.clone());
		}
		for(DesignElementOld e : optionalSingleSubElements) {
			result.addOptionalSingleSubElements(e.clone());
		}
		for(SubElementType e : optionalMultipleSubElements) {
			result.addOptionalMultipleSubElements(e);
		}
	}
	public static ArrayList<DesignElementOld> seen = new ArrayList<>();
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
		for(DesignElementOld e : subElements) {
			result += line(bulletWiki(level+1) + "1" + " " + e.toMinistryMarkdown(level+1));
		}
		for(DesignElementOld e : optionalSingleSubElements) {
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
		if(o instanceof DesignElementOld) {
			DesignElementOld e = (DesignElementOld) o;
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
	public void finalizeLoad() {
		//Remove all empty lines
		String text_new = "";
		String[] lines = text.split("\n");
		for(int i = 0; i < lines.length - 1; i++) {
			String line = lines[i];
			if(!(line.matches("(\\s)+") || line.isEmpty())) {
				text_new += line + "\n";
			}
		}
		if(lines.length > 0) {
			text_new += lines[lines.length-1];
		}
		text = text_new.replace("(\\s)+$", "");;
		//text = text.replaceFirst("(\\s|\\n)+", "").replace("(\\s|\\n)+$", "");
	}
}
