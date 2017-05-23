package designType;

import java.util.ArrayList;
import java.util.List;

import xml.Element;

public class DesignElement extends Element {
	private final List<String> requiredAttributes;
	private final List<String> requiredSubElements;
	public DesignElement(String name) {
		super(name);
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
	public void validate() {
		
	}
}
