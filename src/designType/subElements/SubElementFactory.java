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
			// TODO Auto-generated method stub
			switch(this) {
			case AcceptDonation:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("actualPrice", BOOLEAN)
						);
				break;
			case Buy:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ)
						);
				break;
			case BuyShip:
				break;
			case Custom:
				break;
			case EnhanceItem:
				break;
			case InstallDevice:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("upgradeInstallOnly", BOOLEAN),
						new Attribute("messageID", STRING)
						);
				break;
			case Refuel:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("messageID", STRING)
						);
				break;
			case RemoveDevice:
				
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("messageID", STRING)
						);
				break;
			case RepairArmor:
				
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("messageID", STRING)
						);
				break;
			case RepairItem:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("messageID", STRING)
						);
				break;
			case ReplaceArmor:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("messageID", STRING)
						);
				break;
			case Sell:
				e.addAttributes(
						new Attribute("criteria", STRING),
						new Attribute("inventoryAdj", WHOLE),
						new Attribute("levelFrequency", LEVEL_FREQUENCY),
						new Attribute("priceAdj", PRICE_ADJ),
						new Attribute("noDescription", BOOLEAN)
						);
				break;
			case SellShip:
				break;
			case UpgradeDevice:
				break;
			default:
				break;
			
			}
			return e;
		}
	}
	public static enum SpaceObjectElements implements SubElementType {
		DockingPorts,
		;

			@Override
			public DesignElement create() {
				// TODO Auto-generated method stub
				return new DesignElement(name());
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
		Events,
		HeroImage,
		Image,
		ImageEffect,
		ImageLookup,
		ImageVariants,
		Items,
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
			case Events:
				break;
			case HeroImage:
				e.addAttributes(createImageAttributes());
				break;
			case Image:
				
				break;
			case ImageComposite:
				break;
			case ImageEffect:
				break;
			case ImageLookup:
				break;
			case ImageVariants:
				break;
			case Items:
				break;
			case Names:
				break;
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
}