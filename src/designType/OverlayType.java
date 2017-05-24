package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class OverlayType extends DesignType {
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
		addOptionalSubElements(new Events_OverlayType());
	}
}
class Events_OverlayType extends DesignElement {
	public Events_OverlayType() {
		super("Events");
		addOptionalSubElements(
				new DesignElement("OnCreate"),
				new DesignElement("OnUpdate"),
				new DesignElement("OnDamage"),
				new DesignElement("OnDestroy"),
				new DesignElement("OnObjDestroyed")
				);
	}
}