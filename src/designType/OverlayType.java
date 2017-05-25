package designType;

import designType.subElements.Event;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class OverlayType extends Type {
	public OverlayType() {
		super("OverlayType");
		addAttributes(
				new Attribute("disarm", ValueType.BOOLEAN, "false"),
				new Attribute("paralyze", ValueType.BOOLEAN, "false"),
				new Attribute("spin", ValueType.BOOLEAN, "false"),
				new Attribute("drag", ValueType.INTEGER, "100"),
				new Attribute("absorbAdj", ValueType.INTEGER_SEQUENCE, "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"),
				new Attribute("weaponSuppress", ValueType.STRING, "")
				);
		getOptionalSingleByName("Events").addOptionalSingleSubElements(
				new Event("OnCreate"),
				new Event("OnUpdate"),
				new Event("OnDamage"),
				new Event("OnDestroy"),
				new Event("OnObjDestroyed")
				);
	}
}