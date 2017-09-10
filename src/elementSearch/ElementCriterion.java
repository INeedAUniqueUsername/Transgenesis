package elementSearch;

import java.util.ArrayList;
import java.util.Arrays;

import xml.DesignElement;

public class ElementCriterion implements IElementCriterion {
	public static void main(String[] args) {
		for(String s : IElementCriterion.splitElementSubTerms("<A a=\"5 5 5\" b=\"3\">")) {
			System.out.println(s);
		}
	}
	
	ArrayList<IElementCriterion> subCriteria;
	@Override
	public boolean elementMatches(DesignElement e) {
		for(IElementCriterion c : subCriteria) {
			if(!c.elementMatches(e)) {
				return false;
			}
		}
		return true;
	}
	public void addSubCriteria(IElementCriterion... subCriteria) {
		this.subCriteria.addAll(Arrays.asList(subCriteria));
	}
}