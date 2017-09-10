package elementSearch;

import java.util.LinkedList;

import xml.DesignElement;

public interface IElementCriterion {
	public boolean elementMatches(DesignElement e);
	//This keeps individual element criteria in one piece
	public static String[] splitElementTerms(String query) {
		LinkedList<StringBuilder> terms = new LinkedList<>();
		boolean insideElement = false;
		for(char c : query.toCharArray()) {
			if(insideElement) {
				terms.getLast().append(c);
			}
			if(c == '<') {
				insideElement = true;
				terms.addLast(new StringBuilder("<"));
			} else if(c == '>') {
				insideElement = false;
			}
		}
		int count = terms.size();
		String[] result = new String[count];
		for(int i = 0; i < count; i++) {
			result[i] = terms.get(i).toString();
		}
		return result;
	}
	//
	public static String[] splitElementSubTerms(String query) {
		LinkedList<StringBuilder> terms = new LinkedList<>();
		terms.add(new StringBuilder());
		boolean insideQuote = false;
		boolean escapeQuote = false;
		for(char c : query.toCharArray()) {
			if(insideQuote) {
				terms.getLast().append(c);
				if(c == '\\') {
					escapeQuote = true;
					continue;
				} else if(c == '\"') {
					if(!escapeQuote) {
						insideQuote = false;
					}
				}
				escapeQuote = false;
			} else {
				if(c == '\"') {
					insideQuote = true;
					terms.getLast().append(c);
				}
				else if(c == ' ') {
					terms.addLast(new StringBuilder());
				} else {
					terms.getLast().append(c);
				}
			}
		}
		int count = terms.size();
		String[] result = new String[count];
		for(int i = 0; i < count; i++) {
			result[i] = terms.get(i).toString();
		}
		return result;
	}
	public static void parseQuery(String query) {
		String[] terms = query.split(" ");
		LinkedList<ElementCriterion> stack = new LinkedList<>();
		stack.add(new ElementCriterion());
		for(String s : terms) {
			ElementCriterion last = stack.getLast();
			if(s.startsWith("<") && s.endsWith("/>")) {
				//Check if this is empty, in which case ignore
				if(s.replaceAll(" ", "").length() == 3) {
					continue;
				} else {
					//Trim off the brackets, then split into terms
					String[] subterms = s.substring(1, s.length() - 2).split(" ");
					if(subterms.length > 0) {
						//First subterm of a single element criterion is the element name
						last.addSubCriteria(new NameCriterion(subterms[0]));
						//Expect the rest of the subterms to be attribute criteria
						for(int i = 1; i < subterms.length - 1; i++) {
							String attributeTerm = subterms[i];
							String[] attributeSubTerms = attributeTerm.split("=");
							String name = attributeSubTerms[0];
							String value = attributeSubTerms[1];
							//Assume that the value contains quotes
							last.addSubCriteria(new AttributeCriterion(attributeSubTerms[0], attributeSubTerms[1]));
						}
						
					}
				}
			}
		}
	}
}