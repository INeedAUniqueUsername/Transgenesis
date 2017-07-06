package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xml.DesignAttribute.ValueType;
import static xml.DesignAttribute.*;

public class DesignElement {
	public static HashMap<String, DesignElement> DEFINITIONS;
	public static void getDefinitions() {
		DEFINITIONS = new HashMap<>();
		System.out.println("Loading definitions");
		try {
			File file = new File("C:\\Users\\Alex\\workspace\\TransGenesis\\Unimplemented Definitions");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			Element root = doc.getDocumentElement();
			root.normalize();
			generateDefinition(root);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void generateDefinition(Element e) {
		//Process our attributes first
		DesignElement result;
		String name = e.getAttribute("name");
		String inherit_id = e.getAttribute("inherit");
		result = new DesignElement(name);
		result.base = inherit_id;
		NodeList subelements = e.getChildNodes();
		for(int i = 0; i < subelements.getLength(); i++) {
			Element sub;
			if(subelements.item(i) instanceof Element) {
				sub = (Element) subelements.item(i);
			} else {
				continue;
			}
			
			switch(sub.getTagName()) {
			case "Attribute":
				result.addAttributes(att(sub.getAttribute("name"), ValueType.valueOf(sub.getAttribute("valueType")), sub.getAttribute("value")));
				break;
			case "Element":
				generateDefinition(sub);
				String sub_id = name + sub.getAttribute("name");
				switch(sub.getAttribute("category")) {
				case "":
				case "1":
					result.requiredSingleIDs.add(sub_id);
					break;
				case "+":
					result.requiredMultipleIDs.add(sub_id);
					break;
				case "?":
					result.optionalSingleIDs.add(sub_id);
					break;
				case "*":
					result.optionalMultipleIDs.add(sub_id);
					break;
				}
				break;
			}
		}
		Element parent = (Element) e.getParentNode();
		if(parent == null) {
			DEFINITIONS.put(name, result);
		} else {
			DEFINITIONS.put(parent.getTagName() + "_" + name, result);
		}
	}
	private String base;
	private String name;
	private TreeMap<String, DesignAttribute> attributes;		
	private List<DesignElement> subElements;
	private String text;
	
	private final List<String> requiredSingleIDs;			//Must have 1 of each
	private final List<String> requiredMultipleIDs;			//Must have 1 or more of each
	private final List<String> optionalSingleIDs;			//Can have 0 or 1 of each
	private final List<String> optionalMultipleIDs;		//Can have 0, 1, or more of each
	public DesignElement(String name) {
		base = "";
		this.name = name;
		
		attributes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		subElements = new ArrayList<>();
		text = "";
		requiredSingleIDs = new ArrayList<>();
		requiredMultipleIDs = new ArrayList<>();
		optionalSingleIDs = new ArrayList<>();
		optionalMultipleIDs = new ArrayList<>();
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addAttributes(DesignAttribute...attributes) {
		for(DesignAttribute a : attributes) {
			this.attributes.put(a.getName(), a);
		}
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
	
	public String toString() {
		return getDisplayName();
	}
	public DesignElement clone() {
		DesignElement result = new DesignElement(name);
		result.base = base;
		result.text = text;
		for(DesignAttribute a : attributes.values()) {
			 result.attributes.put(a.getName(), a.clone());
		}
		for(DesignElement e : subElements) {
			result.subElements.add(e.clone());
		}
		for(String s : requiredSingleIDs) {
			result.requiredSingleIDs.add(s);
		}
		for(String s : requiredMultipleIDs) {
			result.requiredMultipleIDs.add(s);
		}
		for(String s : optionalSingleIDs) {
			result.optionalSingleIDs.add(s);
		}
		for(String s : optionalMultipleIDs) {
			result.optionalMultipleIDs.add(s);
		}
		return result;
	}
	public void inherit() {
		DesignElement baseElement = DEFINITIONS.get(base);
		if(baseElement == null) {
			return;
		}
		text = baseElement.text;
		for(DesignAttribute a : attributes.values()) {
			 attributes.put(a.getName(), a.clone());
		}
		for(DesignElement e : baseElement.subElements) {
			subElements.add(e.clone());
		}
		for(String s : baseElement.requiredSingleIDs) {
			requiredSingleIDs.add(s);
		}
		for(String s : baseElement.requiredMultipleIDs) {
			requiredMultipleIDs.add(s);
		}
		for(String s : baseElement.optionalSingleIDs) {
			optionalSingleIDs.add(s);
		}
		for(String s : baseElement.optionalMultipleIDs) {
			optionalMultipleIDs.add(s);
		}
	}
}
