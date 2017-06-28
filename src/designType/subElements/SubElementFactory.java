package designType.subElements;
import java.util.LinkedList;

import javax.lang.model.util.Elements;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;

import designType.Types;
import window.Frame;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElement;
import xml.RenameableElement;
import xml.DesignAttribute.ValueType;
import static xml.DesignAttribute.ValueType.*;
import static xml.DesignAttribute.*;
public class SubElementFactory {
	public enum DockScreensElements implements SubElementType {
		DockScreen_Named, Pane_Named, Action;
		
		@Override
		public DesignElement create() {
			DesignElement e = null;
			switch(this) {
			case DockScreen_Named:
				e = new RenameableElement("DockScreen");
				e.addOptionalSingleSubElements(SingleSubElementFactory.createSingleSubElementsForType(Types.DockScreen));
				break;
			case Pane_Named:
				e = new RenameableElement("Pane");
				e.addAttributes(
						att("layout", LAYOUT),
						att("desc", STRING),
						att("noListNavigation", BOOLEAN),
						att("showCounter", BOOLEAN),
						att("showTextInput", BOOLEAN)
						);
				DesignElement controls = new DesignElement("Controls");
				for(String name : new String[] {"Counter", "ItemDisplay", "ItemListDisplay", "Text", "TextInput"}) {
					DesignElement subelement = new DesignElement(name);
					subelement.addAttributes(
							att("id", STRING),
							att("style", STYLE_CONTROLS)
							);
					controls.addOptionalSingleSubElements(subelement);
				}
				DesignElement actions = new DesignElement("Actions");
				actions.addOptionalMultipleSubElements(DockScreensElements.Action);
				e.addOptionalSingleSubElements(
						new DesignElement("OnPaneInit"),
						new DesignElement("Initialize"),
						controls,
						actions
						);
				break;
			case Action:
				e = new DesignElement("Action");
				e.addAttributes(
						att("name", STRING),
						att("id", STRING),
						att("descID", STRING),
						att("prevKey", BOOLEAN),
						att("nextKey", BOOLEAN),
						att("default", BOOLEAN),
						att("cancel", BOOLEAN),
						att("key", CHARACTER),
						att("minor", BOOLEAN)
						
						);
				DesignElement navigate = new DesignElement("Navigate");
				navigate.addAttributes(att("screen", STRING));
				DesignElement showPane = new DesignElement("ShowPane");
				showPane.addAttributes(att("pane", STRING));
				e.addOptionalSingleSubElements(
						navigate,
						showPane,
						new DesignElement("Exit")
						);
				break;
			}
			return e;
		}
	}
	public enum DisplayAttributesElements implements SubElementType {
		ItemAttribute;

		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			switch(this) {
			case ItemAttribute:
				e.addAttributes(
						att("label", STRING),
						att("criteria", STRING),
						att("labelType", LABEL_TYPE)
						);
				break;
			};
			return e;
		}
		
	}
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
				result.addAttributes(att("unid", TYPE_STATION));
				break;
			case ArmorDamageAdj:
			case ShieldDamageAdj:
				result.addAttributes(
						att("level", WHOLE),
						att("damageAdj", STRING)
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
						att("left", INTEGER),
						att("top", INTEGER),
						att("width", INTEGER),
						att("height", INTEGER),
						att("center", INTEGER),
						att("vcenter", INTEGER)
						);
				break;
			case Text:
				e.addAttributes(
						att("id", STRING),
						att("left", INTEGER),
						att("right", INTEGER),
						att("top", INTEGER),
						att("bottom", INTEGER),
						att("font", FONT),
						att("color", HEX_COLOR),
						att("align", ALIGN_HORIZONTAL)
						);
				break;
			case Image:
				e.addAttributes(
						att("left", INTEGER),
						att("right", INTEGER),
						att("top", INTEGER),
						att("bottom", INTEGER),
						att("align", ALIGN_HORIZONTAL),
						att("valign", ALIGN_VERTICAL),
						att("transparent", BOOLEAN)
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
						att("chance", WHOLE),						//Table, LevelTable, Group, Components, Items, AverageValue, LocationCriteria
						att("count", WHOLE),						//Table, LevelTable, Group, Components, Items, AverageValue, LocationCriteria
						att("criteria", STRING),					//LocationCriteria
						att("levelFrequency", LEVEL_FREQUENCY)	//LevelTable
						);
			}
			switch(this) {
			case Item:
				e.addAttributes(
						att("item", ValueType.TYPE_ITEM),
						att("damaged", WHOLE),
						att("debugOnly", BOOLEAN),
						
						att("enhanced", WHOLE),
						att("enhancement", STRING)
						);
				break;
			case Table:
				e.addOptionalMultipleSubElements(
						ItemGeneratorElements.values()
						);
				break;
			case RandomItem:
				e.addAttributes(
						att("criteria", STRING),
						att("attributes", STRING),
						att("modifiers", STRING),
						att("categories", STRING),
						att("levelFrequency", LEVEL_FREQUENCY),
						att("level", WHOLE),
						att("levelCurve", WHOLE),
						att("damaged", WHOLE),
						
						att("enhanced", WHOLE),
						att("enhancement", STRING)
						);
				break;
			case Group:
			case Components:
			case Items:
			case AverageValue:
				e.addAttributes(
						att("levelValue", ValueType.LEVEL_VALUE),
						att("value", WHOLE)
						);
				e.addOptionalMultipleSubElements(ItemGeneratorElements.values());
				break;
			case Lookup:
				e.addAttributes(att("table", ValueType.TYPE_ITEM_TABLE));
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
	public static enum EffectElements implements SubElementType {
		;

		@Override
		public DesignElement create() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public static enum MiscElements implements SubElementType {
		Data,
		;
		public DesignElement create() {
			DesignElement e = new RenameableElement(name());
			e.addAttributes(att("id", STRING));
			e.addAttributes(att("data", STRING));
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
					att("actualPrice", BOOLEAN),
					att("criteria", STRING),
					att("inventoryAdj", WHOLE),
					att("messageID", STRING),
					att("priceAdj", PRICE_ADJ),					
					att("noDescription", BOOLEAN),
					att("upgradeInstallOnly", BOOLEAN),
					att("levelFrequency", LEVEL_FREQUENCY)
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
						att("sovereign", TYPE_SOVEREIGN),
						att("disposition", DISPOSITION),
						att("mutual", BOOLEAN)
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
			case EncounterGroup:
				break;
			case EncounterType:
				break;
			case Encounter:
				e.addAttributes(
						att("enemyExclusionRadius", WHOLE),
						att("exclusionRadius", WHOLE),
						att("levelFrequency", LEVEL_FREQUENCY),
						att("locationCriteria", STRING),
						att("maxAppearing", WHOLE),
						att("minAppearing", WHOLE),
						att("systemCriteria", STRING),
						att("unique", UNIQUE)
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
				e.addAttributes(att("criteria", STRING));
				break;
			case Chance:
				e.addAttributes(att("chance", WHOLE));
				break;
			case DistanceBetweenNodes:
				e.addAttributes(
						att("min", WHOLE),
						att("max", WHOLE)
						);
				break;
			case DistanceTo:
				e.addAttributes(
						att("criteria", STRING),
						att("nodeID", STRING),
						att("min", WHOLE),
						att("max", WHOLE)
						);
				break;
			case StargateCount:
				e.addAttributes(
						att("min", WHOLE),
						att("max", WHOLE)
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
				e.addAttributes(att("unid", TYPE_MOD));
				break;
			case Module:
				e.addAttributes(att("filename", STRING));
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
	public static DesignAttribute[] createImageAttributes() {
		return new DesignAttribute[] {
				att("imageID", TYPE_IMAGE),
				att("imageX", WHOLE),
				att("imageY", WHOLE),
				att("imageWidth", WHOLE),
				att("imageHeight", WHOLE),
				att("imageFrameCount", WHOLE),
				att("rotationCount", WHOLE),
				att("rotationColumns", WHOLE),
				att("animationColumns", WHOLE),
				att("imageTicksPerFrame", WHOLE),
				att("flashTicks", WHOLE),
				att("blending", BLENDING),
				att("viewportRatio", DECIMAL),
				att("viewportSize", INTEGER),
				att("rotationOffset", INTEGER),
				att("xOffset", INTEGER),
				att("yOffset", INTEGER)
		};
	}
	public static DesignAttribute[] createNameAttributes() {
		return new DesignAttribute[] {
				att("definiteArticle", BOOLEAN),
				att("firstPlural", BOOLEAN),
				att("esPlural", BOOLEAN),
				att("customPlural", BOOLEAN),
				att("secondPlural", BOOLEAN),
				att("reverseArticle", BOOLEAN),
				att("noArticle", BOOLEAN),
				att("personalName", BOOLEAN)
		};
	}
	
	//WIP
	public static DesignElement createEffects() {
		return new DesignElement("Effects");
	}
}