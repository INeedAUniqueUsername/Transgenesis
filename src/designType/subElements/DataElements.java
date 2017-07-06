package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.STRING;

import xml.DesignElementOld;
import xml.RenameableElement;

//Done
public enum DataElements implements SubElementType {
	StaticData,
	GlobalData,
	InitialData
	;
	@Override
	public DesignElementOld get() {
		DesignElementOld e = new DesignElementOld(name());
		e.addOptionalMultipleSubElements(() -> {
			DesignElementOld data = new RenameableElement("Data");
			data.addAttributes(
					att("id", STRING),
					att("data", STRING)
					);
			return data;
		});
		return e;
	}
}