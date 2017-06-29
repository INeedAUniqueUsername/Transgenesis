package designType.subElements;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import designType.Types;
import designType.subElements.SubElementFactory.AdventureDescElements;
import designType.subElements.SubElementFactory.DeviceTableElements;
import designType.subElements.SubElementFactory.DisplayElements;
import designType.subElements.SubElementFactory.DockScreensElements;
import designType.subElements.SubElementFactory.EffectElements;
import designType.subElements.SubElementFactory.ItemGeneratorElements;
import designType.subElements.SubElementFactory.MiscElements;
import designType.subElements.SubElementFactory.SovereignElements;
import designType.subElements.SubElementFactory.TradeElements;
import window.Frame;
import window.Window;
import xml.DesignAttribute;
import xml.DesignAttribute.ValueType;

import xml.DesignElement;
import static xml.DesignAttribute.ValueType.*;
import static xml.DesignElement.ele;
import static xml.DesignAttribute.att;
import java.util.function.Supplier;

import static xml.DesignAttribute.*;
public class SingleSubElementFactory {

	//Done
	public static DesignElement createEvents(Types type) {
		DesignElement e = new DesignElement("Events");
		e.addOptionalSingleSubElements(
				new Event("GetCreatePos"),
				new Event("GetGlobalAchievements"),
				new Event("GetGlobalDockScreen"),
				new Event("GetGlobalPlayerPriceAdj"),
				new Event("GetGlobalResurrectPotential"),
				new Event("OnGlobalEndDiagnostics"),
				new Event("OnGlobalMarkImages"),
				new Event("OnGlobalObjDestroyed"),
				new Event("OnGlobalPaneInit"),
				new Event("OnGlobalPlayerBoughtItem"),
				new Event("OnGlobalPlayerChangedShips"),
				new Event("OnGlobalPlayerEnteredSystem"),
				new Event("OnGlobalPlayerLeftSystem"),
				new Event("OnGlobalPlayerSoldItem"),
				new Event("OnGlobalResurrect"),
				new Event("OnGlobalTopologyCreated"),
				new Event("OnGlobalStartDiagnostics"),
				new Event("OnGlobalSystemDiagnostics"),
				new Event("OnGlobalSystemCreated"),
				new Event("OnGlobalSystemStarted"),
				new Event("OnGlobalSystemStopped"),
				new Event("OnGlobalUniverseCreated"),
				new Event("OnGlobalUniverseLoad"),
				new Event("OnGlobalUniverseSave"),
				new Event("OnGlobalUpdate"),
				new Event("OnRandomEncounter")
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
		case ItemType:
			e.addOptionalSingleSubElements(
					new Event("CanBeInstalled"),
					new Event("CanBeUninstalled"),
					new Event("OnAddedAsEnhancement"),
					new Event("OnDisable"),
					new Event("OnEnable"),
					
					new Event("GetDescription"),
					new Event("GetName"),
					new Event("GetTradePrice"),
					
					new Event("OnInstall"),
					new Event("OnObjDestroyed"),
					new Event("OnReactorOverload"),
					
					new Event("OnRefuel"),
					
					new Event("OnRemovedAsEnhancement"),
					new Event("OnUninstall")
					);
			break;
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
					new Event("CanRemoveItem"),
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
						att("dataFrom", ValueType.DOCKSCREEN_DATA_FROM),
						att("criteria", STRING),
						att("list", STRING),
						att("initialItem", STRING),
						att("rowHeight", WHOLE),
						att("noArmorSpeedDisplay", BOOLEAN),
						att("slotName", STRING),
						att("noEmptySlots", BOOLEAN),
						att("posX", INTEGER),
						att("posY", INTEGER),
						att("width", WHOLE),
						att("height", WHOLE)
						);
			}
			Event onScreenInit = new Event("OnScreenInit");
			Event onInit = new Event("OnInit");
			Event initialPane = new Event("InitialPane");
			Event onScreenUpdate = new Event("OnScreenUpdate");
			DesignElement display = new DesignElement("Display");
			display.addAttributes(
					att("display", STRING),
					att("animate", STRING),
					att("type", DOCKSCREEN_TYPE),
					att("dataFrom", DOCKSCREEN_DATA_FROM)
					);
			display.addOptionalSingleSubElements(
					new Event("OnDisplayInit")
					);
			display.addOptionalMultipleSubElements(
					DisplayElements.values()
					);
			DesignElement canvas = new DesignElement("Canvas");
			canvas.addAttributes(
					att("left", INTEGER),
					att("right", INTEGER),
					att("top", INTEGER),
					att("bottom", INTEGER)
					);
			//WIP
			DesignElement panes = new DesignElement("Panes");
			panes.addOptionalMultipleSubElements(DockScreensElements.Pane_Named);
			return new DesignElement[] {
				listOptions, list, onScreenInit, onInit, initialPane, onScreenUpdate, display, canvas, panes
			};
		case EconomyType:			break;
		case EffectType:			break;
		case Image:					break;
		case ItemTable:				break;
		case ItemType:
			DesignElement
			armor = new DesignElement("Armor"),
			autoDefenseDevice = new DesignElement("AutoDefenseDevice"),
			cargoHoldDevice = new DesignElement("CargoHoldDevice"),
			components = new DesignElement("Components"),
			cyberDeckDevice = new DesignElement("CyberDeckDevice"),
			driveDevice = new DesignElement("DriveDevice"),
			enhancerDevice = new DesignElement("EnhancerDevice"),
			image = new DesignElement("Image"),
			initialData = new DesignElement("InitialData"),
			invoke = new DesignElement("Invoke"),
			miscellaneousDevice = new DesignElement("MiscellaneousDevice"),
			missile = new DesignElement("Missile"),
			names = new DesignElement("Names"),
			reactorDevice = new DesignElement("ReactorDevice"),
			repairerDevice = new DesignElement("RepairerDevice"),
			shields = new DesignElement("Shields"),
			solarDevice = new DesignElement("SolarDevice"),
			weapon = new DesignElement("Weapon");
			armor.addAttributes(
					att("blindingDamageAdj", WHOLE),
					att("blindingImmune", BOOLEAN),
					att("chargeDecay", WHOLE),
					att("chargeRegen", WHOLE),
					att("completeBonus", WHOLE),
					att("damageAdjLevel", INTEGER_SEQUENCE),
					att("decay", WHOLE),
					att("decayRate", WHOLE),
					att("deviceCriteria", STRING),
					att("deviceDamageAdj", WHOLE),
					att("deviceDamageImmune", BOOLEAN),
					att("deviceHPBonus", WHOLE),
					att("distribute", WHOLE),
					att("disintegrationImmune", BOOLEAN),
					att("enhancementType", HEX_NUMBER),
					att("EMPDamageAdj", WHOLE),
					att("EMPImmune", BOOLEAN),
					att("idlePowerUse", WHOLE),
					att("installCost", WHOLE),
					att("installCostAdj", WHOLE),
					att("maxHPBonus", WHOLE),
					att("maxSpeedBonus", WHOLE),
					att("photoRecharge", BOOLEAN),
					att("photoRepair", BOOLEAN),
					att("powerUse", WHOLE),
					att("radiationImmune", BOOLEAN),
					att("reflect", STRING),
					att("regen", WHOLE),
					att("repairCost", WHOLE),
					att("repairCostAdj", WHOLE),
					att("repairRate", WHOLE),
					att("repairTech", WHOLE),
					att("shatterImmune", BOOLEAN),
					att("shieldInterference", BOOLEAN),
					att("stealth", WHOLE),
					//att("unid", ),
					att("useHealerToRegen", BOOLEAN)
					);
			autoDefenseDevice.addAttributes(
					att("fireRate", WHOLE),
					att("weapon", TYPE_WEAPON),
					att("interceptRange", WHOLE),
					att("targetCriteria", STRING),
					att("target", STRING, "missiles")
					);
			cargoHoldDevice.addAttributes(
					att("cargoSpace", INTEGER)
					);
			components.addOptionalMultipleSubElements(
					ItemGeneratorElements.values()
					);
			cyberDeckDevice.addAttributes(
					att("range", WHOLE),
					att("attackChance", WHOLE),
					att("aiLevel", WHOLE),
					att("program", PROGRAM),
					att("programName", STRING)
					);
			driveDevice.addAttributes(
					att("maxSpeed", WHOLE),
					att("maxSpeedInc", WHOLE),
					att("powerUse", WHOLE),
					att("thrust", DECIMAL),
					att("inertialessDrive", BOOLEAN),
					att("rotationAccel", DECIMAL),
					att("rotationStopAccel", DECIMAL),
					att("maxRotationRate", DECIMAL)
					);
			enhancerDevice.addAttributes(
					att("W.I.P.", STRING)
					);
			image.addAttributes(SubElementFactory.createImageAttributes());
			
