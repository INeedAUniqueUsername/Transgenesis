package designType.subElements;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import designType.TypeFactory.Types;
import designType.subElements.SubElementFactory.AdventureDescElements;
import designType.subElements.SubElementFactory.DisplayElements;
import designType.subElements.SubElementFactory.EffectElements;
import designType.subElements.SubElementFactory.ItemGeneratorElements;
import designType.subElements.SubElementFactory.MiscElements;
import designType.subElements.SubElementFactory.SovereignElements;
import designType.subElements.SubElementFactory.TradeElements;
import window.Frame;
import window.Window;
import xml.Attribute;
import xml.Attribute.ValueType;
import xml.DesignElement;
import static xml.Attribute.ValueType.*;

public class SingleSubElementFactory {

	//Done
	public static DesignElement createEvents(Types type) {
		DesignElement e = new DesignElement("Events");
		e.addOptionalSingleSubElements(
					new Event("GetGlobalAchievements"),
					new Event("GetGlobalDockScreen"),
					new Event("GetGlobalPlayerPriceAdj"),
					new Event("OnGlobalPaneInit"),
					new Event("OnGlobalMarkImages"),
	
					new Event("OnGlobalObjDestroyed"),
					new Event("OnGlobalPlayerBoughtItem"),
					new Event("OnGlobalPlayerSoldItem"),
					new Event("OnGlobalSystemStarted"),
					new Event("OnGlobalSystemStopped"),
	
					new Event("OnGlobalUniverseCreated"),
					new Event("OnGlobalUniverseLoad"),
					new Event("OnGlobalUniverseSave"),
					new Event("OnGlobalUpdate")
				);
		switch(type) {
		case AdventureDesc:
			e.addOptionalSingleSubElements(
					new Event("OnGameStart"),
					new Event("OnGameEnd")
					);
			break;
		case DockScreen:			break;
		case EconomyType:			break;
		case EffectType:			break;
		case Image:					break;
		case ItemTable:				break;
		case ItemType:				break;
		case MissionType:
			e.addOptionalSingleSubElements(
					new Event("OnCreate"),
					new Event("OnDestroy"),
					new Event("OnAccepted"),
					new Event("OnAcceptedUndock"),
					new Event("OnDeclined"),
					new Event("OnStarted"),
					new Event("OnInProgress"),
					new Event("OnInProgressUndock"),
					new Event("OnSetPlayerTarget"),
					new Event("OnCompleted"),
					new Event("OnCanDebrief"),
					new Event("OnDebriefedUndock"),
					new Event("OnGetNextScreen"),
					new Event("OnReward")
				);
		case NameGenerator:			break;
		case OverlayType:
			e.addOptionalSingleSubElements(
					new Event("OnCreate"),
					new Event("OnUpdate"),
					new Event("OnDamage"),
					new Event("OnDestroy"),
					new Event("OnObjDestroyed")
					);
			break;
		case Power: 				break;
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
		case ShipTable:				break;
		case Sound:					break;
		case Sovereign:				break;
		case SpaceEnvironmentType:
			e.addOptionalSingleSubElements(new Event("OnObjUpdate"));
			break;
		case SystemMap: 			break;
		case SystemTable: 			break;
		case SystemType:
			e.addOptionalSingleSubElements(
					new Event("OnCreate"),
					new Event("OnObjJumpPosAdj")
					);
			break;
		case TemplateType:
			e.addOptionalSingleSubElements(
					new Event("GetTypeSource")
					);
			break;
		case Type:					break;
		default:					break;
		}
		return e;
	}
	//Done
	public static DesignElement createLanguage(Types t) {
		DesignElement e = new DesignElement("Language");
		
		switch(t) {
		case AdventureDesc:
			e.addOptionalSingleSubElements(
					new Text("description")
					);
			break;
		case DockScreen:			break;
		case EconomyType:			break;
		case EffectType:			break;
		case Image:					break;
		case ItemTable:				break;
		case ItemType:				break;
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
		case NameGenerator:			break;
		case OverlayType:			break;
		case Power:					break;
		case ShipClass:				break;
		case ShipTable:				break;
		case Sound:					break;
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
		case SpaceEnvironmentType:	break;
		case StationType:			break;
		case SystemMap:				break;
		case SystemTable:			break;
		case SystemType:			break;
		case TemplateType:			break;
		case Type:					break;
		default:
			break;
		}
		return e;
	}

