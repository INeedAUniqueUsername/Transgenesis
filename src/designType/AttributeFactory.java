package designType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import designType.TypeFactory.Types;
import xml.Attribute;
import xml.Attribute.ValueType;
import xml.DesignElement;
import static xml.Attribute.ValueType.*;

public class AttributeFactory {
	public static Attribute[] createAttributesForType(Types t) {
		Attribute[] result = null;
		switch(t) {
		case AdventureDesc:
			return new Attribute[] {
					new Attribute("adventureUNID", UNID),
					new Attribute("backgroundID", TYPE_IMAGE),
					new Attribute("desc", STRING),
					new Attribute("include10StartingShips", BOOLEAN),
					new Attribute("name", STRING),
					new Attribute("startingMap", TYPE_SYSTEM_MAP),
					new Attribute("startingPos", STRING),
					new Attribute("startingShipCriteria", STRING),
					new Attribute("startingSystem", STRING),
					new Attribute("welcomeMessage", STRING)
			};
		case DockScreen:
			return new Attribute[] {
				new Attribute("unid", UNID),
				new Attribute("name", STRING),
				new Attribute("desc", STRING),
				new Attribute("type", DOCKSCREEN_TYPE),
				new Attribute("inherit", TYPE_INHERITED),
				new Attribute("backgroundID", ValueType.DOCKSCREEN_BACKGROUND_ID),
				new Attribute("nestedScreen", BOOLEAN),
			};
		case EconomyType:
			return new Attribute[] {
					new Attribute("id", STRING),
					new Attribute("currency", STRING),
					new Attribute("conversion", INTEGER)	
			};
		case EffectType:
			break;
		case Image:
			return new Attribute[] {
					new Attribute("backColor", HEX_COLOR),
					new Attribute("bitmap", FILENAME),
					new Attribute("bitmask", FILENAME),
					new Attribute("hitMask", FILENAME),
					new Attribute("loadOnUse", BOOLEAN),
					new Attribute("noPM", BOOLEAN),
					new Attribute("shadowMask", FILENAME),
					new Attribute("sprite", BOOLEAN)
			};
		case ItemTable:
			break;
		case ItemType:
			return new Attribute[] {
					new Attribute("ammoCharges", BOOLEAN),
					new Attribute("charges", WHOLE),
					new Attribute("data", STRING),
					new Attribute("enhancement", STRING),
					new Attribute("frequency", FREQUENCY),
					new Attribute("level", WHOLE),
					new Attribute("massBonusPerCharge", WHOLE),
					new Attribute("noSaleIfUsed", BOOLEAN),
					new Attribute("name", STRING),
					new Attribute("numberAppearing", WHOLE),
					new Attribute("pluralName", STRING),
					new Attribute("reverseArticle", BOOLEAN),
					new Attribute("secondPlural", BOOLEAN),
					new Attribute("showReference", BOOLEAN),
					new Attribute("sortName", STRING),
					new Attribute("UNID", UNID),
					new Attribute("unknownType", TYPE_ITEM),
					new Attribute("useAsArmorSet", BOOLEAN),
					new Attribute("useCompleteArmorOnly", BOOLEAN),
					new Attribute("useEnabledOnly", BOOLEAN),
					new Attribute("useInstalledOnly", BOOLEAN),
					new Attribute("useKey", CHARACTER),
					new Attribute("useScreen", ValueType.TYPE_DOCKSCREEN),
					new Attribute("useUninstalledOnly", BOOLEAN),
					new Attribute("value", WHOLE),
					new Attribute("valueBonusPerCharge", WHOLE),
					new Attribute("valueCharges", BOOLEAN),
					new Attribute("virtual", BOOLEAN),
			};
		case MissionType:
			return new Attribute[] {
					new Attribute("allowPlayerDelete", BOOLEAN, "false"),
					new Attribute("debriefAfterOutOfSystem", BOOLEAN, "false"),
					new Attribute("expireTime", INTEGER),
					new Attribute("failureAfterOutOfSystem", BOOLEAN, "false"),
					new Attribute("forceUndockAfterDebrief", BOOLEAN, "false"),
					new Attribute("level", WHOLE),
					new Attribute("maxAppearing", WHOLE),
					new Attribute("name", STRING),
					new Attribute("noDebrief", BOOLEAN, "false"),
					new Attribute("noDecline", BOOLEAN, "false"),
					new Attribute("noFailureOnOwnerDestroyed", BOOLEAN, "false"),
					new Attribute("noStats", BOOLEAN, "false"),
					new Attribute("priority", WHOLE, "1")
			};
		case NameGenerator:
			break;
		case OverlayType:
			return new Attribute[] {
					new Attribute("disarm", BOOLEAN, "false"),
					new Attribute("paralyze", BOOLEAN, "false"),
					new Attribute("spin", BOOLEAN, "false"),
					new Attribute("drag", INTEGER, "100"),
					new Attribute("absorbAdj", INTEGER_SEQUENCE, "0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0"),
					new Attribute("weaponSuppress", STRING, "")
			};
		case Power:
			return new Attribute[] {
					new Attribute("name", STRING, ""),
					new Attribute("key", CHARACTER, "")	
			};
		case ShipClass:
			break;
		case ShipTable:
			break;
		case Sound:
			return new Attribute[] {
					new Attribute("filename", FILENAME)	
			};
		case Sovereign:
			return new Attribute[] {
					new Attribute("name", STRING),
					new Attribute("shortName", STRING),
					new Attribute("adjective", STRING),
					new Attribute("demonym", STRING),
					new Attribute("plural", STRING),
					new Attribute("alignment", ALIGNMENT)
			};
		case SpaceEnvironmentType:
			return new Attribute[] {
					new Attribute("autoEdges", BOOLEAN),
					new Attribute("dragFactor", WHOLE),
					new Attribute("lrsJammer", BOOLEAN, "false"),
					new Attribute("mapColor", HEX_COLOR),
					new Attribute("opacity", WHOLE),
					new Attribute("shieldJammer", BOOLEAN, "false"),
					new Attribute("srsJammer", BOOLEAN, "false")
			};
		case StationType:
			return new Attribute[] {
					new Attribute("abandonedScreen", STRING),
					new Attribute("alertWhenAttacked", STRING),
					new Attribute("alertWhenDestroyed", STRING),
					new Attribute("allowEnemyDocking", STRING),
					new Attribute("armorID", STRING),
					new Attribute("backgroundPlane", STRING),
					new Attribute("barrierEffect", STRING),
					new Attribute("beacon", STRING),
					
					new Attribute("canAttack", BOOLEAN),
					new Attribute("chance", STRING),
					new Attribute("constructionRate", INTEGER),
					new Attribute("controllingSovereign", TYPE_SOVEREIGN),
					new Attribute("count", INTEGER),
					new Attribute("defaultBackgroundID", STRING),
					new Attribute("destEntryPoint	", STRING),
					new Attribute("destNodeID", STRING),
					new Attribute("destroyWhenEmpty", BOOLEAN),
					new Attribute("dockScreen", STRING),
					new Attribute("dockingPorts", INTEGER),
					new Attribute("enemyExclusionRadius", INTEGER),
					new Attribute("ejactaAdj", INTEGER),
					new Attribute("ejectaType", TYPE_ANY),
					new Attribute("explosionType", TYPE_ANY),
					new Attribute("fireRateAdj", INTEGER),
					new Attribute("frequency", FREQUENCY),
					new Attribute("gateEffect", TYPE_ANY),
					new Attribute("gravityRadius", INTEGER),
					new Attribute("hitPoints", INTEGER),
					new Attribute("immutable", BOOLEAN),
					new Attribute("inactive", BOOLEAN),
					new Attribute("level", INTEGER),
					new Attribute("levelFrequency", LEVEL_FREQUENCY),
					new Attribute("locationCriteria", STRING),
					new Attribute("mass", INTEGER),
					
					new Attribute("maxHitPoints", INTEGER),
					new Attribute("maxLightRadius", INTEGER),
					new Attribute("maxStructuralHitPoints", INTEGER),
					new Attribute("minShips", INTEGER),
					new Attribute("mobile", BOOLEAN),
					new Attribute("multiHull", BOOLEAN),
					new Attribute("name", STRING),
					new Attribute("noBlacklist", BOOLEAN),
					new Attribute("noFriendlyFire", BOOLEAN),
					new Attribute("noFriendlyTarget", BOOLEAN),
					new Attribute("noMapDetails", BOOLEAN),
					new Attribute("noMapIcon", BOOLEAN),
					new Attribute("noMapLabel", BOOLEAN),
					new Attribute("paintLayer", STRING),
					new Attribute("radioactive", BOOLEAN),
					new Attribute("randomEncounters", FREQUENCY),
					new Attribute("regen", INTEGER),
					new Attribute("repairRate", INTEGER),
					new Attribute("reverseArticle", BOOLEAN),
					new Attribute("scale", SCALE),
					new Attribute("shipEncounter", BOOLEAN),
					new Attribute("shipRegen", INTEGER),
					new Attribute("shipRepairRate", INTEGER),
					new Attribute("sign", BOOLEAN),
					new Attribute("size", INTEGER),
					new Attribute("sovereign", TYPE_SOVEREIGN),
					new Attribute("spaceColor", STRING),
					new Attribute("stealth", INTEGER),
					new Attribute("structuralHitPoints", STRING),
					new Attribute("timeStopImmune", BOOLEAN),
					new Attribute("UNID", UNID),
					new Attribute("unique", UNIQUE),
					new Attribute("virtual", BOOLEAN),
					new Attribute("barrier", BOOLEAN),
					new Attribute("x", INTEGER),
					new Attribute("y", INTEGER),

					new Attribute("largeDamageImageID", TYPE_IMAGE),
					new Attribute("largeDamageWidth", INTEGER),
					new Attribute("largeDamageHeight", INTEGER),
					new Attribute("largeDamageCount", INTEGER),
					new Attribute("mediumDamageImageID", TYPE_IMAGE),
					new Attribute("mediumDamageWidth", INTEGER),
					new Attribute("mediumDamageHeight", INTEGER),
					new Attribute("mediumDamageCount", INTEGER)	
			};
		case SystemMap:
			break;
		case SystemTable:
			break;
		case SystemType:
			return new Attribute[] {
					new Attribute("backgroundID", TYPE_IMAGE),
					new Attribute("noExtraEncounters", BOOLEAN),
					new Attribute("noRandomEncounters", BOOLEAN),
					new Attribute("spaceScale", INTEGER),
					new Attribute("spaceEnvironmentTileSize", TILE_SIZE),
					new Attribute("timeScale", INTEGER)
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
