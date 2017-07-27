package window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Consumer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.InputSource;

import designType.subElements.ElementType;
import designType.Types;
import designType.subElements.Language;
import designType.subElements.SubElementFactory;
import mod.ExtensionFactory.Extensions;
import mod.ExtensionFactory;
import mod.TranscendenceMod;
import panels.TypeManager;
import xml.DesignElement;
import xml.DesignAttribute;
import xml.DesignAttribute.ValueType;

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
		TranscendenceMod mod = null;	//Current mod
		System.out.println("Processing: " + path.getAbsolutePath());
		try {
			System.out.println("Beginning Read");
			byte[] bytes = Files.readAllBytes(path.toPath());
			String lines = new String(bytes);
			lines = lines.replace("&", "&amp;");
			bytes = lines.getBytes();
			//String lines = new String(bytes, Charset.defaultCharset());
			
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
			inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
			inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
			
			XMLEventReader reader = inputFactory.createXMLEventReader(new ByteArrayInputStream(bytes));
			
			LinkedList<DesignElement> elementStack = new LinkedList<DesignElement>();	//The last one is the current element we are looking at
			Types category = null;			//Current category of DesignType
			Read: while (reader.hasNext()) {
			    XMLEvent event = reader.nextEvent();
			    /*
			    switch(event.getEventType()) {
			    case XMLEvent.START_ELEMENT:
		            System.out.println("START_ELEMENT");
		            System.out.println(event.asStartElement().getName().getLocalPart());
		            break;
		        case XMLEvent.END_ELEMENT:
		        	System.out.println("END_ELEMENT");
		        	break;
		        case XMLEvent.START_DOCUMENT:
		        	System.out.println("START_DOCUMENT");
		        	break;

		        case XMLEvent.END_DOCUMENT:
		        	System.out.println("END_DOCUMENT");
		        	break;
			    }
			    if(true) {
			    	continue Read;
			    }
			    */
			    EventType: switch(event.getEventType()) {
			    case XMLEvent.ENTITY_DECLARATION:
			    	EntityDeclaration b = (EntityDeclaration) event;
			    	//unids.createFromXML(b);
			    	//unid_map.put(b.getName(), b.getReplacementText());
			    	System.out.println("Entity: " + b.getName() + "=" + b.getReplacementText());
			    	break;
			    case XMLEvent.START_ELEMENT:
			    	StartElement start = event.asStartElement();
			    	String name = start.getName().getLocalPart();
			    	System.out.println("Element name: " + name);
			    	Consumer<DesignElement> addAttributes =((DesignElement e) -> {
			    		DesignElement element = elementStack.getLast();
				    	//Now add all the attributes
				    	Iterator<Attribute> attributes = start.getAttributes();
				    	while(attributes.hasNext()) {
				    		Attribute a = attributes.next();
				    		System.out.println("Attribute found: " + a.getName().getLocalPart() + "=" + a.getValue());
				    		element.setAttribute(a.getName().getLocalPart(), a.getValue());
				    	}
			    	});
			    	//Check if we have an extension (if we do not already have one, then it's just an element)
			    	if(mod == null) {
			    		try {
				    		Extensions result = Extensions.valueOf(name);
				    		mod = result.get();
				    		mod.setPath(path);
				    		elementStack.addLast(mod);
				    		addAttributes.accept(mod);
				    		System.out.println("Extension Found");
				    		continue Read;
				    	} catch(Exception e) { System.out.println("Not an extension"); }
			    	}
			    	try {
			    		//Check if we have a DesignType
				    	Types result = Types.valueOf(name);
			    		DesignElement element = result.get();
			    		elementStack.getLast().addSubElements(element);
			    		elementStack.addLast(element);
			    		addAttributes.accept(element);
			    		category = result;
			    		System.out.println("DesignType Found");
			    		continue Read;
			    	} catch(Exception e) { System.out.println("Not a DesignType"); }
			    	
			    	//Otherwise, assume that we are making a DesignType and currently looking at a subelement
		    		if(elementStack.size() == 0) {
		    			System.out.println("Skipping Start: First element is unrecognized");
		    			break EventType;
		    		}
		    		DesignElement element = elementStack.getLast();
		    		if(element == null) {
		    			System.out.println("Null Element Found");
		    			break EventType;
		    		}
		    		//If we have a Language subelement, then we need to identify it by the id= attribute
		    		DesignElement add = null;
		    		if(name.equals("Text")) {
		    			Attribute id = start.getAttributeByName(new QName("id"));
		    			if(id != null) {
		    				add = element.getAddableElement(
			    					name,
			    					new DesignAttribute(
			    							"id",
			    							ValueType.STRING,
			    							id.getValue()
			    							)
			    					);
			    			if(add == null) {
			    				add = Language.createText(id.getValue());
			    			}
		    			}
		    		} else {
		    			add = element.getAddableElement(name);
		    		}
		    		if(add == null) {
		    			System.out.println("Adding generic element: " + name);
		    			add = new DesignElement(name);
		    		} else {
		    			System.out.println("Adding identified element: " + name);
		    		}
		    		
		    		element.addSubElements(add);
		    		elementStack.addLast(add);
		    		addAttributes.accept(add);
			    	break EventType;
			    case XMLEvent.END_ELEMENT:
			    	//This means that the first element was not recognized
			    	if(elementStack.size() == 0) {
			    		System.out.println("Skipping End: First element is unrecognized");
			    	} else {
			    		elementStack.removeLast().finalizeLoad();
			    	}
			    	break EventType;
			    case XMLEvent.CHARACTERS:
			    	if(elementStack.size() > 0) {
			    		elementStack.getLast().appendText(((Characters) event).getData());
			    	}
			    	break;
			    case XMLEvent.COMMENT:
			    	if(elementStack.size() > 0) {
			    		elementStack.getLast().appendText("<!--" + ((Comment) event).getText() + "-->");
			    	}
			    	
			    	break;
			    }
			    
			}
			if(mod != null) {
				mod.setUNIDs(new TypeManager(path));
			}
		} catch (IOException | XMLStreamException e) {
			System.out.println("Encountered error; could not load " + mod.getPath());
			mod = new TranscendenceMod("TranscendenceError");
			mod.setPath(path);
			e.printStackTrace();
		}
		return mod;
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