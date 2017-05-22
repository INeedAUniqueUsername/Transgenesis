package designType;

import subelements.CodeBlock;
import xml.Attribute;
import xml.Attribute.ValueType;
import xml.Element;

public class OverlayType extends DesignType {
	public OverlayType() {
		super();
		addAttributes(
				new Attribute("disarm", ValueType.BOOLEAN, "false"),
				new Attribute("paralyze", ValueType.BOOLEAN, "false"),
				new Attribute("spin", ValueType.BOOLEAN, "false"),
				new Attribute("drag", ValueType.INTEGER, "100"),
				new Attribute("absorbAdj", ValueType.INTEGER_SEQUENCE, "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"),
				new Attribute("weaponSuppress", ValueType.STRING, "")
				);
		addSubElements(
				new CodeBlock("OnCreate"),
				new CodeBlock("OnUpdate"),
				new CodeBlock("OnDamage"),
				new CodeBlock("OnDestroy"),
				new CodeBlock("OnObjDestroyed")
				);
	}
}
