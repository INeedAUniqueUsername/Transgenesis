package designType.subElements;

import designType.DesignElement;

public class Events_OverlayType extends DesignElement {
	public Events_OverlayType() {
		super("Events");
		addSubElements(
				new DesignElement("OnCreate"),
				new DesignElement("OnUpdate"),
				new DesignElement("OnDamage"),
				new DesignElement("OnDestroy"),
				new DesignElement("OnObjDestroyed")
				);
	}
}
