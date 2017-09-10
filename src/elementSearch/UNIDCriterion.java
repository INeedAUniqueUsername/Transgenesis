package elementSearch;

import xml.DesignAttribute;
import xml.DesignElement;

public class UNIDCriterion implements IElementCriterion {
	final String unid;
	public UNIDCriterion(String unid) {
		this.unid = unid;
	}
	@Override
	public boolean elementMatches(DesignElement e) {
		DesignAttribute attribute = e.getAttributeByName("unid");
		return attribute != null && attribute.getValue().equals(unid);
	}
}
