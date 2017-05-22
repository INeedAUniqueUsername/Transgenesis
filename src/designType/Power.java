package designType;

import subelements.CodeBlock;
import xml.Attribute;
import xml.Attribute.ValueType;

public class Power extends DesignType {
	public Power() {
		super();
		addAttributes(
				new Attribute("name", ValueType.STRING, ""),
				new Attribute("key", ValueType.CHARACTER, "")
				);
		addSubElements(
				new CodeBlock("OnShow"),
				new CodeBlock("OnInvokedByPlayer"),
				new CodeBlock("OnInvoke"),
				new CodeBlock("OnDestroyCheck")
				);
	}
}
