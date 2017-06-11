package mod;

import designType.TypeFactory;
import designType.TypeFactory.Types;
import designType.subElements.SubElementFactory.ExtensionElements;
import designType.subElements.SubElementType;
import xml.Attribute;
import xml.DesignElement;
import xml.Attribute.ValueType;

public class ExtensionFactory {
	//Done
	public static enum Extensions implements SubElementType {
		TranscendenceAdventure,
		TranscendenceExtension,
		TranscendenceLibrary,
		TranscendenceModule,
		;

		@Override
		public TranscendenceMod create() {
			TranscendenceMod e = new TranscendenceMod(name());
			switch(this) {
			//TranscendenceAdventure is an extension with an AdventureDesc
			case TranscendenceAdventure:
				e.addSubElements(
						TypeFactory.Types.AdventureDesc.create()
						);
			case TranscendenceExtension:
			case TranscendenceLibrary:
				e.addAttributes(
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
				
				e.addOptionalMultipleSubElements(ExtensionElements.Module, ExtensionElements.Library);
				break;
			//TranscendenceModule is an extension with only DesignTypes
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
			e.addOptionalSingleSubElements(new DesignElement("Globals"));
			for(Types t : Types.values()) {
				if(!t.equals(Types.AdventureDesc)) {
					e.addOptionalMultipleSubElements(t);
				}
			}
			return e;
		}
	}
}
