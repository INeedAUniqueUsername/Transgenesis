package designType;

import xml.Element;

public class Type extends Element {
	public Type(String name) {
		super(name);
	}
	public String getDisplayName() {
		String result = getAttributeByName("unid").getValue();
		if(!result.equals("")) {
			return String.format("%-12s", super.getDisplayName()) + result;
		}
		return super.getDisplayName();
	}
}
