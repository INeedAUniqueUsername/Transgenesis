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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.InputSource;

import designType.subElements.SubElementType;
import designType.Types;
import designType.subElements.SubElementFactory;
import mod.ExtensionFactory.Extensions;
import mod.ExtensionFactory;
import mod.TranscendenceMod;
import xml.DesignElement;

public class Loader {
	private static final String AMPERSAND_PLACEHOLDER;
	static {
		SecureRandom random = new SecureRandom();
		AMPERSAND_PLACEHOLDER = new BigInteger(130, random).toString(32);
	}
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
			
			inputFactory.setXMLResolver(new XMLResolver() {

				@Override
				public Object resolveEntity(String arg0, String arg1, String arg2, String arg3)
						throws XMLStreamException {
					// TODO Auto-generated method stub
					System.exit(0);
					int a = 1/0;
					return new InputSource();
				}
				
			});
			XMLEventReader reader = inputFactory.createXMLEventReader(new ByteArrayInputStream(bytes));
			
			LinkedList<DesignElement> elementStack = new LinkedList<DesignElement>();	//The last one is the current element we are looking at
			Types category = null;			//Current category of DesignType
			TreeMap<String, String> unid_map = new TreeMap<>();
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
			    	unid_map.put(b.getName(), b.getReplacementText());
			    	System.out.println("Entity: " + b.getName() + "=" + b.getReplacementText());
			    	System.exit(0);
			    	break;
			    case XMLEvent.START_ELEMENT:
			    	String name = event.asStartElement().getName().getLocalPart();
			    	System.out.println("Element name: " + name);
			    	Consumer<DesignElement> addAttributes =((DesignElement e) -> {
			    		DesignElement element = elementStack.getLast();
				    	//Now add all the attributes
				    	Iterator<Attribute> attributes = event.asStartElement().getAttributes();
				    	while(attributes.hasNext()) {
				    		Attribute a = attributes.next();
				    		System.out.println("Attribute found: " + a.getName().getLocalPart() + "=" + a.getValue());
				    		element.setAttribute(a.getName().getLocalPart(), a.getValue());
				    	}
			    	});
			    	//Check if we have an extension
			    	try {
			    		Extensions result = Extensions.valueOf(name);
			    		mod = result.get();
			    		mod.setPath(path);
			    		elementStack.addLast(mod);
			    		addAttributes.accept(mod);
			    		System.out.println("Extension Found");
			    		continue Read;
			    	} catch(Exception e) { System.out.println("Not an extension"); }
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
			    	
			    	//Otherwise, we have some kind of subelement for our current DesignType
			    	ElementName: switch(name) {
			    	/*
			    	case "Events":
			    		System.out.println("Events Found");
			    		Element element = SubElementFactory.createEvents(category);
			    		elementStack.add(element);
			    		elementStack.getLast().addSubElements(element);
			    		break ElementName;
			    	*/
			    	default:
			    		if(elementStack.size() == 0) {
			    			System.out.println("Skipping Start: First element is unrecognized");
			    			break ElementName;
			    		}
			    		DesignElement element = elementStack.getLast();
			    		if(element == null) {
			    			System.out.println("Null Element Found");
			    			break ElementName;
			    		}
			    		DesignElement add = element.getAddableElement(name);
			    		if(add == null) {
			    			System.out.println("Adding generic element: " + name);
			    			add = new DesignElement(name);
			    		} else {
			    			System.out.println("Adding identified element: " + name);
			    		}
			    		element.addSubElements(add);
			    		elementStack.addLast(add);
			    		addAttributes.accept(add);
			    		break ElementName;
			    	}
			    	break EventType;
			    case XMLEvent.END_ELEMENT:
			    	//This means that the first element was not recognized
			    	if(elementStack.size() == 0) {
			    		System.out.println("Skipping End: First element is unrecognized");
			    	} else {
			    		elementStack.removeLast();
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
			mod.setUNIDMap(unid_map);
			
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
			System.out.println("Encountered error; mod loading cancelled");
			mod = null;
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
