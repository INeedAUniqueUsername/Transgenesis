package designType;

import xml.DesignElement;

public class Type extends DesignElement {
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
