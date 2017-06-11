package designType;

import designType.subElements.SubElementType;
import designType.subElements.SingleSubElementFactory;
import designType.subElements.SingleSubElementFactory.DataElements;
import xml.Attribute;
import xml.DesignElement;
import xml.Attribute.ValueType;
public final class TypeFactory {
	//@Override
	/*
	public int compareTo(Object arg0) {
		if(arg0 instanceof Type) {
			int unid = Integer.valueOf(getAttributeByName("UNID").getValue());
			int unid_other = Integer.valueOf(((Type) arg0).getAttributeByName("UNID").getValue());
			if(unid > unid_other) {
				return 1;
			} else if(unid < unid_other) {
				return -1;
			}
		}
		return 0;
	}
	*/
	public static enum Types implements SubElementType {
		AdventureDesc,
		DockScreen,
		EconomyType,
		EffectType,
		Image,
		ItemTable,
		ItemType,
		MissionType,
		NameGenerator,
		OverlayType,
		Power,
		ShipClass,
		ShipClassOverride,
		ShipTable,
		Sound,
		Sovereign,
		SpaceEnvironmentType,
		StationType,
		SystemMap,
		SystemTable,
		SystemType,
		TemplateType,
		Type,
		;
		
		@Override
		public DesignElement create() {
			return createDesignType(this);
		}
	}
	//\#define ([A-Z]*_*)*\s+CONSTLIT\(\"(.+)\"\)
	public static DesignElement createDesignType(Types t) {
		DesignElement e = new Type(t.name());
		e.addAttributes(AttributeFactory.createAttributesForType(t));
		e.addOptionalSingleSubElements(
				SingleSubElementFactory.createSingleSubElementsForType(t)
				);
		switch(t) {
		case SpaceEnvironmentType:
			//e.addSubElements(SubElementFactory.createSingleSubElementsForType(MultipleSubElements.Image));
			//e.addOptionalSingleSubElements(SubElementFactory.createSingleSubElementsForType(MultipleSubElements.EdgeMask));
			break;
		case StationType:
			System.out.println("Replace ValueType for each attribute with the correct one.");
			//<Image>
			//new Attribute("shipwreckID", ValueType.STRING),
			//<Construction>
			//new Attribute("maxConstruction", ValueType.INTEGER),
			//<Ships>
			//new Attribute("standingCount", ValueType.STRING), new Attribute("buildReinforcements", ValueType.STRING),
			//<Station>
			//new Attribute("type", ValueType.STRING),
			e.addOptionalSingleSubElements(
					/*
					MultipleSubElements.Animations.create(),
					MultipleSubElements.Communications.create(),
					MultipleSubElements.ImageComposite.create(),
					MultipleSubElements.Construction.create(),
					MultipleSubElements.Devices.create(),
					MultipleSubElements.DockingPorts.create(),
					MultipleSubElements.DockScreens.create(),
					MultipleSubElements.EncounterGroup.create(),
					MultipleSubElements.EncounterType.create(),
					MultipleSubElements.Encounters.create(),
					MultipleSubElements.Events.create(),
					MultipleSubElements.HeroImage.create(),
					MultipleSubElements.Image.create(),
					MultipleSubElements.ImageEffect.create(),
					MultipleSubElements.ImageLookup.create(),
					MultipleSubElements.ImageVariants.create(),
					MultipleSubElements.Items.create(),
					MultipleSubElements.Names.create(),
					MultipleSubElements.Reinforcements.create(),
					MultipleSubElements.Satellites.create(),
					MultipleSubElements.Ship.create(),
					MultipleSubElements.Ships.create(),
					MultipleSubElements.Station.create(),
					MultipleSubElements.Table.create(),
					MultipleSubElements.Trade.create()
					*/
					);
			break;
		}
		
		
		
		e.addAttributes(new Attribute("UNID", ValueType.UNID, ""));
		e.addAttributes(
				new Attribute("attributes", ValueType.STRING, ""),
				new Attribute("inherit", ValueType.TYPE_INHERITED, "")
				);
		//All DesignTypes can have Language and Events
		e.addOptionalSingleSubElements(
				SingleSubElementFactory.createLanguage(t),
				SingleSubElementFactory.createEvents(t),
				DataElements.StaticData.create(),
				DataElements.GlobalData.create()
				);
		
		return e;
	}
}
