package designType;

import designType.subElements.Text;
import xml.Attribute;
import xml.Attribute.ValueType;

public class MissionType extends Type {
	public MissionType() {
		super();
		addAttributes(
				new Attribute("allowPlayerDelete", ValueType.BOOLEAN, "false"),
				new Attribute("debriefAfterOutOfSystem", ValueType.BOOLEAN, "false"),
				new Attribute("expireTime", ValueType.INTEGER),
				new Attribute("failureAfterOutOfSystem", ValueType.BOOLEAN, "false"),
				new Attribute("forceUndockAfterDebrief", ValueType.BOOLEAN, "false"),
				new Attribute("level", ValueType.WHOLE),
				new Attribute("maxAppearing", ValueType.WHOLE),
				new Attribute("name", ValueType.STRING),
				new Attribute("noDebrief", ValueType.BOOLEAN, "false"),
				new Attribute("noDecline", ValueType.BOOLEAN, "false"),
				new Attribute("noFailureOnOwnerDestroyed", ValueType.BOOLEAN, "false"),
				new Attribute("noStats", ValueType.BOOLEAN, "false"),
				new Attribute("priority", ValueType.WHOLE, "1")
				);
		getOptionalSingleByName("Events").addOptionalSingleSubElements(
				"OnCreate",
				"OnDestroy",
				"OnAccepted",
				"OnAcceptedUndock",
				"OnDeclined",
				"OnStarted",
				"OnInProgress",
				"OnInProgressUndock",
				"OnSetPlayerTarget",
				"OnCompleted",
				"OnCanDebrief",
				"OnDebriefedUndock",
				"OnGetNextScreen",
				"OnReward"
				);
		getOptionalSingleByName("Language").addOptionalSingleSubElements(
				new Text("Name"),
				new Text("Summary"),
				new Text("Intro"),
				new Text("Briefing"),
				new Text("AcceptReply"),
				new Text("DeclineReply"),
				new Text("InProgress"),
				new Text("SuccessMsg"),
				new Text("FailureMsg"),
				new Text("SuccessDebrief"),
				new Text("FailureDebrief"),
				new Text("AlreadyDebriefed"),
				new Text("Unavailable")
				);
	}
}
