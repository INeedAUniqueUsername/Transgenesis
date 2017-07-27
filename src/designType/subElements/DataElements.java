package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.STRING;

import xml.DesignElement;
import xml.RenameableElement;

//Done
public enum DataElements implements ElementType {
	StaticData,
	GlobalData,
	InitialData
	;
	@Override
	public DesignElement get() {
		DesignElement e = new DesignElement(name());
		e.addOptionalMultipleSubElements(() -> {
			DesignElement data = new RenameableElement("Data");
			data.addAttributes(
					att("id", STRING),
					att("data", STRING)
					);
			return data;
		});
		return e;
	}
}