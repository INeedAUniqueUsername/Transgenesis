package designType.subElements;

import designType.Types;
import xml.DesignElementOld;

public final class Events {
	private Events() {}
	public static DesignElementOld createEvents(Types type) {
		DesignElementOld e = new DesignElementOld("Events");
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

}
