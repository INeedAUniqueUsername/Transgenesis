package elementSearch;

import xml.DesignAttribute;
import xml.DesignElement;

public class PartialUNIDCriterion extends UNIDCriterion {
	public PartialUNIDCriterion(String unid) {
		super(unid);
	}
	@Override
	public boolean elementMatches(DesignElement e) {
		DesignAttribute attribute = e.getAttributeByName("unid");
		return attribute != null && attribute.getValue().contains(unid);
	}
}
