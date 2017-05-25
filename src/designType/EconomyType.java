package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class EconomyType extends Type {
	public EconomyType() {
		super();
		addRequiredAttributes(
				new Attribute("id", ValueType.STRING),
				new Attribute("currency", ValueType.STRING),
				new Attribute("conversion", ValueType.INTEGER)
				);
	}
}
