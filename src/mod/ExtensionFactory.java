package mod;

import designType.Types;
import designType.subElements.SubElementFactory.ExtensionElements;
import designType.subElements.SubElementType;
import xml.DesignAttribute;
import xml.DesignElement;
import xml.DesignAttribute.ValueType;
import static xml.DesignAttribute.*;
public class ExtensionFactory {
	//Done
	public static enum Extensions implements SubElementType {
		TranscendenceAdventure,
		TranscendenceExtension,
		TranscendenceLibrary,
		TranscendenceModule,
		;

		@Override
		public TranscendenceMod get() {
			TranscendenceMod e = new TranscendenceMod(name());
			switch(this) {
			//TranscendenceAdventure is an extension with an AdventureDesc
			case TranscendenceAdventure:
				e.addSubElements(
						Types.AdventureDesc.get()
						);
			case TranscendenceExtension:
			case TranscendenceLibrary:
				e.addAttributes(
						att("apiVersion", ValueType.INTEGER, ""),
						att("autoInclude", ValueType.BOOLEAN, ""),//"false"),
						att("autoIncludeForCompatibility", ValueType.BOOLEAN, ""),//"false"),
						att("coverImageID", ValueType.TYPE_IMAGE, ""),
						att("credits", ValueType.STRING, ""),
						att("debugOnly", ValueType.BOOLEAN, ""),//"false"),
						//att("extends", ValueType.TYPE_MOD, ""),
						att("extensionAPIVersion", ValueType.INTEGER, ""),
						att("hidden", ValueType.BOOLEAN, ""),//"false"),
						att("name", ValueType.STRING, ""),
						att("private", ValueType.BOOLEAN, ""),//"false"),
						att("release", ValueType.INTEGER, ""),//"1"),
						att("UNID", ValueType.UNID, ""),
						att("usesXML", ValueType.BOOLEAN, ""),//"false"),
						att("version", ValueType.STRING, "")//"1.0")
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
