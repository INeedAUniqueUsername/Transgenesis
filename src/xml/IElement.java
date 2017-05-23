package xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.Set;

public interface IElement {
	public String getName();
	public default String getXML() {
		return getXML("");
	}
	public default String getXML(String tabs) {
		String tabs_subelements = tabs + "\t";
		String tabs_attributes = tabs + "\t\t";
		
		String name = getName();
		Collection<Attribute> attributes = getAttributes();
		Collection<IElement> subelements = getSubElements();
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
			for(IElement e : subelements) {
				result += "\n" + e.getXML(tabs_subelements);
			}
			result += "\n" + tabs + "</" + name + ">";
		} else {
			result += "/>";
		}
		
		return result;
	}
	public List<Attribute> getAttributes();
	public List<IElement> getSubElements();
	public String getText();
	
	public default DefaultMutableTreeNode toTreeNode() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this);
		for (IElement e : getSubElements()) {
			node.add(e.toTreeNode());
		}
		return node;
	}
}
