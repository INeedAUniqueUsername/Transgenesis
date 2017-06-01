package mod;

import designType.TypeFactory;
import designType.subElements.SubElement;
import designType.subElements.SubElementFactory.SubElements;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class ExtensionFactory {
	public static enum Extensions implements SubElement {
		TranscendenceAdventure,
		TranscendenceExtension,
		TranscendenceModule,
		TranscendenceLibrary
		;

		@Override
		public TranscendenceMod create() {
			// TODO Auto-generated method stub
			return createExtension(this);
		}
	}
	public static TranscendenceMod createExtension(Extensions t) {
		TranscendenceMod e = null;
		switch(t) {
		case TranscendenceAdventure:
			e = new TranscendenceMod("TranscendenceAdventure");
			e.addRequiredSingleSubElements(
					TypeFactory.Types.AdventureDesc.create()
					);
			break;
		case TranscendenceLibrary:
			e = new TranscendenceMod("TranscendenceLibrary");
			break;
		case TranscendenceExtension:
			e = new TranscendenceMod("TranscendenceExtension");
			break;
		case TranscendenceModule:
			e = new TranscendenceMod("TranscendenceModule");
			break;
		default:
			try {
				throw new Exception("Not an Extension");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
		e.addRequiredAttributes(
				new Attribute("apiVersion", ValueType.INTEGER, ""),
				new Attribute("autoInclude", ValueType.BOOLEAN, "false"),
				new Attribute("autoIncludeForCompatibility", ValueType.BOOLEAN, "false"),
				new Attribute("coverImageID", ValueType.TYPE_IMAGE, ""),
				new Attribute("credits", ValueType.STRING, ""),
				new Attribute("debugOnly", ValueType.BOOLEAN, "false"),
				new Attribute("extends", ValueType.TYPE_MOD, ""),
				new Attribute("extensionAPIVersion", ValueType.INTEGER, ""),
				new Attribute("hidden", ValueType.BOOLEAN, "false"),
				new Attribute("name", ValueType.STRING, ""),
				new Attribute("private", ValueType.BOOLEAN, "false"),
				new Attribute("release", ValueType.INTEGER, "1"),
				new Attribute("UNID", ValueType.UNID, ""),
				new Attribute("usesXML", ValueType.BOOLEAN, "false"),
				new Attribute("version", ValueType.STRING, "1.0")
				);
		e.addOptionalSingleSubElements(new Element("Globals"));
		e.addOptionalMultipleSubElements(
				TypeFactory.Types.values()
				);
		
		e.addOptionalMultipleSubElements(SubElements.Module, SubElements.Library);
		return e;
	}
}
