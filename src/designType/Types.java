package designType;

import designType.subElements.SubElementType;
import xml.DesignElementOld;

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
public enum Types implements SubElementType {
	AdventureDesc,
	DockScreen,
	EconomyType,
	EffectType,
	Image,
	ImageComposite,
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
	public DesignElementOld get() {
		return TypeFactory.createDesignType(this);
	}
}