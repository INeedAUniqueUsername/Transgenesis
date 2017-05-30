package designType.subElements;
import javax.lang.model.util.Elements;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import designType.TypeFactory.Types;
import window.Frame;
import window.Window;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class SubElementFactory {
	public static Element createEvents(Types type) {
		Element e = new Element("Events");
		e.addOptionalSingleSubElements(
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
		switch(type) {
		case AdventureDesc:
			e.addOptionalSingleSubElements(
					new Element("OnGameStart"),
					new Element("OnGameEnd")
					);
			break;
		case DockScreen:
			break;
		case EconomyType:
			e.addRequiredAttributes(
					new Attribute("id", ValueType.STRING),
					new Attribute("currency", ValueType.STRING),
					new Attribute("conversion", ValueType.INTEGER)
					);
			break;
		case EffectType:
			break;
		case Image:
			break;
		case ItemTable:
			break;
		case ItemType:
			break;
		case MissionType:
			e.addOptionalSingleSubElements(
				"OnCreate",
				"OnDestroy",
				"OnAccepted",
				"OnAcceptedUndock",
				"OnDeclined",
				"OnStarted",
				"OnInProgress",
				"OnInProgressUndock",
				"OnSetPlayerTarget",
				"OnCompleted",
				"OnCanDebrief",
				"OnDebriefedUndock",
				"OnGetNextScreen",
				"OnReward"
				);
		case NameGenerator:
			break;
		case OverlayType:
			e.addOptionalSingleSubElements(
					new Event("OnCreate"),
					new Event("OnUpdate"),
					new Event("OnDamage"),
					new Event("OnDestroy"),
					new Event("OnObjDestroyed")
					);
			break;
		case Power:
			break;
		case ShipClass:
		case StationType:
			e.addOptionalSingleSubElements(
					new Event("CanDockAsPlayer"),
					new Event("CanInstallItem"),
					new Event("GetDockScreen"),
					new Event("GetExplosionType"),
					new Event("GetPlayerPriceAdj"),
					new Event("OnAttacked"),
					new Event("OnAttackedByPlayer"),
					new Event("OnCreate"),
					new Event("OnCreateOrders"),
					new Event("OnDamage"),
					new Event("OnDataTransfer"),
					new Event("OnDeselected"),
					new Event("OnDestroy"),
					new Event("OnDockObjAdj"),
					new Event("OnEnteredGate"),
					new Event("OnEnteredSystem"),
					new Event("OnLoad"),
					new Event("OnMining"),
					new Event("OnObjBlacklistedPlayer"),
					new Event("OnObjDestroyed"),
					new Event("OnObjDocked"),
					new Event("OnObjEnteredGate"),
					new Event("OnObjJumped"),
					new Event("OnObjJumpPosAdj"),
					new Event("OnObjReconned"),
					new Event("OnOrderChanged"),
					new Event("OnOrdersCompleted"),
					new Event("OnEventHandlerInit"),
					new Event("OnMissionAccepted"),
					new Event("OnMissionCompleted"),
					new Event("OnPlayerBlacklisted"),
					new Event("OnPlayerEnteredShip"),
					new Event("OnPlayerEnteredSystem"),
					new Event("OnPlayerLeftShip"),
					new Event("OnPlayerLeftSystem"),
					new Event("OnRandomEncounter"),
					new Event("OnSelected"),
					new Event("OnSubordinateAttacked"),
					new Event("OnSystemExplosion"),
					new Event("OnSystemObjAttacked"),
					new Event("OnSystemObjDestroyed"),
					new Event("OnSystemWeaponFire"),
					new Event("OnTranslateMessage"),
					new Event("OnUpdate")
					);
			break;
		case ShipTable:
			break;
		case Sound:
			break;
		case Sovereign:
			break;
		case SpaceEnvironment:
			e.addOptionalSingleSubElements(new Event("OnObjUpdate"));
			break;
		case SystemMap:
			break;
		case SystemTable:
			break;
		case SystemType:
			break;
		case TemplateType:
			e.addOptionalSingleSubElements(
					new Event("GetTypeSource")
					);
			break;
		case Type:
			break;
		default:
			break;
		}
		return e;
	}
	public static Element createLanguage(Types t) {
		Element e = new Element("Language");
		switch(t) {
		case AdventureDesc:
			e.addOptionalSingleSubElements(
					new Text("description")
					);
			break;
		case DockScreen:
			break;
		case EconomyType:
			break;
		case EffectType:
			break;
		case Image:
			break;
		case ItemTable:
			break;
		case ItemType:
			break;
		case MissionType:
			e.addOptionalSingleSubElements(
					new Text("Name"),
					new Text("Summary"),
					new Text("Intro"),
					new Text("Briefing"),
					new Text("AcceptReply"),
					new Text("DeclineReply"),
					new Text("InProgress"),
					new Text("SuccessMsg"),
					new Text("FailureMsg"),
					new Text("SuccessDebrief"),
					new Text("FailureDebrief"),
					new Text("AlreadyDebriefed"),
					new Text("Unavailable")
					);
			break;
		case NameGenerator:
			break;
		case OverlayType:
			break;
		case Power:
			break;
		case ShipClass:
			break;
		case ShipTable:
			break;
		case Sound:
			break;
		case Sovereign:
			e.addOptionalSingleSubElements(
					new Text("AttackTarget"),				//	msgAttack
					new Text("AttackTargetBroadcast"),	//	msgDestroyBroadcast
					new Text("HitByFriendlyFire"),		//	msgHitByFriendlyFire
					new Text("QueryEscortStatus"),		//	msgQueryEscortStatus
					new Text("QueryFleetStatus"),			//	msgQueryFleetStatus
					new Text("EscortAttacked"),			//	msgEscortAttacked
					new Text("EscortReportingIn"),		//	msgEscortReportingIn
					new Text("WatchYourTargets"),			//	msgWatchTargets
					new Text("NiceShooting"),				//	msgNiceShooting
					new Text("FormUp"),					//	msgFormUp
					new Text("BreakAndAttack"),			//	msgBreakAndAttack
					new Text("QueryComms"),				//	msgQueryCommunications
					new Text("AbortAttack"),				//	msgAbortAttack
					new Text("Wait"),						//	msgWait
					new Text("QueryWaitStatus"),			//	msgQueryWaitStatus
					new Text("AttackInFormation"),		//	msgAttackInFormation
					new Text("DeterTarget"),				//	msgAttackDeter
					new Text("QueryAttackStatus"),		//	msgQueryAttackStatus
					new Text("DockingSequenceEngaged"),	//	msgDockingSequenceEngaged
					new Text("HitByHostileFire"),			//	msgHitByHostileFire
					new Text("DestroyedByFriendlyFire"),	//	msgDestroyedByFriendlyFire
					new Text("DestroyedByHostileFire"),	//	msgDestroyedByHostileFire
					new Text("BaseDestroyedByTarget")		//	msgBaseDestroyedByTarget
					);
			break;
		case SpaceEnvironment:
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
			break;
		}
		return e;
	}
	public static Element createStaticData() {
		return new Element("StaticData");
	}
	public static Element createGlobalData() {
		return new Element("GlobalData");
	}
	public static Element createInitialData() {
		return new Element("InitialData");
	}
	public static enum SubElements implements SubElement {
		DockingPorts,
		Trade,
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
			Custom,
			
		//StationType
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
		
		Relationships,
			Relationship,
		
		Image,
		
		EdgeMask,
		
		Module, Library
	;

	@Override
	public Element create() {
		return createSubElement(this);
	}}
	
	public static Element createSubElement(SubElements s) {
		Element e = null;
		switch(s) {
		case Relationships:
			e = new Element("Relationships");
			e.addOptionalMultipleSubElements(SubElements.Relationship);
			break;
			
		case Relationship:
			e = new Element("Relationship");
			e.addRequiredAttributes(
					new Attribute("sovereign", ValueType.TYPE_SOVEREIGN),
					new Attribute("disposition", ValueType.DISPOSITION),
					new Attribute("mutual", ValueType.BOOLEAN)
					);
			break;
		case EdgeMask:
			e = new Element("EdgeMask");
			initializeImage(e);
			break;
		case Image:
			e = new Element("Image");
			initializeImage(e);
			break;
		case Module:
			e = new Element("Module");
			e.addRequiredAttributes(new Attribute("filename", ValueType.STRING));
			break;
		case Library:
			e = new Element("Library");
			e.addRequiredAttributes(new Attribute("unid", ValueType.TYPE_MOD));
			break;
		default:
			try {
				throw new Exception("Not supported: " + s.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			break;
		}
		return e;
	}
	public static void initializeImage(Element e) {
		e.addRequiredAttributes(
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
				);
	}
}

class Text extends Element {
	String displayName;
	public Text(String id) {
		super();
		displayName = id;
		addRequiredAttributes(new Attribute("id", ValueType.STRING, id));
	}
	public String getDisplayName() {
		return displayName;
	}
	//Make uneditable
	public void initializeFrame(Frame frame) {
		JPanel labelPanel = frame.getAttributeLabelPanel();
		JPanel fieldPanel = frame.getAttributeFieldPanel();
		JPanel subElementPanel = frame.getSubElementPanel();
		JTextArea textArea = frame.getTextArea();
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		
		for(Attribute a : getAttributes()) {
			JLabel label = new JLabel(a.getName() + "=");
			label.setFont(Window.FONT_MEDIUM);
			labelPanel.add(label);
			JLabel value = new JLabel(a.getValue());
			value.setFont(Window.FONT_MEDIUM);
			fieldPanel.add(value);
		}
		
		textArea.setText(getText());
	}
}
class Event extends Element {
	public Event(String name) {
		super();
	}
}