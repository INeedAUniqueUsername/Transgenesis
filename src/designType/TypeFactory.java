package designType;

import designType.subElements.SubElement;
import designType.subElements.SubElementFactory;
import designType.subElements.SubElementFactory.SubElements;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;
public final class TypeFactory {
	//@Override
	/*
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
	*/
	public static enum Types implements SubElement {
		AdventureDesc,
		DockScreen,
		EconomyType,
		EffectType,
		Image,
		ItemTable,
		ItemType,
		MissionType,
		NameGenerator,
		OverlayType,
		Power,
		ShipClass,
		ShipTable,
		Sound,
		Sovereign,
		SpaceEnvironment,
		StationType,
		SystemMap,
		SystemTable,
		SystemType,
		TemplateType,
		Type;

		@Override
		public Element create() {
			return createDesignType(this);
		}
	}
	public static Element createDesignType(Types t) {
		Element e = new Element(t.name());
		switch(t) {
		case AdventureDesc:
			e.addRequiredAttributes(
					new Attribute("adventureUNID", ValueType.UNID),
					new Attribute("backgroundID", ValueType.TYPE_IMAGE),
					new Attribute("desc", ValueType.STRING),
					new Attribute("include10StartingShips", ValueType.BOOLEAN),
					new Attribute("level", ValueType.INTEGER),
					new Attribute("name", ValueType.STRING),
					new Attribute("startingMap", ValueType.TYPE_SYSTEM_MAP),
					new Attribute("startingPos", ValueType.STRING),
					new Attribute("startingShipCriteria", ValueType.STRING),
					new Attribute("startingSystem", ValueType.STRING),
					new Attribute("welcomeMessage", ValueType.STRING)
					);
			break;
		case DockScreen:
			break;
		case EconomyType:
			break;
		case EffectType:
			break;
		case Image:
			e.addRequiredAttributes(
					new Attribute("bitmap", ValueType.FILENAME),
					new Attribute("bitmask", ValueType.FILENAME)
					);
			e.addAttributes(
					new Attribute("backColor", ValueType.HEX_COLOR),
					new Attribute("hitMask", ValueType.FILENAME),
					new Attribute("loadOnUse", ValueType.BOOLEAN),
					new Attribute("noPM", ValueType.BOOLEAN),
					new Attribute("shadowMask", ValueType.FILENAME),
					new Attribute("sprite", ValueType.BOOLEAN)
					);
			break;
		case ItemTable:
			break;
		case ItemType:
			break;
		case MissionType:
			e.addAttributes(
					new Attribute("allowPlayerDelete", ValueType.BOOLEAN, "false"),
					new Attribute("debriefAfterOutOfSystem", ValueType.BOOLEAN, "false"),
					new Attribute("expireTime", ValueType.INTEGER),
					new Attribute("failureAfterOutOfSystem", ValueType.BOOLEAN, "false"),
					new Attribute("forceUndockAfterDebrief", ValueType.BOOLEAN, "false"),
					new Attribute("level", ValueType.WHOLE),
					new Attribute("maxAppearing", ValueType.WHOLE),
					new Attribute("name", ValueType.STRING),
					new Attribute("noDebrief", ValueType.BOOLEAN, "false"),
					new Attribute("noDecline", ValueType.BOOLEAN, "false"),
					new Attribute("noFailureOnOwnerDestroyed", ValueType.BOOLEAN, "false"),
					new Attribute("noStats", ValueType.BOOLEAN, "false"),
					new Attribute("priority", ValueType.WHOLE, "1")
					);
			break;
		case NameGenerator:
			break;
		case OverlayType:
			e.addAttributes(
					new Attribute("disarm", ValueType.BOOLEAN, "false"),
					new Attribute("paralyze", ValueType.BOOLEAN, "false"),
					new Attribute("spin", ValueType.BOOLEAN, "false"),
					new Attribute("drag", ValueType.INTEGER, "100"),
					new Attribute("absorbAdj", ValueType.INTEGER_SEQUENCE, "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"),
					new Attribute("weaponSuppress", ValueType.STRING, "")
					);
			break;
		case Power:
			e.addAttributes(
					new Attribute("name", ValueType.STRING, ""),
					new Attribute("key", ValueType.CHARACTER, "")
					);
			e.addOptionalSingleSubElements(
					new Element("OnShow"),
					new Element("OnInvokedByPlayer"),
					new Element("OnInvoke"),
					new Element("OnDestroyCheck")
					);
			break;
		case ShipClass:
			break;
		case ShipTable:
			break;
		case Sound:
			e.addRequiredAttributes(new Attribute("filename", ValueType.FILENAME));
			break;
		case Sovereign:
			e.addRequiredAttributes(
					new Attribute("name", ValueType.STRING),
					new Attribute("shortName", ValueType.STRING),
					new Attribute("adjective", ValueType.STRING),
					new Attribute("demonym", ValueType.STRING),
					new Attribute("plural", ValueType.STRING),
					new Attribute("alignment", ValueType.ALIGNMENT)
					);
			e.addOptionalMultipleSubElements(
					SubElements.Relationships
					);
			break;
		case SpaceEnvironment:
			e.addAttributes(
					new Attribute("autoEdges", ValueType.BOOLEAN),
					new Attribute("dragFactor", ValueType.WHOLE),
					new Attribute("lrsJammer", ValueType.BOOLEAN, "false"),
					new Attribute("mapColor", ValueType.HEX_COLOR),
					new Attribute("opacity", ValueType.WHOLE),
					new Attribute("shieldJammer", ValueType.BOOLEAN, "false"),
					new Attribute("srsJammer", ValueType.BOOLEAN, "false")
					);
			e.addRequiredSingleSubElements(SubElementFactory.createSubElement(SubElements.Image));
			e.addOptionalSingleSubElements(SubElementFactory.createSubElement(SubElements.EdgeMask));
			break;
		case StationType:
			break;
		case SystemMap:
			break;
		case SystemTable:
			break;
		case SystemType:
			break;
		case TemplateType:
			break;
		case Type:
			break;
		default:
			try {
				throw new Exception("Wow, way to ruin the moment.");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
		
		
		
		e.addRequiredAttributes(new Attribute("UNID", ValueType.UNID, ""));
		e.addAttributes(
				new Attribute("attributes", ValueType.STRING, ""),
				new Attribute("inherit", ValueType.TYPE_INHERITED, "")
				);
		//All DesignTypes can have Language and Events
		e.addOptionalSingleSubElements(
				SubElementFactory.createLanguage(t),
				SubElementFactory.createEvents(t),
				SubElementFactory.createStaticData(),
				SubElementFactory.createGlobalData(),
				SubElementFactory.createInitialData()
				);
		
		return e;
	}
}
