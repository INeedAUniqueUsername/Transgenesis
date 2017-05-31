package mod;

import java.io.File;
import java.util.TreeMap;

import xml.Attribute;
import xml.Element;
public class TranscendenceMod extends Element {
	TreeMap<String, String> unid_index;
	public TranscendenceMod(String name) {
		super(name);
	}
	
	public String getDisplayName() {
		String result = super.getDisplayName();
		for(Attribute a : new Attribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
			if(a == null) {
				continue;
			}
			String name = a.getValue();
			if(name != null && !name.equals("")) {
				result = name;
			}
		}
		return result;
	}
}
