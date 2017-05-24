package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class DesignType extends DesignElement implements Comparable {

	public DesignType() {
		this("Type");
	}
	public DesignType(String name) {
		super(name);
		
		Attribute unid = new Attribute("UNID", ValueType.UNID, "");
		addRequiredAttributes(unid);
		addAttributes(
				new Attribute("attributes", ValueType.STRING, ""),
				new Attribute("inherit", ValueType.TYPE_INHERITED, "")
				);
	}
	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof DesignType) {
			int unid = Integer.valueOf(getAttributes().get(0).getValue());
			int unid_other = Integer.valueOf(((DesignElement) arg0).getAttributes().get(0).getValue());
			if(unid > unid_other) {
				return 1;
			} else if(unid < unid_other) {
				return -1;
			}
		}
		return 0;
	}
}
