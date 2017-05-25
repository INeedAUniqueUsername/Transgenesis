package designType;

import designType.subElements.Event;

public class TemplateType extends Type {
	public TemplateType() {
		super();
		getOptionalSingleByName("Events").addOptionalSingleSubElements(
				new Event("GetTypeSource")
				);
	}
}