package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class Sound extends Type {
	public Sound() {
		super();
		addRequiredAttributes(new Attribute("filename", ValueType.FILENAME));
	}
}
