package designType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import xml.Attribute;

public class DesignElement {
	private final String name;
	private final LinkedHashMap<String, Attribute> attributes;
	private final List<DesignElement> subElements;
	String text;
	
	private final List<String> requiredAttributes;
	private final List<String> requiredSubElements;
	public DesignElement(String name) {
		this.name = name;
		
		attributes = new LinkedHashMap<String, Attribute>();
		subElements = new ArrayList<DesignElement>();
		text = "";
		requiredAttributes = new ArrayList<String>();
		requiredSubElements = new ArrayList<String>();
	}
	protected void addRequiredAttributes(String...attributes) {
		for(String s : attributes) {
			requiredAttributes.add(s);
		}
	}
	protected void addRequiredSubElements(String...subelements) {
		for(String s : subelements) {
			requiredSubElements.add(s);
		}
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
		for(String name : requiredAttributes) {
			Attribute a = getAttributeByName(name);
			if(a == null || a.getValue().equals("")) {
				return false;
			}
		}
		for(String name : requiredSubElements) {
			
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
}
