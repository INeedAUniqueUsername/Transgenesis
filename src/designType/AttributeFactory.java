package designType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import designType.TypeFactory.Types;
import xml.Attribute;
import xml.Attribute.ValueType;
import xml.Element;

public class AttributeFactory {
	public static Attribute[] createAttributesForType(Types t) {
		Attribute[] result = null;
		switch(t) {
		case AdventureDesc:
			return new Attribute[] {
					new Attribute("adventureUNID", ValueType.UNID),
					new Attribute("backgroundID", ValueType.TYPE_IMAGE),
					new Attribute("desc", ValueType.STRING),
					new Attribute("include10StartingShips", ValueType.BOOLEAN),
					new Attribute("name", ValueType.STRING),
					new Attribute("startingMap", ValueType.TYPE_SYSTEM_MAP),
					new Attribute("startingPos", ValueType.STRING),
					new Attribute("startingShipCriteria", ValueType.STRING),
					new Attribute("startingSystem", ValueType.STRING),
					new Attribute("welcomeMessage", ValueType.STRING)
			};
		case DockScreen:
			break;
		case EconomyType:
			return new Attribute[] {
					new Attribute("id", ValueType.STRING),
					new Attribute("currency", ValueType.STRING),
					new Attribute("conversion", ValueType.INTEGER)	
			};
		case EffectType:
			break;
		case Image:
			return new Attribute[] {
					new Attribute("backColor", ValueType.HEX_COLOR),
					new Attribute("bitmap", ValueType.FILENAME),
					new Attribute("bitmask", ValueType.FILENAME),
					new Attribute("hitMask", ValueType.FILENAME),
					new Attribute("loadOnUse", ValueType.BOOLEAN),
					new Attribute("noPM", ValueType.BOOLEAN),
					new Attribute("shadowMask", ValueType.FILENAME),
					new Attribute("sprite", ValueType.BOOLEAN)
			};
		case ItemTable:
			break;
		case ItemType:
			break;
		case MissionType:
			return new Attribute[] {
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
			};
		case NameGenerator:
			break;
		case OverlayType:
			return new Attribute[] {
					new Attribute("disarm", ValueType.BOOLEAN, "false"),
					new Attribute("paralyze", ValueType.BOOLEAN, "false"),
					new Attribute("spin", ValueType.BOOLEAN, "false"),
					new Attribute("drag", ValueType.INTEGER, "100"),
					new Attribute("absorbAdj", ValueType.INTEGER_SEQUENCE, "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"),
					new Attribute("weaponSuppress", ValueType.STRING, "")
			};
		case Power:
			return new Attribute[] {
					new Attribute("name", ValueType.STRING, ""),
					new Attribute("key", ValueType.CHARACTER, "")	
			};
		case ShipClass:
			break;
		case ShipTable:
			break;
		case Sound:
			return new Attribute[] {
					new Attribute("filename", ValueType.FILENAME)	
			};
		case Sovereign:
			return new Attribute[] {
					new Attribute("name", ValueType.STRING),
					new Attribute("shortName", ValueType.STRING),
					new Attribute("adjective", ValueType.STRING),
					new Attribute("demonym", ValueType.STRING),
					new Attribute("plural", ValueType.STRING),
					new Attribute("alignment", ValueType.ALIGNMENT)
			};
		case SpaceEnvironmentType:
			return new Attribute[] {
					new Attribute("autoEdges", ValueType.BOOLEAN),
					new Attribute("dragFactor", ValueType.WHOLE),
					new Attribute("lrsJammer", ValueType.BOOLEAN, "false"),
					new Attribute("mapColor", ValueType.HEX_COLOR),
					new Attribute("opacity", ValueType.WHOLE),
					new Attribute("shieldJammer", ValueType.BOOLEAN, "false"),
					new Attribute("srsJammer", ValueType.BOOLEAN, "false")
			};
		case StationType:
			return new Attribute[] {
					new Attribute("abandonedScreen", ValueType.STRING),
					new Attribute("alertWhenAttacked", ValueType.STRING),
					new Attribute("alertWhenDestroyed", ValueType.STRING),
					new Attribute("allowEnemyDocking", ValueType.STRING),
					new Attribute("armorID", ValueType.STRING),
					new Attribute("backgroundPlane", ValueType.STRING),
					new Attribute("barrierEffect", ValueType.STRING),
					new Attribute("beacon", ValueType.STRING),
					
					new Attribute("canAttack", ValueType.BOOLEAN),
					new Attribute("chance", ValueType.STRING),
					new Attribute("constructionRate", ValueType.INTEGER),
					new Attribute("controllingSovereign", ValueType.TYPE_SOVEREIGN),
					new Attribute("count", ValueType.INTEGER),
					new Attribute("defaultBackgroundID", ValueType.STRING),
					new Attribute("destEntryPoint	", ValueType.STRING),
					new Attribute("destNodeID", ValueType.STRING),
					new Attribute("destroyWhenEmpty", ValueType.BOOLEAN),
					new Attribute("dockScreen", ValueType.STRING),
					new Attribute("dockingPorts", ValueType.INTEGER),
					new Attribute("enemyExclusionRadius", ValueType.INTEGER),
					new Attribute("ejactaAdj", ValueType.INTEGER),
					new Attribute("ejectaType", ValueType.TYPE_ANY),
					new Attribute("explosionType", ValueType.TYPE_ANY),
					new Attribute("fireRateAdj", ValueType.INTEGER),
					new Attribute("frequency", ValueType.FREQUENCY),
					new Attribute("gateEffect", ValueType.TYPE_ANY),
					new Attribute("gravityRadius", ValueType.INTEGER),
					new Attribute("hitPoints", ValueType.INTEGER),
					new Attribute("immutable", ValueType.BOOLEAN),
					new Attribute("inactive", ValueType.BOOLEAN),
					new Attribute("level", ValueType.INTEGER),
					new Attribute("levelFrequency", ValueType.LEVEL_FREQUENCY),
					new Attribute("locationCriteria", ValueType.STRING),
					new Attribute("mass", ValueType.INTEGER),
					
					new Attribute("maxHitPoints", ValueType.INTEGER),
					new Attribute("maxLightRadius", ValueType.INTEGER),
					new Attribute("maxStructuralHitPoints", ValueType.INTEGER),
					new Attribute("minShips", ValueType.INTEGER),
					new Attribute("mobile", ValueType.BOOLEAN),
					new Attribute("multiHull", ValueType.BOOLEAN),
					new Attribute("name", ValueType.STRING),
					new Attribute("noBlacklist", ValueType.BOOLEAN),
					new Attribute("noFriendlyFire", ValueType.BOOLEAN),
					new Attribute("noFriendlyTarget", ValueType.BOOLEAN),
					new Attribute("noMapDetails", ValueType.BOOLEAN),
					new Attribute("noMapIcon", ValueType.BOOLEAN),
					new Attribute("noMapLabel", ValueType.BOOLEAN),
					new Attribute("paintLayer", ValueType.STRING),
					new Attribute("radioactive", ValueType.BOOLEAN),
					new Attribute("randomEncounters", ValueType.FREQUENCY),
					new Attribute("regen", ValueType.INTEGER),
					new Attribute("repairRate", ValueType.INTEGER),
					new Attribute("reverseArticle", ValueType.BOOLEAN),
					new Attribute("scale", ValueType.SCALE),
					new Attribute("shipEncounter", ValueType.BOOLEAN),
					new Attribute("shipRegen", ValueType.INTEGER),
					new Attribute("shipRepairRate", ValueType.INTEGER),
					new Attribute("sign", ValueType.BOOLEAN),
					new Attribute("size", ValueType.INTEGER),
					new Attribute("sovereign", ValueType.TYPE_SOVEREIGN),
					new Attribute("spaceColor", ValueType.STRING),
					new Attribute("stealth", ValueType.INTEGER),
					new Attribute("structuralHitPoints", ValueType.STRING),
					new Attribute("timeStopImmune", ValueType.BOOLEAN),
					new Attribute("UNID", ValueType.UNID),
					new Attribute("unique", ValueType.UNIQUE),
					new Attribute("virtual", ValueType.BOOLEAN),
					new Attribute("barrier", ValueType.BOOLEAN),
					new Attribute("x", ValueType.INTEGER),
					new Attribute("y", ValueType.INTEGER),

					new Attribute("largeDamageImageID", ValueType.TYPE_IMAGE),
					new Attribute("largeDamageWidth", ValueType.INTEGER),
					new Attribute("largeDamageHeight", ValueType.INTEGER),
					new Attribute("largeDamageCount", ValueType.INTEGER),
					new Attribute("mediumDamageImageID", ValueType.TYPE_IMAGE),
					new Attribute("mediumDamageWidth", ValueType.INTEGER),
					new Attribute("mediumDamageHeight", ValueType.INTEGER),
					new Attribute("mediumDamageCount", ValueType.INTEGER)	
			};
		case SystemMap:
			break;
		case SystemTable:
			break;
		case SystemType:
			return new Attribute[] {
					new Attribute("backgroundID", ValueType.TYPE_IMAGE),
					new Attribute("noExtraEncounters", ValueType.BOOLEAN),
					new Attribute("noRandomEncounters", ValueType.BOOLEAN),
					new Attribute("spaceScale", ValueType.INTEGER),
					new Attribute("spaceEnvironmentTileSize", ValueType.TILE_SIZE),
					new Attribute("timeScale", ValueType.INTEGER)
			};
		case TemplateType:
			break;
		case Type:
			break;
		default:
		
		}
		return new Attribute[] {};
	}
}
