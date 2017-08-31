package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.STRING;

import xml.DesignElement;
import xml.RenameableElement;

//Done
public enum DataElements implements ElementType {
	Data,
	StaticData,
	GlobalData,
	InitialData
	;
	@Override
	public DesignElement get() {
		DesignElement e;
		switch(this) {
		case Data:
			e = new RenameableElement("Data");
			e.addAttributes(
					att("id", STRING),
					att("data", STRING)
					);
			break;
		default:
			e = new DesignElement(name());
			e.addOptionalMultipleSubElements(Data);
			break;
		}
		
		return e;
	}
}