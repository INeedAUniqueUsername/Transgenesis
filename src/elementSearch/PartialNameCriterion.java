package elementSearch;

import xml.DesignElement;

public class PartialNameCriterion extends NameCriterion {

	public PartialNameCriterion(String name) {
		super(name);
	}
	public boolean elementMatches(DesignElement e) {
		return e.getName().contains(name);
	}
}