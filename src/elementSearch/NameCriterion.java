package elementSearch;

import xml.DesignElement;

public class NameCriterion implements IElementCriterion {
	final String name;
	public NameCriterion(String name) {
		this.name = name;
	}
	@Override
	public boolean elementMatches(DesignElement e) {
		return name.equals(e.getName());
	}
}