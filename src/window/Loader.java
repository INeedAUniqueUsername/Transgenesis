package window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mod.TranscendenceMod;

public class Loader {
	public static int successes = 0;
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
		try {
			Scanner s = new Scanner(path);
			boolean keep = false;
			for(int i = 0; i < 10; i++) {
				if(s.nextLine().contains("Library")) {
					keep = true;
				}
			}
			if(!keep) {
				return null;
			}
		} catch(Exception e) {
			return null;
		}
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			System.out.println(successes);
			byte[] bytes = Files.readAllBytes(path.toPath());
			String lines = new String(bytes, Charset.defaultCharset());
			Document mod = loadXMLFromString(lines);
			System.out.println(((Entity) mod.getDoctype().getEntities().item(0)).toString());
			successes++;
			System.out.println("Parsed " + path.getPath());
		} catch(Exception e) {
			//e.printStackTrace();
			
		}
		return null;
		
	}
	
	
	
	  public static void main(String[] argv) throws Exception {
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    factory.setValidating(false);
		    factory.setExpandEntityReferences(true);

		    Document doc = factory.newDocumentBuilder().parse(new File("C:\\Users\\Alex\\Desktop\\Transcendence Multiverse\\Sources\\Transcendence_Source\\GalaxyLibrary.xml"));

		    Map entityValues = new HashMap();
		    getEntityValues(doc, entityValues);

		    NamedNodeMap entities = doc.getDoctype().getEntities();
		    for (int i = 0; i < entities.getLength(); i++) {
		      Entity entity = (Entity) entities.item(i);
		      System.out.println(entity);
		      String entityName = entity.getNodeName();
		      System.out.println(entityName);
		      String entityPublicId = entity.getPublicId();
		      System.out.println(entityPublicId);
		      String entitySystemId = entity.getSystemId();
		      System.out.println(entitySystemId);
		      Node entityValue = (Node) entityValues.get(entityName);
		      System.out.println(entityValue);
		      System.out.println(entity.getTextContent());
		    }
		  }

		  public static void getEntityValues(Node node, Map map) {
		    if (node instanceof EntityReference) {
		      map.put(node.getNodeName(), node);
		    }
		    NodeList list = node.getChildNodes();
		    for (int i = 0; i < list.getLength(); i++) {
		      getEntityValues(list.item(i), map);
		    }
		  }
	
	
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
	    factory.setValidating(false);
	    factory.setExpandEntityReferences(false);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    builder.setEntityResolver(new EntityResolver() {
		    @Override
		        public InputSource resolveEntity(String publicId, String systemId) {
		                // it might be a good idea to insert a trace logging here that you are ignoring publicId/systemId
		                return new InputSource(new StringReader("")); // Returns a valid dummy source
		        }
		    });
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}
