package elementSearch;

import xml.DesignAttribute;
import xml.DesignElement;

public class AttributeCriterion implements IElementCriterion {

	private String name, value;
	public AttributeCriterion(String name, String value) {
		this.name = name;
		this.value = value;
	}
	@Override
	public boolean elementMatches(DesignElement e) {
		for(DesignAttribute a : e.getAttributes()) {
			if(a.getName().equals(name) && a.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}
}