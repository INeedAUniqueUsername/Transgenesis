package mod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

import xml.DesignAttribute;
import xml.DesignElementOld;
public class TranscendenceMod extends DesignElementOld {
	private TreeMap<String, String> unid_index;
	private File path;
	public TranscendenceMod(String name) {
		super(name);
		unid_index = new TreeMap<String, String>();
		path = null;
	}
	public void addUNID(String unid, String entity) {
		unid_index.put(unid, entity);
	}
	public void setUNIDMap(TreeMap<String, String> unid_map) {
		unid_index = unid_map;
	}
	public String getXMLOutput() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" + super.getXMLOutput();
	}
	public String getDisplayName() {
		for(DesignAttribute a : new DesignAttribute[] {getAttributeByName("name"), getAttributeByName("UNID")}) {
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
	public void save() {
		System.out.println("File saved!");
		path.delete();
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(getXMLOutput());
			bw.close();
			fw.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}
}
