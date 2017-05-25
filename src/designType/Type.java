package designType;

import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class Type extends Element implements Comparable {

	public Type() {
		super();
		initializeType();
	}
	public Type(String name) {
		super(name);
		initializeType();
	}
	private void initializeType() {
		Attribute unid = new Attribute("UNID", ValueType.UNID, "");
		addRequiredAttributes(unid);
		addAttributes(
				new Attribute("attributes", ValueType.STRING, ""),
				new Attribute("inherit", ValueType.TYPE_INHERITED, "")
				);
		addOptionalSingleSubElements(
				new Element("Language"),
				new Events()
				);
	}
	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof Type) {
			int unid = Integer.valueOf(getAttributeByName("UNID").getValue());
			int unid_other = Integer.valueOf(((Type) arg0).getAttributeByName("UNID").getValue());
			if(unid > unid_other) {
				return 1;
			} else if(unid < unid_other) {
				return -1;
			}
		}
		return 0;
	}
}
class Events extends Element {
	public Events() {
		super();
		addOptionalSingleSubElements(
				"GetGlobalAchievements",
				"GetGlobalDockScreen",
				"GetGlobalPlayerPriceAdj",
				"OnGlobalPaneInit",
				"OnGlobalMarkImages",

				"OnGlobalObjDestroyed",
				"OnGlobalPlayerBoughtItem",
				"OnGlobalPlayerSoldItem",
				"OnGlobalSystemStarted",
				"OnGlobalSystemStopped",

				"OnGlobalUniverseCreated",
				"OnGlobalUniverseLoad",
				"OnGlobalUniverseSave",
				"OnGlobalUpdate"
				);
	}
}
