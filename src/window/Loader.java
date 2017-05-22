package window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import mod.TranscendenceMod;

public class Loader {
	public static List<TranscendenceMod> loadAllMods(File path) {
		List<TranscendenceMod> result = new ArrayList<TranscendenceMod>();
		if(path.isDirectory()) {
			for(File f : path.listFiles()) {
				result.addAll(loadAllMods(f));
			}
		} else if(path.isFile() && path.getPath().endsWith(".xml")) {
			result.add(processMod(path));
		}
		return result;
	}
	public static TranscendenceMod processMod(File path) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder(); 
			Document doc = db.parse(path);
		} catch(Exception e) {
			
		}
		return null;
		
	}
}
