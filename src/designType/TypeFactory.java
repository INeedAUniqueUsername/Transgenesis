package designType;

import designType.subElements.SingleSubElementFactory;
import designType.subElements.SingleSubElementFactory.DataElements;
import designType.subElements.SubElementFactory.DisplayAttributesElements;
import designType.subElements.SubElementFactory.DockScreensElements;
import xml.DesignAttribute;
import xml.DesignElement;
import static xml.DesignAttribute.ValueType;
import static xml.DesignAttribute.*;
public final class TypeFactory {
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
		
		e.addAttributes(
				att("UNID", ValueType.UNID),
				att("attributes", ValueType.STRING),
				att("inherit", ValueType.TYPE_INHERITED),
				att("extends", ValueType.STRING)
				);
		DesignElement dockScreens = new DesignElement("DockScreens");
		dockScreens.addOptionalMultipleSubElements(DockScreensElements.DockScreen_Named);
		DesignElement displayAttributes = new DesignElement("DisplayAttributes");
		displayAttributes.addOptionalMultipleSubElements(DisplayAttributesElements.ItemAttribute);
		DesignElement attributeDesc = new DesignElement("AttributeDesc");
		attributeDesc.addOptionalMultipleSubElements(DisplayAttributesElements.ItemAttribute);
		e.addOptionalSingleSubElements(
				DataElements.StaticData.get(),
				DataElements.GlobalData.get(),
				SingleSubElementFactory.createLanguage(t),
				SingleSubElementFactory.createEvents(t),
				dockScreens,
				displayAttributes,
				attributeDesc
				);
		
		return e;
	}
}