			invoke.addAttributes(
					att("key", CHARACTER),
					att("installedOnly", BOOLEAN),
					att("uninstalledOnly", BOOLEAN),
					att("enabledOnly", BOOLEAN),
					att("completeArmorOnly", BOOLEAN),
					att("asArmorSet", BOOLEAN)
					);
			miscellaneousDevice.addAttributes(
					att("powerUse", WHOLE),
					att("powerToActivate", WHOLE),
					att("capacitorPowerUse", WHOLE),
					att("powerRating", WHOLE)
					);
			missile.addAttributes(att("W.I.P.", STRING));
			reactorDevice.addAttributes(
					att("fuelCapacity", WHOLE),
					att("fuelCriteria", STRING),
					att("maxFuel", WHOLE),
					att("maxFuelTech", WHOLE),
					att("minFuelTech", WHOLE),
					att("maxPower", WHOLE),
					att("maxPowerBonusPerCharge", WHOLE),
					att("noFuel", BOOLEAN),
					att("reactorEfficiency", WHOLE),
					att("reactorPower", WHOLE)
					);
			shields.addAttributes(
					att("absorbAdj", WHOLE),
					att("armorShield", WHOLE),
					att("damageAdj", STRING),
					att("damageAdjLevel", STRING),
					att("depletionDelay", WHOLE),
					att("hitEffect", TYPE_EFFECT),
					att("hitPoints", WHOLE),
					att("idlePowerUse", WHOLE),
					att("hasNonRegenHP", BOOLEAN),
					att("hpBonus", WHOLE),
					att("HPBonusPerCharge", WHOLE),
					att("maxCharges", WHOLE),
					att("powerBonusPerCharge", WHOLE),
					att("powerUse", WHOLE),
					att("reflect", STRING),
					att("regen", WHOLE),
					att("regenHP", WHOLE),
					att("regenHPBonusPerCharge", WHOLE),
					att("regenTime", WHOLE),
					att("weaponSuppress", STRING),
					att("maxHPBonus", WHOLE)
					);
			solarDevice.addAttributes(
					att("refuel", WHOLE),
					att("powerGen", WHOLE)
					);
			weapon.addAttributes();
			//WIP: Add device-specific events
			components.addOptionalMultipleSubElements(ItemGeneratorElements.values());
			
