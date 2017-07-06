package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.INTEGER;
import static xml.DesignAttribute.ValueType.STRING;
import static xml.DesignAttribute.ValueType.WHOLE;

import designType.Types;
import designType.subElements.SubElementFactory.DeviceTableElements;
import designType.subElements.SubElementFactory.ItemGeneratorElements;
import designType.subElements.SubElementFactory.TradeElements;
import xml.DesignAttribute.ValueType;
import xml.DesignElementOld;

public final class SpaceObject {
	private SpaceObject() {}

	public static DesignElementOld[] createSpaceObjectSubElements(Types t) {
		DesignElementOld names = new DesignElementOld("Names");
		names.addAttributes(
				SubElementFactory.createNameAttributes()
				);
		DesignElementOld items = new DesignElementOld("Items");
		items.addOptionalMultipleSubElements(ItemGeneratorElements.values());
		DesignElementOld devices = new DesignElementOld("Devices");
		devices.addOptionalMultipleSubElements(
				DeviceTableElements.values()
				);
		DesignElementOld image = new DesignElementOld("Image");
		image.addAttributes(SubElementFactory.createImageDescAttributes());
		if(t == Types.StationType) {
			image.addAttributes(att("shipwreckID", ValueType.TYPE_SHIPCLASS));
		}
		
		
		DesignElementOld heroImage = new DesignElementOld("HeroImage");
		heroImage.addAttributes(SubElementFactory.createImageDescAttributes());
		
		DesignElementOld initialData = DataElements.InitialData.get();
		
		DesignElementOld dockingPorts = new DesignElementOld("DockingPorts");
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
		DesignElementOld trade = new DesignElementOld("Trade");
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
		return new DesignElementOld[] {
				names, items, devices, image, heroImage, initialData, dockingPorts, trade
		};
	}
}
