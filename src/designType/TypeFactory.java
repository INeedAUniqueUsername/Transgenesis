package designType;

import designType.subElements.DataElements;
import designType.subElements.ElementType;
import designType.subElements.Event;
import designType.subElements.Events;
import designType.subElements.Language;
import designType.subElements.SpaceObject;
import designType.subElements.SubElementFactory;
import designType.subElements.SubElementFactory.AdventureDescElements;
import designType.subElements.SubElementFactory.DeviceGeneratorElements;
import designType.subElements.SubElementFactory.DisplayAttributesElements;
import designType.subElements.SubElementFactory.DisplayElements;
import designType.subElements.SubElementFactory.DockScreensElements;
import designType.subElements.SubElementFactory.EffectElements;
import designType.subElements.SubElementFactory.ItemElements;
import designType.subElements.SubElementFactory.ItemGeneratorElements;
import designType.subElements.SubElementFactory.ShipGeneratorElements;
import designType.subElements.SubElementFactory.SovereignElements;
import designType.subElements.SubElementFactory.SystemCriteria;
import designType.subElements.SubElementFactory.SystemGroupElements;
import designType.subElements.SubElementFactory.SystemMapElements;
import designType.subElements.SubElementFactory.SystemPartTableElements;
import xml.DesignAttribute;
import xml.DesignAttribute.ValueType;
import xml.DesignElement;
import static xml.DesignAttribute.ValueType.*;
import static designType.AttributeFactory.addDeviceContent;
import static xml.DesignAttribute.*;
import static xml.DesignElement.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
public final class TypeFactory {
	//\#define ([A-Z]*_*)*\s+CONSTLIT\(\"(.+)\"\)
	public static DesignElement createDesignType(Types t) {
		DesignElement e = new Type(t.name());
		e.addAttributes(AttributeFactory.createAttributesForType(t));
		e.addOptionalSingleSubElements(createSingleSubElementsForType(t));
		e.addOptionalMultipleSubElements(createMultipleSubElementsForType(t));
		e.addAttributes(
				att("UNID", UNID),
				att("attributes", STRING),
				att("inherit", TYPE_INHERITED),
				att("extends", STRING),
				att("obsolete", WHOLE)
				);
		DesignElement communications = ele("Communications");
		communications.addOptionalMultipleSubElements(
				() -> {
					DesignElement message = ele("Message");
					message.addAttributes(
							att("id", STRING),
							att("name", STRING),
							att("key", CHARACTER)
							);
					message.addOptionalSingleSubElements(
							ele("OnShow"),
							ele("Invoke")
							);
					return message;
				}
				);
		DesignElement dockScreens = ele("DockScreens");
		dockScreens.addOptionalMultipleSubElements(DockScreensElements.DockScreen_Named);
		DesignElement displayAttributes = ele("DisplayAttributes");
		displayAttributes.addOptionalMultipleSubElements(DisplayAttributesElements.ItemAttribute);
		DesignElement attributeDesc = ele("AttributeDesc");
		attributeDesc.addOptionalMultipleSubElements(DisplayAttributesElements.ItemAttribute);
		e.addOptionalSingleSubElements(
				communications,
				DataElements.StaticData.get(),
				DataElements.GlobalData.get(),
				Language.createLanguage(t),
				Events.createEvents(t),
				dockScreens,
				displayAttributes,
				attributeDesc
				);
		
		return e;
	}

	public static ElementType[] createMultipleSubElementsForType(Types t) {
		switch(t) {
		case AdventureDesc:
			break;
		case DockScreen:
			break;
		case EconomyType:
			break;
		case EffectType:
			return EffectElements.values();
		case Image:
			break;
		case ImageComposite:
			break;
		case ItemTable:
			return ItemGeneratorElements.values();
		case ItemType:
			break;
		case MissionType:
			break;
		case NameGenerator:
			break;
		case OverlayType:
			break;
		case Power:
			break;
		case ShipClass:
			break;
		case ShipClassOverride:
			break;
		case ShipTable:
			return ShipGeneratorElements.values();
		case Sound:
			break;
		case Soundtrack:
			break;
		case Sovereign:
			break;
		case SpaceEnvironmentType:
			break;
		case StationType:
			break;
		case SystemMap:
			return SystemMapElements.values();
		case SystemPartTable:
			return SystemPartTableElements.values();
		case SystemType:
			break;
		case TemplateType:
			break;
		case Type:
			break;
		default:
			break;
		}
		return new ElementType[0];
	}

