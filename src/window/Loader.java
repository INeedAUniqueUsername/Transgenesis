package window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

import designType.TypeFactory.Types;
import designType.subElements.SubElement;
import designType.subElements.SubElementFactory;
import mod.ExtensionFactory.Extensions;
import mod.ExtensionFactory;
import mod.TranscendenceMod;
import xml.Element;

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
			byte[] bytes = Files.readAllBytes(path.toPath());
			//String lines = new String(bytes, Charset.defaultCharset());
			
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
			XMLEventReader reader = inputFactory.createXMLEventReader(new ByteArrayInputStream(bytes));
			
			TranscendenceMod mod = null;	//Current mod
			Element element = null;			//Current element we are looking at
			Types category = null;			//Current category of DesignType
			while (reader.hasNext()) {
			    XMLEvent event = reader.nextEvent();
			    switch(event.getEventType()) {
			    case XMLEvent.START_ELEMENT:
			    	String name = event.asStartElement().getName().getLocalPart();
			    	
			    	//Check if we have an extension
			    	try {
			    		Extensions result = Extensions.valueOf(name);
			    		mod = result.create();
			    		continue;
			    	} catch(Exception e) {}
			    	try {
			    		//Check if we have a DesignType
				    	Types result = Types.valueOf(name);
				    	if(result != null) {
				    		element = result.create();
				    		category = result;
				    		continue;
				    	}
			    	} catch(Exception e) {}
			    	
			    	
			    	//Otherwise, we have some kind of subelement for our current DesignType
			    	switch(name) {
			    	//Note: category cannot equal null at this point because extensions cannot have Events
			    	case "Events":
			    		SubElementFactory.createEvents(category);
			    		break;
			    	}
			    	break;
			    //We have an attribute for our current element, which is definitely not null
			    case XMLEvent.ATTRIBUTE:
			    	Attribute a = ((Attribute) event);
			    	element.setAttribute(a.getName().getLocalPart(), a.getValue());
			    	break;
			    }
			}
			
			/*
			lines = lines.replaceAll("&", "AMPERSAND");
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    spf.setValidating(false);
		    SAXParser saxParser = spf.newSAXParser();
		    XMLReader xmlReader = saxParser.getXMLReader();
		    
		    xmlReader.setContentHandler(new Parser());
		    //xmlReader.parse(convertToFileURL(path.getAbsolutePath()));
		    xmlReader.parse(new InputSource(new StringReader(lines)));
		    */
		} catch (IOException | XMLStreamException e) {
			//e.printStackTrace();
		}
		return null;
		/*
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
		*/
	}
	private static String convertToFileURL(String absolutePath) {
        if (File.separatorChar != '/') {
            absolutePath = absolutePath.replace(File.separatorChar, '/');
        }

        if (!absolutePath.startsWith("/")) {
        	absolutePath = "/" + absolutePath;
        }
        return "file:" + absolutePath;
    }
	
	
	/*
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
	*/
	
	/*
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
	*/
}
