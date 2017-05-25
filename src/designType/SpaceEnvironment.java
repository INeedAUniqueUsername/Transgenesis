package designType;

import designType.subElements.Event;
import xml.Attribute;
import xml.Attribute.ValueType;

public class SpaceEnvironment extends Type {
	public SpaceEnvironment() {
		super();
		addRequiredSingleSubElements(new Image());
		addOptionalSingleSubElements(new Image("EdgeMask"));
		addAttributes(
				new Attribute("autoEdges", ValueType.BOOLEAN),
				new Attribute("dragFactor", ValueType.WHOLE),
				new Attribute("lrsJammer", ValueType.BOOLEAN, "false"),
				new Attribute("mapColor", ValueType.HEX_COLOR),
				new Attribute("opacity", ValueType.WHOLE),
				new Attribute("shieldJammer", ValueType.BOOLEAN, "false"),
				new Attribute("srsJammer", ValueType.BOOLEAN, "false")
				);
		getOptionalSingleByName("Events").addOptionalSingleSubElements(new Event("OnObjUpdate"));
	}
}
