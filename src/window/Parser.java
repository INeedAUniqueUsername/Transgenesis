package window;

import java.io.IOException;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import xml.Element;

public class Parser extends DefaultHandler implements EntityResolver {
	LinkedList<Element> stack;
	public void startDocument() throws SAXException {
		stack = new LinkedList<Element>();
	}
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		Element e = new Element(localName);
		if(stack.size() > 0) {
			stack.getLast().addSubElements(e);
		}
		String tabs_element = tabs(stack.size());
		String tabs_attribute = tabs_element + "\t";
		System.out.println(tabs_element + "<" + localName);
		for(int i = 0; i < atts.getLength(); i++) {
			System.out.println(tabs_attribute + atts.getLocalName(i) + "=\"" + atts.getValue(i) + "\"");
		}
		System.out.println(tabs_attribute + ">");
		stack.add(e);
	}
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("</" + stack.removeLast().getName() + ">");
	}
	public void endDocument() throws SAXException {
		String a = "¬";
		char b = '\u5555';
	}
	
	public static String tabs(int count) {
		String result = "";
		for(int i = 0; i < count; i++) {
			result += '\t';
		}
		return result;
	}
	
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		return new InputSource();
	}
}
