package designType.subElements;

import subelements.CodeBlock;
import xml.Element;

public class Events_OverlayType extends Element {
	public Events_OverlayType() {
		super("Events");
		addSubElements(
				new CodeBlock("OnCreate"),
				new CodeBlock("OnUpdate"),
				new CodeBlock("OnDamage"),
				new CodeBlock("OnDestroy"),
				new CodeBlock("OnObjDestroyed")
				);
	}
}
