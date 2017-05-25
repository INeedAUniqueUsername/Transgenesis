package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class Image extends Type {
	public Image() {
		super();
		addRequiredAttributes(
				new Attribute("bitmap", ValueType.FILENAME),
				new Attribute("bitmask", ValueType.FILENAME)
				);
		addAttributes(
				new Attribute("backColor", ValueType.HEX_COLOR),
				new Attribute("hitMask", ValueType.FILENAME),
				new Attribute("loadOnUse", ValueType.BOOLEAN),
				new Attribute("noPM", ValueType.BOOLEAN),
				new Attribute("shadowMask", ValueType.FILENAME),
				new Attribute("sprite", ValueType.BOOLEAN)
				);
	}
}