	public static DesignElement[] createSingleSubElementsForType(Types t) {
		switch(t) {
		case AdventureDesc:
			DesignElement encounterOverrides = new DesignElement("EncounterOverrides");
			encounterOverrides.addOptionalMultipleSubElements(AdventureDescElements.EncounterOverrides);
			DesignElement constants = new DesignElement("Constants");
			constants.addOptionalMultipleSubElements(
					AdventureDescElements.ArmorDamageAdj,
					AdventureDescElements.ShieldDamageAdj
					);
			return new DesignElement[] {
					encounterOverrides, constants
			}
			;
		case DockScreen:
			DesignElement listOptions = new DesignElement("ListOptions");
			DesignElement list = new DesignElement("List");
			for(DesignElement e : new DesignElement[] {listOptions, list}) {
				e.addAttributes(
						new Attribute("dataFrom", ValueType.DOCKSCREEN_DATA_FROM),
						new Attribute("criteria", STRING),
						new Attribute("list", STRING),
						new Attribute("initialItem", STRING),
						new Attribute("rowHeight", WHOLE),
						new Attribute("noArmorSpeedDisplay", BOOLEAN),
						new Attribute("slotName", STRING),
						new Attribute("noEmptySlots", BOOLEAN),
						new Attribute("posX", INTEGER),
						new Attribute("posY", INTEGER),
						new Attribute("width", WHOLE),
						new Attribute("height", WHOLE)
						);
			}
			Event onScreenInit = new Event("OnScreenInit");
			Event onInit = new Event("OnInit");
			Event initialPane = new Event("InitialPane");
			Event onScreenUpdate = new Event("OnScreenUpdate");
			DesignElement display = new DesignElement("Display");
			display.addAttributes(
					new Attribute("display", STRING),
					new Attribute("animate", STRING),
					new Attribute("type", DOCKSCREEN_TYPE),
					new Attribute("dataFrom", DOCKSCREEN_DATA_FROM)
					);
			display.addOptionalSingleSubElements(
					new Event("OnDisplayInit")
					);
			display.addOptionalMultipleSubElements(
					DisplayElements.values()
					);
			DesignElement canvas = new DesignElement("Canvas");
			canvas.addAttributes(
					new Attribute("left", INTEGER),
					new Attribute("right", INTEGER),
					new Attribute("top", INTEGER),
					new Attribute("bottom", INTEGER)
					);
			//WIP
			DesignElement panes = new DesignElement("Panes");
			return new DesignElement[] {
				listOptions, list, onScreenInit, onInit, initialPane, onScreenUpdate, display, canvas, panes
			};
		case EconomyType:			break;
		case EffectType:			break;
		case Image:					break;
		case ItemTable:				break;
		case ItemType:
			DesignElement invoke = new DesignElement("Invoke");
			invoke.addAttributes(
					new Attribute("key", CHARACTER),
					new Attribute("installedOnly", BOOLEAN),
					new Attribute("uninstalledOnly", BOOLEAN),
					new Attribute("enabledOnly", BOOLEAN),
					new Attribute("completeArmorOnly", BOOLEAN),
					new Attribute("asArmorSet", BOOLEAN)
					);
			DesignElement onRefuel = new DesignElement("OnRefuel");
			DesignElement components = new DesignElement("Components");
			components.addOptionalMultipleSubElements(ItemGeneratorElements.values());
			
			DesignElement initialData = new DesignElement("InitialData");
			initialData.addOptionalMultipleSubElements(MiscElements.Data);
			//WIP
			//Add Armor, Devices, etc
			return new DesignElement[] {
					invoke, onRefuel, components, initialData
			};
		case MissionType:			break;
		case NameGenerator:			break;
		case OverlayType:
			DesignElement effect = new DesignElement(SubElementFactory.createEffects, "Effect");
			DesignElement hitEffect = new DesignElement(EffectElements.values(), "HitEffect");
			DesignElement effectWhenHit = new DesignElement(EffectElements.values(), "EffectWhenHit");
			effectWhenHit.addAttributes(new Attribute("altEffect", BOOLEAN));
			DesignElement counter = new DesignElement("Counter");
			counter.addAttributes(
					new Attribute("style", ValueType.OVERLAY_COUNTER_STTYLE),
					new Attribute("label", STRING),
					new Attribute("max", WHOLE),
					new Attribute("color", HEX_COLOR),
					new Attribute("showOnMap", BOOLEAN)
					);
			return new DesignElement[] {
				effect, hitEffect, effectWhenHit, counter
			};
		case Power:
			return new DesignElement[] {
					new DesignElement("OnShow"),
					new DesignElement("OnInvokedByPlayer"),
					new DesignElement("OnInvoke"),
					new DesignElement("OnDestroyCheck")	
			};
		case ShipClass:				break;
		case ShipTable:				break;
		case Sound:					break;
		case Sovereign:
			DesignElement relationships = new DesignElement("Relationships");
			relationships.addOptionalMultipleSubElements(SovereignElements.Relationship);
			return new DesignElement[] {
					relationships
			};
		case SpaceEnvironmentType:	break;
		case StationType:			break;
		case SystemMap:				break;
		case SystemTable:			break;
		case SystemType:			break;
		case TemplateType:			break;
		case Type:					break;
		default:					break;
		}
		return new DesignElement[] {
		};
	}
	public static DesignElement[] createSpaceObjectSubElements() {
		DesignElement names = new DesignElement("Names");
		names.addAttributes(
				SubElementFactory.createNameAttributes()
				);
		
		DesignElement items = new DesignElement("Items");
		items.addOptionalMultipleSubElements(ItemGeneratorElements.values());
		
		DesignElement devices = new DesignElement("Devices");
		
		DesignElement image = new DesignElement("Image");
		image.addAttributes(SubElementFactory.createImageAttributes());
		
		DesignElement heroImage = new DesignElement("HeroImage");
		heroImage.addAttributes(SubElementFactory.createImageAttributes());
		
		DesignElement initialData = new DesignElement("InitialData");
		initialData.addOptionalMultipleSubElements(MiscElements.Data);
		
		DesignElement dockingPorts = new DesignElement("DockingPorts");
		dockingPorts.addAttributes(
				new Attribute("bringToFront", STRING),
				new Attribute("sendToBack", STRING),
				new Attribute("maxDist", WHOLE),
				new Attribute("portAngle", INTEGER),
				new Attribute("portCount", WHOLE),
				new Attribute("portRadius", INTEGER),
				new Attribute("rotation", INTEGER),
				new Attribute("x", INTEGER),
				new Attribute("y", INTEGER)
				);
		DesignElement trade = new DesignElement("Trade");
		trade.addAttributes(
				new Attribute("currency", STRING),
				new Attribute("creditConversion", WHOLE),
				new Attribute("max", WHOLE),
				new Attribute("replenish", WHOLE)
				);
		trade.addOptionalMultipleSubElements(TradeElements.values());
		
		try {
			throw new Exception("") {};
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DesignElement[0];
	}
	
	//Done
	public static enum DataElements implements SubElementType {
		StaticData,
		GlobalData,
		;
		@Override
		public DesignElement create() {
			DesignElement e = new DesignElement(name());
			e.addOptionalMultipleSubElements(MiscElements.Data);
			return e;
		}
	}
}

class Event extends DesignElement {
	public Event(String name) {
		super(name);
	}
}
class Text extends DesignElement {
	String displayName;
	public Text(String id) {
		super();
		displayName = id;
		addAttributes(new Attribute("id", STRING, id));
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
