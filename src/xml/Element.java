package xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import xml.Attribute.ValueType;

public class Element implements IElement {

	private final String name;
	private LinkedHashMap<String, Attribute> attributes;
	private LinkedHashMap<String, IElement> subelements;
	String text;
	public Element() {
		name = getClass().getSimpleName();
		initializeContent();
	}
	public Element(String name) {
		this.name = name;
		initializeContent();
	}
	private void initializeContent() {
		attributes = new LinkedHashMap<String, Attribute>();
		subelements = new LinkedHashMap<String, IElement>();
		text = "";
	}
	public String getName() {
		return name;
	}
	@Override
	public List<Attribute> getAttributes() {
		return new ArrayList<Attribute>(attributes.values());
	}
	@Override
	public List<IElement> getSubElements() {
		return new ArrayList<IElement>(subelements.values());
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
	public void addAttributes(Attribute...attributes) {
		for(Attribute a : attributes) {
			this.attributes.put(a.getName(), a);
		}
	}
	public void addSubElements(IElement...subelements) {
		for(IElement e : subelements) {
			this.subelements.put(e.getName(), e);
		}
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String toString() {
		return getName();
	}
	
}
