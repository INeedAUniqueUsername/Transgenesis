package designType;

import designType.subElements.Event;
import designType.subElements.ImageElement;
import xml.Attribute;
import xml.Attribute.ValueType;

public class SpaceEnvironment extends Type {
	public SpaceEnvironment() {
		super();
		addRequiredSingleSubElements(new ImageElement());
		addOptionalSingleSubElements(new ImageElement("EdgeMask"));
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
