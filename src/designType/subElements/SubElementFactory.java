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
import xml.Element;
import xml.Attribute.ValueType;

public class SubElementFactory {
	public static enum MiscElements implements SubElementType {
		Data,
		;
		public Element create() {
			Element e = new Element(name());
			e.addAttributes(new Attribute("id", ValueType.STRING));
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
		public Element create() {
			Element e = new Element(name());
			// TODO Auto-generated method stub
			switch(this) {
			case AcceptDonation:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("actualPrice", ValueType.BOOLEAN)
						);
				break;
			case Buy:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ)
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
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("upgradeInstallOnly", ValueType.BOOLEAN),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case Refuel:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case RemoveDevice:
				
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case RepairArmor:
				
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case RepairItem:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case ReplaceArmor:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("messageID", ValueType.STRING)
						);
				break;
			case Sell:
				e.addAttributes(
						new Attribute("criteria", ValueType.STRING),
						new Attribute("inventoryAdj", ValueType.WHOLE),
						new Attribute("levelFrequency", ValueType.LEVEL_FREQUENCY),
						new Attribute("priceAdj", ValueType.PRICE_ADJ),
						new Attribute("noDescription", ValueType.BOOLEAN)
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
			public Element create() {
				// TODO Auto-generated method stub
				return new Element(name());
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
		public Element create() {
			// TODO Auto-generated method stub
			Element e = new Element(this.name());
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
	public static enum SovereignElements implements SubElementType {
		//Relationships,
			Relationship,
			
		;

		@Override
		public Element create() {
			// TODO Auto-generated method stub
			Element e = new Element(name());
			switch(this) {
			case Relationship:
				e.addAttributes(
						new Attribute("sovereign", ValueType.TYPE_SOVEREIGN),
						new Attribute("disposition", ValueType.DISPOSITION),
						new Attribute("mutual", ValueType.BOOLEAN)
						);
				break;
			}
			return e;
		}
		
	}
	public static enum ExtensionElements implements SubElementType {
		Module, Library;

		@Override
		public Element create() {
			Element e = new Element(name());
			switch(this) {
			case Library:
				e.addAttributes(new Attribute("unid", ValueType.TYPE_MOD));
				break;
			case Module:
				e.addAttributes(new Attribute("filename", ValueType.STRING));
				break;
			}
			return e;
		}
	}
	public static enum SpaceEnvironmentElements implements SubElementType {
		Image,
		EdgeMask,;
		public Element create() {
			Element e = new Element(this.name());
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
				new Attribute("imageID", ValueType.TYPE_IMAGE),
				new Attribute("imageX", ValueType.WHOLE),
				new Attribute("imageY", ValueType.WHOLE),
				new Attribute("imageWidth", ValueType.WHOLE),
				new Attribute("imageHeight", ValueType.WHOLE),
				new Attribute("imageFrameCount", ValueType.WHOLE),
				new Attribute("rotationCount", ValueType.WHOLE),
				new Attribute("rotationColumns", ValueType.WHOLE),
				new Attribute("animationColumns", ValueType.WHOLE),
				new Attribute("imageTicksPerFrame", ValueType.WHOLE),
				new Attribute("flashTicks", ValueType.WHOLE),
				new Attribute("blending", ValueType.BLENDING),
				new Attribute("viewportRatio", ValueType.DECIMAL),
				new Attribute("viewportSize", ValueType.INTEGER),
				new Attribute("rotationOffset", ValueType.INTEGER),
				new Attribute("xOffset", ValueType.INTEGER),
				new Attribute("yOffset", ValueType.INTEGER)
		};
	}
}