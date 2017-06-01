package mod;

import java.io.File;
import java.util.TreeMap;

import xml.Attribute;
import xml.Element;
public class TranscendenceMod extends Element {
	TreeMap<String, String> unid_index;
	File path;
	public TranscendenceMod(String name) {
		super(name);
		path = null;
	}
	
	public String getDisplayName() {
		for(Attribute a : new Attribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
			if(a == null) {
				continue;
			}
			String name = a.getValue();
			if(name != null && !name.equals("")) {
				return name;
			}
		}
		if(path != null) {
			return path.getName();
		}
		return super.getDisplayName();
	}

	public void setPath(File absolutePath) {
		// TODO Auto-generated method stub
		path = absolutePath;
	}
}
