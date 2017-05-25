package designType.subElements;

import xml.Attribute;
import xml.Attribute.ValueType;
import xml.Element;

public class Text extends Element {
	String displayName;
	public Text(String id) {
		super();
		displayName = id;
		addRequiredAttributes(new Attribute("id", ValueType.STRING, id));
	}
	public String getDisplayName() {
		return displayName;
	}
	
	//Make uneditable
}
