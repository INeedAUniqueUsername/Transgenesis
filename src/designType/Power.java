package designType;

import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class Power extends Type {
	public Power() {
		super("Power");
		addAttributes(
				new Attribute("name", ValueType.STRING, ""),
				new Attribute("key", ValueType.CHARACTER, "")
				);
		addOptionalSingleSubElements(
				new Element("OnShow"),
				new Element("OnInvokedByPlayer"),
				new Element("OnInvoke"),
				new Element("OnDestroyCheck")
				);
	}
}