	public static DesignElement[] createSingleSubElementsForType(Types t) {
		switch(t) {
		case AdventureDesc:
			DesignElement encounterOverrides = ele("EncounterOverrides");
			encounterOverrides.addOptionalMultipleSubElements(AdventureDescElements.EncounterOverrides);
			DesignElement constants = ele("Constants");
			constants.addOptionalMultipleSubElements(
					AdventureDescElements.ArmorDamageAdj,
					AdventureDescElements.ShieldDamageAdj
					);
			return new DesignElement[] {
					encounterOverrides, constants
			}
			;
		case DockScreen:
			DesignElement listOptions = ele("ListOptions");
			DesignElement list = ele("List");
			for(DesignElement e : new DesignElement[] {listOptions, list}) {
				e.addAttributes(
						att("dataFrom", ValueType.DATA_FROM),
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
			DesignElement display = ele("Display");
			display.addAttributes(
					att("display", STRING),
					att("animate", STRING),
					att("type", DOCKSCREEN_TYPE),
					att("dataFrom", DATA_FROM)
					);
			display.addOptionalSingleSubElements(
					new Event("OnDisplayInit")
					);
			display.addOptionalMultipleSubElements(
					DisplayElements.values()
					);
			DesignElement canvas = ele("Canvas");
			canvas.addAttributes(
					att("left", INTEGER),
					att("right", INTEGER),
					att("top", INTEGER),
					att("bottom", INTEGER)
					);
			//WIP
			DesignElement panes = ele("Panes");
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
			armor = ele("Armor"),
			autoDefenseDevice = ele("AutoDefenseDevice"),
			cargoHoldDevice = ele("CargoHoldDevice"),
			components = ele("Components"),
			cyberDeckDevice = ele("CyberDeckDevice"),
			driveDevice = ele("DriveDevice"),
			enhancerDevice = ele("EnhancerDevice"),
			image = ele("Image"),
			initialData = DataElements.InitialData.get(),
			invoke = ele("Invoke"),
			miscellaneousDevice = ele("MiscellaneousDevice"),
			missile = SubElementFactory.createWeaponDescElement("Missile"),
			names = ele("Names"),
			reactorDevice = ele("ReactorDevice"),
			repairerDevice = ele("RepairerDevice"),
			shields = ele("Shields"),
			solarDevice = ele("SolarDevice"),
			weapon = ItemElements.Weapon.get();
			//Add armor subelements
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
					att("photoRecharge", STRING),
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
					att("thrust", DOUBLE),
					att("inertialessDrive", BOOLEAN),
					att("rotationAccel", DOUBLE),
					att("rotationStopAccel", DOUBLE),
					att("maxRotationRate", DOUBLE)
					);
			enhancerDevice.addAttributes(
					att("activateAdj", WHOLE),
					att("criteria", STRING),
					att("hpBonus", WHOLE),
					att("minActivateDelay", INTEGER),
					att("maxActivateDelay", INTEGER),
					att("damageAdj", INTEGER_SEQUENCE),
					att("powerUse", WHOLE)
					);
			image.addAttributes(SubElementFactory.createImageDescAttributes());
			
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
			//WIP
			//names
			//missile
			reactorDevice.addAttributes(
					att("fuelCriteria", STRING),
					att("maxFuel", DOUBLE),
					att("maxFuelTech", WHOLE),
					att("minFuelTech", WHOLE),
					att("maxPower", WHOLE),
					att("maxPowerBonusPerCharge", WHOLE),
					att("noFuel", BOOLEAN),
					att("reactorEfficiency", DOUBLE)
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
			//WIP: Add device-specific events
			components.addOptionalMultipleSubElements(ItemGeneratorElements.values());
			
			
			for(DesignElement e : new DesignElement[] {
					autoDefenseDevice,
					cargoHoldDevice,
					cyberDeckDevice,
					driveDevice,
					enhancerDevice,
					miscellaneousDevice,
					reactorDevice,
					repairerDevice,
					shields,
					solarDevice,
					weapon
			}) {
				addDeviceContent(e);
			}
			
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
			DesignElement effect = new DesignElement(Types.EffectType.get(), "Effect"),
			hitEffect = new DesignElement(Types.EffectType.get(), "HitEffect"),
			effectWhenHit = new DesignElement(Types.EffectType.get(), "EffectWhenHit"),
			counter = ele("Counter");
			
			effectWhenHit.addAttributes(att("altEffect", BOOLEAN));
			counter.addAttributes(
					att("style", ValueType.STYLE_COUNTER),
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
					ele("OnShow"),
					ele("OnInvokedByPlayer"),
					ele("OnInvoke"),
					ele("OnDestroyCheck")	
			};
		case ShipClass:
		case ShipClassOverride:
			List<DesignElement> result = new ArrayList<>(Arrays.asList());
			result.addAll(Arrays.asList(SpaceObject.createSpaceObjectSubElements(t)));
			result.addAll(Arrays.asList(new DesignElement[] {
					new DesignElement("AISettings") {{
						addAttributes(
								att("combatStyle", ValueType.COMBAT_STYLE, "standard"),
								//Deprecated
								//att("flyBy", BOOLEAN),
								//att("standOff", BOOLEAN),
								att("flockingStyle", FLOCKING_STYLE),
								att("flockFormation", BOOLEAN),
								att("fireRateAdj", WHOLE),
								att("fireRangeAdj", WHOLE),
								att("fireAccuracy", WHOLE_100),
								att("perception", WHOLE, "4"),
								att("combatSeparation", INTEGER),
								att("ascendOnGate", BOOLEAN),
								att("ignoreShieldsDown", BOOLEAN),
								att("noDogfights", BOOLEAN),
								att("nonCombatant", BOOLEAN),
								att("noFriendlyFire", BOOLEAN),
								att("aggressor", BOOLEAN),
								att("noAttackOnThreat", BOOLEAN),
								att("noTargetsOfOpportunity", BOOLEAN),
								att("noFriendlyFireCheck", BOOLEAN),
								att("noNavPaths", BOOLEAN),
								att("noOrderGiver", BOOLEAN)
								);
					}},
					new DesignElement("Armor") {{
						addAttributes(
								att("armorID", TYPE_ARMOR),
								att("count", WHOLE),
								att("enhanced", INTEGER),
								att("enhancement", STRING),
								att("level", WHOLE),
								att("startAt", INTEGER)
								);
						addOptionalMultipleSubElements(() -> {
							return new DesignElement("ArmorSection") {{
								addAttributes(
										att("start", INTEGER),
										att("span", INTEGER),
										att("armorID", TYPE_ARMOR),
										att("level", WHOLE),
										att("nonCritical", ValueType.ARMOR_NONCRITICAL)
										);
							}};
						});
					}},
					//WIP
					new DesignElement("DriveImages") {{
						addOptionalMultipleSubElements(
								() -> {
									return new DesignElement("NozzleImage") {{
										addAttributes(SubElementFactory.createImageDescAttributes());
									}};
								}, () -> {
									return new DesignElement("NozzlePos") {{
										addAttributes(
												SubElementFactory.createShipEffectAttributes()
												);
									}};
								}
								);
					}},
					new DesignElement("Effects") {{
						addOptionalMultipleSubElements(
								() -> {
									return new DesignElement("Effect") {{
										addAttributes(
												att("effect", TYPE_EFFECT),
												att("type", SHIP_EFFECT_TYPE),
												att("rotation", INTEGER)
												);
										addAttributes(SubElementFactory.createShipEffectAttributes());
										addOptionalMultipleSubElements(TypeFactory.createMultipleSubElementsForType(Types.EffectType));
										addOptionalSingleSubElements(TypeFactory.createSingleSubElementsForType(Types.EffectType));
									}};
								}
								);
					}},
					new DesignElement("Equipment") {{
						addOptionalMultipleSubElements(
								() -> {
									return new DesignElement("Install") {{
										addAttributes(att("equipment", SHIP_EQUIPMENT));
									}};
								}, () -> {
									return new DesignElement("Remove") {{
										addAttributes(att("equipment", SHIP_EQUIPMENT));
									}};
								}
								);
					}},
					new DesignElement("Escorts") {{
						addOptionalMultipleSubElements(ShipGeneratorElements.values());
					}}, SubElementFactory.createImageDescElement("Image"),
					new DesignElement("Interior") {{
						addOptionalMultipleSubElements(
								() -> {
									return new DesignElement("Compartment") {{
										addAttributes(SubElementFactory.createInteriorAttributes());
										addAttributes(
												att("hitPoints", WHOLE),
												att("posX", INTEGER),
												att("posY", INTEGER),
												att("sizeX", INTEGER),
												att("sizeY", INTEGER)
												);
									}};
								}, () -> {
									return new DesignElement("Section") {{
										addAttributes(SubElementFactory.createInteriorAttributes());
										addAttributes(SubElementFactory.createShipEffectAttributes());
										addAttributes(
												att("class", TYPE_SHIPCLASS),
												att("attachTo", STRING)
												);
										
									}};
								}
								);
						
					}}, new DesignElement("Maneuver") {{
						addAttributes(
								att("rotationCount", WHOLE, "20"),
								att("maxRotationRate", DOUBLE),
								att("rotationAccel", DOUBLE),
								att("rotationStopAccel", DOUBLE)
								);
					}},
					new DesignElement("PlayerSettings") {{
						addAttributes(
								att("desc", STRING),
								att("largeImage", TYPE_IMAGE),
								att("debugOnly", BOOLEAN),
								att("initialClass", ValueType.INITIAL_CLASS),
								att("sortOrder", WHOLE_100),
								att("ui", ValueType.SHIP_UI),
								att("startingCredits", STRING),
								att("startingMap", TYPE_SYSTEM_MAP),
								att("startingSystem", STRING),
								att("startingPos", STRING, "Start"),
								att("shipScreen", TYPE_DOCKSCREEN, "&dsShipInterior;"),
								att("dockServicesScreen", TYPE_DOCKSCREEN),
								att("shipConfigScreen", TYPE_DOCKSCREEN)
								);
						addOptionalSingleSubElements(
								new DesignElement("DockScreenDisplay") {{
									addAttributes(
											att("backgroundImage", TYPE_IMAGE),
											att("contentMask", TYPE_IMAGE),
											att("textColor", HEX_COLOR),
											att("textBackgroundColor", HEX_COLOR),
											att("titleBackgroundColor", HEX_COLOR),
											att("titleTextColor", HEX_COLOR)
											);
								}}, new DesignElement("ArmorDisplay") {{
									addAttributes(att("style", ValueType.HUD_STYLE, "circular"));
									//style="circular" or style="rectangular"
									addAttributes(
											att("armorColor", HEX_COLOR),
											att("shieldsColor", HEX_COLOR)
											);
									addSubElements(
											//style="default"
											new DesignElement("ShipImage") {{
												addAttributes(SubElementFactory.createImageDescAttributes());
											}}
											);
									addOptionalMultipleSubElements(() -> {
										//style="default"
										return new DesignElement("ArmorSection") {{
											addAttributes(SubElementFactory.createImageDescAttributes());
											addAttributes(
													att("name", STRING),
													att("destX", INTEGER),
													att("destY", INTEGER),
													att("hpX", INTEGER),
													att("hpY", INTEGER),
													att("nameY", INTEGER),
													att("nameBreakWidth", INTEGER),
													att("nameDestX", INTEGER),
													att("nameDestY", INTEGER)
													);
										}};
									});
								}},
								new DesignElement("ReactorDisplay") {{
									addAttributes(att("style", ValueType.HUD_STYLE, "circular"));
									//style="circular"
									addAttributes(
											att("backgroundColor", HEX_COLOR),
											att("fuelColor", HEX_COLOR),
											att("powerColor", HEX_COLOR),
											att("powerGenColor", HEX_COLOR),
											att("warningColor", HEX_COLOR)
											);
									
									DesignElement powerLevelImage = SubElementFactory.createImageDescElement("PowerLevelImage");
									powerLevelImage.addAttributes(att("destX", INTEGER), att("destY", INTEGER));
									DesignElement powerGenImage = SubElementFactory.createImageDescElement("PowerGenImage");
									powerGenImage.addAttributes(att("destX", INTEGER), att("destY", INTEGER));
									DesignElement fuelLevelImage = SubElementFactory.createImageDescElement("FuelLevelImage");
									fuelLevelImage.addAttributes(att("destX", INTEGER), att("destY", INTEGER));
									//style="default"
									addSubElements(
											
											SubElementFactory.createImageDescElement("Image"),
											powerLevelImage,
											powerGenImage,
											fuelLevelImage,
											SubElementFactory.createImageDescElement("FuelLowLevelImage"),
											SubElementFactory.createRectangleElement("ReactorText"),
											SubElementFactory.createRectangleElement("PowerLevelText"),
											SubElementFactory.createRectangleElement("FuelLevelText")
											
											);
								}},
								new DesignElement("ShieldDisplay") {{
									addAttributes(att("shieldEffect", TYPE_EFFECT));
									addOptionalSingleSubElements(SubElementFactory.createEffect("ShieldEffect"), SubElementFactory.createImageDescElement("Image"));
									
								}},
								new DesignElement("WeaponDisplay") {{
									addAttributes(att("style", ValueType.HUD_STYLE, "circular"));
									//style="circular"
									addAttributes(att("backgroundColor", HEX_COLOR), att("targetColor", HEX_COLOR), att("weaponColor", HEX_COLOR));
									//style="default"
									addSubElements(SubElementFactory.createImageDescElement("Image"));
								}}
								);
					}},
			}));
			return result.toArray(new DesignElement[0]);
		case ShipTable:				break;
		case Sound:					break;
		case Soundtrack:
			DesignElement segments = ele("Segments");
			segments.addOptionalMultipleSubElements(() -> {
				return new DesignElement("Segment") {{
					addAttributes(att("startPos", WHOLE), att("endPos", WHOLE));
				}};
			});
			break;
		case Sovereign:
			DesignElement relationships = ele("Relationships");
				relationships.addOptionalMultipleSubElements(SovereignElements.Relationship);
			return new DesignElement[] {
					relationships
			};
		case SpaceEnvironmentType:
			image = ele("Image");
			DesignElement edgeMask = ele("EdgeMask");
			image.addAttributes(SubElementFactory.createImageDescAttributes());
			edgeMask.addAttributes(SubElementFactory.createImageDescAttributes());
			return new DesignElement[] {
					image, edgeMask
			};
		case StationType:
			DesignElement animations = ele("Animations");
				animations.addOptionalMultipleSubElements(() -> {
					DesignElement animation = ele("Animation");
					animation.addAttributes(att("x", INTEGER), att("y", INTEGER));
					animation.addAttributes(SubElementFactory.createImageDescAttributes());
					return animation;
				});
			
			DesignElement construction = ele("Construction");
				construction.addAttributes(
						att("constructionRate", WHOLE),
						att("maxConstruction", WHOLE)
						);
				construction.addOptionalMultipleSubElements(ShipGeneratorElements.values());
			
			//WIP
			DesignElement encounterGroup = ele("EncounterGroup");
			DesignElement encounterType = ele("EncounterType");
			
			DesignElement encounter = ele("Encounter");
				encounter.addAttributes(
						att("enemyExclusionRadius", WHOLE),
						att("exclusionRadius", WHOLE),
						att("levelFrequency", LEVEL_FREQUENCY),
						att("locationCriteria", STRING),
						att("maxAppearing", WHOLE),
						att("minAppearing", WHOLE),
						att("systemCriteria", STRING),
						att("unique", UNIQUE)
						);
				DesignElement criteria = ele("Criteria");
				criteria.addOptionalMultipleSubElements(
						SystemCriteria.values()
						);
				encounter.addOptionalSingleSubElements(
						criteria
						);
			DesignElement encounters = ele("Encounters");
				encounters.addAttributes(att("frequency", FREQUENCY));
				encounters.addOptionalMultipleSubElements(ShipGeneratorElements.values());
				
			//WIP, move to CompositeImageElements
			image = ele("Image");
			DesignElement imageComposite = ele("ImageComposite");
			DesignElement imageEffect = ele("ImageEffect");
			DesignElement imageLookup = ele("ImageLookup");
			DesignElement imageVariants = ele("ImageVariants");
			
			for(DesignElement i : new DesignElement[] {image, imageComposite, imageEffect, imageLookup, imageVariants}) {
				i.addAttributes(att("shipwreckID", ValueType.TYPE_SHIPCLASS)); //WIP, replace with list
			}
			
			DesignElement reinforcements = ele("Reinforcements");
				reinforcements.addAttributes(
						att("minShips", DICE_RANGE),
						att("buildReinforcements", BOOLEAN)
						);
				reinforcements.addOptionalMultipleSubElements(ShipGeneratorElements.values());
			
			DesignElement satellites = ele("Satellites");
				satellites.addAttributes(
						att("overlapCheck", OVERLAP_CHECK)
						);
				satellites.addOptionalMultipleSubElements(SystemGroupElements.values());
			
			DesignElement ships = ele("Ships");
				ships.addAttributes(
						att("challenge", DICE_RANGE),
						att("standingCount", DICE_RANGE),
						att("minShips", DICE_RANGE),
						att("buildReinforcements", BOOLEAN)
						);
				ships.addOptionalMultipleSubElements(ShipGeneratorElements.values());
			result = new ArrayList<DesignElement>(Arrays.asList(animations,
					construction,
					encounterGroup,
					encounterType,
					encounter,
					encounters,
					imageComposite,
					imageEffect,
					imageLookup,
					imageVariants,
					reinforcements,
					satellites,
					ships));
			result.addAll(Arrays.asList(SpaceObject.createSpaceObjectSubElements(t)));
			return result.toArray(new DesignElement[0]);
		case SystemMap:				break;
		case SystemPartTable:			break;
		case SystemType:
			return new DesignElement[] {
					new DesignElement("SystemGroup") {{
						addOptionalMultipleSubElements(SystemGroupElements.values());
					}}
			};
		case TemplateType:			break;
		case Type:					break;
		default:					break;
		}
		return new DesignElement[] {
		};
	}
}