			initialData.addOptionalMultipleSubElements(MiscElements.Data);
			//WIP
			
			//Add Armor, Devices, etc
			return new DesignElement[] {
					armor,
					autoDefenseDevice,
					cargoHoldDevice,
					components,
					cyberDeckDevice,
					driveDevice,
					enhancerDevice,
					image,
					initialData,
					invoke,
					miscellaneousDevice,
					missile,
					names,
					reactorDevice,
					repairerDevice,
					shields,
					solarDevice,
					weapon
			};
		case MissionType:			break;
		case NameGenerator:			break;
		case OverlayType:
			DesignElement effect = new DesignElement(SubElementFactory.createEffects(), "Effect");
			DesignElement hitEffect = new DesignElement(SubElementFactory.createEffects(), "HitEffect");
			DesignElement effectWhenHit = new DesignElement(SubElementFactory.createEffects(), "EffectWhenHit");
			effectWhenHit.addAttributes(att("altEffect", BOOLEAN));
			DesignElement counter = new DesignElement("Counter");
			counter.addAttributes(
					att("style", ValueType.OVERLAY_COUNTER_STTYLE),
					att("label", STRING),
					att("max", WHOLE),
					att("color", HEX_COLOR),
					att("showOnMap", BOOLEAN)
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
		devices.addOptionalMultipleSubElements(
				DeviceTableElements.values()
				);
		DesignElement image = new DesignElement("Image");
		image.addAttributes(SubElementFactory.createImageAttributes());
		
		DesignElement heroImage = new DesignElement("HeroImage");
		heroImage.addAttributes(SubElementFactory.createImageAttributes());
		
		DesignElement initialData = new DesignElement("InitialData");
		initialData.addOptionalMultipleSubElements(MiscElements.Data);
		
		DesignElement dockingPorts = new DesignElement("DockingPorts");
		dockingPorts.addAttributes(
				att("bringToFront", STRING),
				att("sendToBack", STRING),
				att("maxDist", WHOLE),
				att("portAngle", INTEGER),
				att("portCount", WHOLE),
				att("portRadius", INTEGER),
				att("rotation", INTEGER),
				att("x", INTEGER),
				att("y", INTEGER)
				);
		DesignElement trade = new DesignElement("Trade");
		trade.addAttributes(
				att("currency", STRING),
				att("creditConversion", WHOLE),
				att("max", WHOLE),
				att("replenish", WHOLE)
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
		public DesignElement get() {
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
		addAttributes(att("id", STRING, id));
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
		
		for(DesignAttribute a : getAttributes()) {
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
