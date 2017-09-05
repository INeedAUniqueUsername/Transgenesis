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
			e = getDataBlock(name());
			break;
		}
		
		return e;
	}
	public static DesignElement getDataBlock(String name) {
		return new DesignElement(name) {{
			addOptionalMultipleSubElements(Data);
		}};
	}
}