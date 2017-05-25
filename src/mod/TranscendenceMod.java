package mod;

import designType.*;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class TranscendenceMod extends Element {

	public TranscendenceMod() {
		super();
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
		addOptionalSingleSubElements(
				new Globals()
				);
		addOptionalMultipleSubElements(
				new DockScreen(),
				new EffectType(),
				new Image(),
				new ItemTable(),
				new ItemType(),
				new MissionType(),
				new NameGenerator(),
				new OverlayType(),
				new Power(),
				new SpaceEnvironment(),
				new ShipClass(),
				new ShipTable(),
				new Sound(),
				new Sovereign(),
				new StationType(),
				new SystemTable(),
				new SystemType(),
				new SystemMap(),
				new TemplateType(),
				new Library(),
				new Module()
				
				);
	}
}
class Library extends Element {
	public Library() {
		super();
		addRequiredAttributes(new Attribute("unid", ValueType.TYPE_MOD));
	}
}
class Module extends Element {
	public Module() {
		super();
		addRequiredAttributes(new Attribute("filename", ValueType.STRING));
	}
}
