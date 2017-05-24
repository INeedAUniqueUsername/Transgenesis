package mod;

import designType.DesignElement;
import designType.DesignType;
import xml.Attribute;
import xml.Attribute.ValueType;

public class TranscendenceMod extends DesignElement {

	public TranscendenceMod(String name) {
		super(name);
		addRequiredAttributes(
				new Attribute("apiVersion", ValueType.INTEGER, ""),
				new Attribute("autoInclude", ValueType.BOOLEAN, "false"),
				new Attribute("autoIncludeForCompatibility", ValueType.BOOLEAN, "false"),
				new Attribute("coverImageID", ValueType.TYPE_IMAGE, ""),
				new Attribute("credits", ValueType.STRING, ""),
				new Attribute("debugOnly", ValueType.BOOLEAN, "false"),
				new Attribute("extends", ValueType.TYPE_MOD, ""),
				new Attribute("extensionAPIVersion", ValueType.INTEGER, ""),
				new Attribute("hidden", ValueType.BOOLEAN, "false"),
				new Attribute("name", ValueType.STRING, ""),
				new Attribute("private", ValueType.BOOLEAN, "false"),
				new Attribute("release", ValueType.INTEGER, "1"),
				new Attribute("UNID", ValueType.UNID, ""),
				new Attribute("usesXML", ValueType.BOOLEAN, "false"),
				new Attribute("version", ValueType.STRING, "1.0")
				
				);
	}

}
