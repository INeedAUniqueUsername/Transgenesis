package designType.subElements;
import java.util.LinkedList;

import javax.lang.model.util.Elements;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;

import window.Frame;
import window.Window;
import xml.Attribute;
import xml.DesignElement;
import xml.Attribute.ValueType;
import static xml.Attribute.ValueType.*;

public class SubElementFactory {
	public static enum AdventureDescElements implements SubElementType {
		EncounterOverrides,

		ArmorDamageAdj,
		ShieldDamageAdj,
		
		;
		@Override
		public DesignElement create() {
			DesignElement result = new DesignElement(name());
			switch(this) {
			case EncounterOverrides:
				result = StationTypeElements.Encounter.create();
				result.addAttributes(new Attribute("unid", TYPE_STATION));
				break;
			case ArmorDamageAdj:
			case ShieldDamageAdj:
				result.addAttributes(
						new Attribute("level", WHOLE),
						new Attribute("damageAdj", STRING)
						);
				break;
			default:
				break;
			
			}
			return result;
		}
	}
	public static enum DisplayElements implements SubElementType {
		Group,
		Text,
		Image;
		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Group:
				e.addAttributes(
						new Attribute("left", INTEGER),
						new Attribute("top", INTEGER),
						new Attribute("width", INTEGER),
						new Attribute("height", INTEGER),
						new Attribute("center", INTEGER),
						new Attribute("vcenter", INTEGER)
						);
				break;
			case Text:
				e.addAttributes(
						new Attribute("id", STRING),
						new Attribute("left", INTEGER),
						new Attribute("right", INTEGER),
						new Attribute("top", INTEGER),
						new Attribute("bottom", INTEGER),
						new Attribute("font", FONT),
						new Attribute("color", HEX_COLOR),
						new Attribute("align", ALIGN_HORIZONTAL)
						);
				break;
			case Image:
				e.addAttributes(
						new Attribute("left", INTEGER),
						new Attribute("right", INTEGER),
						new Attribute("top", INTEGER),
						new Attribute("bottom", INTEGER),
						new Attribute("align", ALIGN_HORIZONTAL),
						new Attribute("valign", ALIGN_VERTICAL),
						new Attribute("transparent", BOOLEAN)
						);
				break;
			}
			return e;
		}
	}
	public static enum ItemGeneratorElements implements SubElementType {
		Item,
		Table,
		RandomItem,
		Group, Components, Items, AverageValue,
		Lookup,
		LevelTable,
		LocationCriteria,
		Null;

		@Override
		public DesignElement create() {
			// TODO Auto-generated method stub
			DesignElement e = new DesignElement(name());
			if(!this.equals(Null)) {
				e.addAttributes(
						new Attribute("chance", WHOLE),						//Table, LevelTable, Group, Components, Items, AverageValue, LocationCriteria
						new Attribute("count", WHOLE),						//Table, LevelTable, Group, Components, Items, AverageValue, LocationCriteria
						new Attribute("criteria", STRING),					//LocationCriteria
						new Attribute("levelFrequency", LEVEL_FREQUENCY)	//LevelTable
						);
			}
			switch(this) {
			case Item:
				e.addAttributes(
						new Attribute("item", ValueType.TYPE_ITEM),
						new Attribute("damaged", WHOLE),
						new Attribute("debugOnly", BOOLEAN),
						
						new Attribute("enhanced", WHOLE),
						new Attribute("enhancement", STRING)
						);
				break;
			case Table:
				e.addOptionalMultipleSubElements(
						ItemGeneratorElements.values()
						);
				break;
			case RandomItem:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("attributes", STRING),
						new Attribute("modifiers", STRING),
						new Attribute("categories", STRING),
						new Attribute("levelFrequency", LEVEL_FREQUENCY),
						new Attribute("level", WHOLE),
						new Attribute("levelCurve", WHOLE),
						new Attribute("damaged", WHOLE),
						
						new Attribute("enhanced", WHOLE),
						new Attribute("enhancement", STRING)
						);
				break;
			case Group:
			case Components:
			case Items:
			case AverageValue:
				e.addAttributes(
						new Attribute("levelValue", ValueType.LEVEL_VALUE),
						new Attribute("value", WHOLE)
						);
				e.addOptionalMultipleSubElements(ItemGeneratorElements.values());
				break;
			case Lookup:
				e.addAttributes(new Attribute("table", ValueType.TYPE_ITEM_TABLE));
				break;
			case LevelTable:
				e.addOptionalMultipleSubElements(ItemGeneratorElements.values());
				break;
			case LocationCriteria:
				e.addOptionalMultipleSubElements(ItemGeneratorElements.values());
				break;
			}
			
			return e;
		}
	}
	public static enum MiscElements implements SubElementType {
		Data,
		;
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			e.addAttributes(new Attribute("id", STRING));
			return e;
		}
	}
	public static enum TradeElements implements SubElementType {
		AcceptDonation,
		Buy,
		Sell,
		Refuel,
		RepairArmor,
		ReplaceArmor,
		InstallDevice,
		UpgradeDevice,
		RemoveDevice,
		EnhanceItem,
		RepairItem,
		BuyShip,
		SellShip,
		Custom,;

		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			e.addAttributes(
					new Attribute("actualPrice", BOOLEAN),
					new Attribute("criteria", STRING),
					new Attribute("inventoryAdj", WHOLE),
					new Attribute("messageID", STRING),
					new Attribute("priceAdj", PRICE_ADJ),					
					new Attribute("noDescription", BOOLEAN),
					new Attribute("upgradeInstallOnly", BOOLEAN),
					new Attribute("levelFrequency", LEVEL_FREQUENCY)
					);
			//All trade elements have the same attributes
			return e;
		}
	}
	public static enum SovereignElements implements SubElementType {
		//Relationships,
			Relationship,
			
		;

		@Override
		public DesignElement create() {
			// TODO Auto-generated method stub
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Relationship:
				e.addAttributes(
						new Attribute("sovereign", TYPE_SOVEREIGN),
						new Attribute("disposition", DISPOSITION),
						new Attribute("mutual", BOOLEAN)
						);
				break;
			}
			return e;
		}
		
	}
	public static enum StationTypeElements implements SubElementType {
		Animations,
		Communications,
		ImageComposite,
		Construction,
		Devices,
		
		DockScreens,
		EncounterGroup,
		EncounterType,
		Encounter,
		Encounters,
		//Events,
		HeroImage,
		Image,
		ImageEffect,
		ImageLookup,
		ImageVariants,
		//Items,
		Names,
		Reinforcements,
		Satellites,
		Ships,
			Ship,
		Station,
		Table,
		;

		@Override
		public DesignElement create() {
			// TODO Auto-generated method stub
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Animations:
				break;
			case Communications:
				break;
			case Construction:
				break;
			case Devices:
				break;
			case DockScreens:
				break;
			case EncounterGroup:
				break;
			case EncounterType:
				break;
			case Encounter:
				e.addAttributes(
						new Attribute("enemyExclusionRadius", WHOLE),
						new Attribute("exclusionRadius", WHOLE),
						new Attribute("levelFrequency", LEVEL_FREQUENCY),
						new Attribute("locationCriteria", STRING),
						new Attribute("maxAppearing", WHOLE),
						new Attribute("minAppearing", WHOLE),
						new Attribute("systemCriteria", STRING),
						new Attribute("unique", UNIQUE)
						);
				DesignElement criteria = new DesignElement("Criteria");
				criteria.addOptionalMultipleSubElements(
						SystemCriteria.values()
						);
				e.addOptionalSingleSubElements(
						criteria
						);
				break;
			case Encounters:
				break;
			//case Events:				break;
			//case HeroImage:			break;
			//case Image:				break;
			case ImageComposite:
				break;
			case ImageEffect:
				break;
			case ImageLookup:
				break;
			case ImageVariants:
				break;
			//case Items:				break;
			//case Names:				break;
			case Reinforcements:
				break;
			case Satellites:
				break;
			case Ship:
				break;
			case Ships:
				break;
			case Station:
				break;
			case Table:
				break;
			default:
				break;
			
			}
			return e;
		}
	}
	public static enum SystemCriteria implements SubElementType {
		Attributes,
		Chance,
		DistanceBetweenNodes,
		DistanceTo,
		StargateCount,;

		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Attributes:
				e.addAttributes(new Attribute("criteria", STRING));
				break;
			case Chance:
				e.addAttributes(new Attribute("chance", WHOLE));
				break;
			case DistanceBetweenNodes:
				e.addAttributes(
						new Attribute("min", WHOLE),
						new Attribute("max", WHOLE)
						);
				break;
			case DistanceTo:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("nodeID", STRING),
						new Attribute("min", WHOLE),
						new Attribute("max", WHOLE)
						);
				break;
			case StargateCount:
				e.addAttributes(
						new Attribute("min", WHOLE),
						new Attribute("max", WHOLE)
						);
				break;
			default:
				break;
			}
			return e;
		}
	}
	public static enum ExtensionElements implements SubElementType {
		Module, Library;

		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Library:
				e.addAttributes(new Attribute("unid", TYPE_MOD));
				break;
			case Module:
				e.addAttributes(new Attribute("filename", STRING));
				break;
			}
			return e;
		}
	}
	public static enum SpaceEnvironmentElements implements SubElementType {
		Image,
		EdgeMask,;
		public DesignElement create() {
			DesignElement e = new DesignElement(this.name());
			e.addAttributes(createImageAttributes());
			return e;
		}
	}
	
	/*
	switch(s) {
	
		
	case Relationship:
		e = new Element("Relationship");
		
		break;
	case EdgeMask:
		e = new Element("EdgeMask");
		initializeImage(e);
		break;
	case Image:
		e = new Element("Image");
		initializeImage(e);
		break;
	default:
		System.out.println("Not supported: " + s.toString());
		/*
		try {
			throw new Exception("Not supported: " + s.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		*//*
		e = new Element(s.name());
		break;
	}
	*/
	public static Attribute[] createImageAttributes() {
		return new Attribute[] {
				new Attribute("imageID", TYPE_IMAGE),
				new Attribute("imageX", WHOLE),
				new Attribute("imageY", WHOLE),
				new Attribute("imageWidth", WHOLE),
				new Attribute("imageHeight", WHOLE),
				new Attribute("imageFrameCount", WHOLE),
				new Attribute("rotationCount", WHOLE),
				new Attribute("rotationColumns", WHOLE),
				new Attribute("animationColumns", WHOLE),
				new Attribute("imageTicksPerFrame", WHOLE),
				new Attribute("flashTicks", WHOLE),
				new Attribute("blending", BLENDING),
				new Attribute("viewportRatio", DECIMAL),
				new Attribute("viewportSize", INTEGER),
				new Attribute("rotationOffset", INTEGER),
				new Attribute("xOffset", INTEGER),
				new Attribute("yOffset", INTEGER)
		};
	}
	public static Attribute[] createNameAttributes() {
		return new Attribute[] {
				new Attribute("definiteArticle", BOOLEAN),
				new Attribute("firstPlural", BOOLEAN),
				new Attribute("esPlural", BOOLEAN),
				new Attribute("customPlural", BOOLEAN),
				new Attribute("secondPlural", BOOLEAN),
				new Attribute("reverseArticle", BOOLEAN),
				new Attribute("noArticle", BOOLEAN),
				new Attribute("personalName", BOOLEAN)
		};
	}
}