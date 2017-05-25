package designType;

import designType.subElements.Text;
import xml.Attribute;
import xml.Element;
import xml.Attribute.ValueType;

public class AdventureDesc extends Type {
	public AdventureDesc() {
		super();
		addRequiredAttributes(
				new Attribute("adventureUNID", ValueType.UNID),
				new Attribute("backgroundID", ValueType.TYPE_IMAGE),
				new Attribute("desc", ValueType.STRING),
				new Attribute("include10StartingShips", ValueType.BOOLEAN),
				new Attribute("level", ValueType.INTEGER),
				new Attribute("name", ValueType.STRING),
				new Attribute("startingMap", ValueType.TYPE_SYSTEM_MAP),
				new Attribute("startingPos", ValueType.STRING),
				new Attribute("startingShipCriteria", ValueType.STRING),
				new Attribute("startingSystem", ValueType.STRING),
				new Attribute("welcomeMessage", ValueType.STRING)
				);
		getOptionalSingleByName("Events").addOptionalSingleSubElements(
				new Element("OnGameStart"),
				new Element("OnGameEnd")
				);
		getOptionalSingleByName("Language").addSubElements(new Text("description"));
	}
}