package xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import xml.Attribute.ValueType;

public class Element implements IElement {

	private String name;
	private List<Attribute> attributes;
	private List<IElement> subelements;
	String text;
	public Element() {
		name = getClass().getSimpleName();
		attributes = new LinkedList<Attribute>();
		subelements = new LinkedList<IElement>();
		text = "";
	}
	public Element(String name) {
		this();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	@Override
	public List<Attribute> getAttributes() {
		return attributes;
	}
	@Override
	public List<IElement> getSubElements() {
		return subelements;
	}
	public String getText() {
		return text;
	}
	
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttributes(Attribute...attributes) {
		this.attributes.addAll(Arrays.asList(attributes));
	}
	public void setSubElements(List<IElement> subelements) {
		this.subelements = subelements;
	}
	public void addSubElements(IElement...subelements) {
		this.subelements.addAll(Arrays.asList(subelements));
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String toString() {
		return getName();
	}
	
}
