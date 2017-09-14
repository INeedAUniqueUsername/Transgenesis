package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.INTEGER;
import static xml.DesignAttribute.ValueType.STRING;
import static xml.DesignAttribute.ValueType.WHOLE;

import designType.Types;
import designType.subElements.SubElementFactory.DeviceGeneratorElements;
import designType.subElements.SubElementFactory.ItemGeneratorElements;
import designType.subElements.SubElementFactory.TradeElements;
import xml.DesignAttribute.ValueType;
import xml.DesignElement;

public final class SpaceObject {
	private SpaceObject() {}
	public static DesignElement createNamesElement() {
		return new DesignElement("Names") {{
			addAttributes(SubElementFactory.createNameAttributes());
		}};
	}
	enum SpaceObjectElements implements ElementType {
		Names,
		Items;
		public DesignElement get() {
			DesignElement e = new DesignElement(name());
			switch(this) {
			case Names:
				e.addAttributes(
						SubElementFactory.createNameAttributes()
						);
				break;
			case Items:
				e.addOptionalMultipleSubElements(ItemGeneratorElements.values());
				break;
			}
			return e;
		}
	}
	public static DesignElement[] createSpaceObjectSubElements(Types t) {
		DesignElement names = new DesignElement("Names");
		names.addAttributes(
				SubElementFactory.createNameAttributes()
				);
		DesignElement items = new DesignElement("Items");
		items.addOptionalMultipleSubElements(ItemGeneratorElements.values());
		DesignElement devices = new DesignElement("Devices");
		devices.addOptionalMultipleSubElements(
				DeviceGeneratorElements.values()
				);
		
		/*
		DesignElement image = new DesignElement("Image");
		image.addAttributes(SubElementFactory.createImageDescAttributes());
		if(t == Types.StationType) {
			image.addAttributes(att("shipwreckID", ValueType.TYPE_SHIPCLASS)); //WIP, replace with list
		}
		*/
		
		
		
		DesignElement heroImage = new DesignElement("HeroImage");
		heroImage.addAttributes(SubElementFactory.createImageDescAttributes());
		
		DesignElement initialData = DataElements.InitialData.get();
		
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
				att("y", INTEGER),
				att("posAngle", INTEGER),
				att("posRadius", INTEGER),
				att("posZ", INTEGER)
				);
		DesignElement trade = new DesignElement("Trade");
		trade.addAttributes(
				att("currency", STRING),
				att("creditConversion", WHOLE),
				att("max", WHOLE),
				att("replenish", WHOLE)
				);
		trade.addOptionalMultipleSubElements(TradeElements.values());
		
		return new DesignElement[] {
				names, items, devices, /*image,*/ heroImage, initialData, dockingPorts, trade
		};
	}
}
