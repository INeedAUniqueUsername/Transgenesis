package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.STRING;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import designType.Types;
import window.Frame;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElement;

public final class Language {

	//Done
	public static DesignElement createLanguage(Types t) {
		DesignElement e = new DesignElement("Language");
		e.addOptionalMultipleSubElements(
				() -> {
					DesignElement text = new DesignElement("Text");
					text.addAttributes(att("id", STRING));
					return text;
				},
				() -> {
					DesignElement string = new DesignElement("String");
					string.addAttributes(att("id", STRING));
					return string;
				},
				() -> {
					DesignElement message = new DesignElement("Message");
					message.addAttributes(
							att("id", STRING),
							att("text", STRING)
							);
					return message;
				}
				);
		switch(t) {
		case AdventureDesc:
			e.addOptionalSingleSubElements(
					new Text("description")
					);
			break;
		case DockScreen:			break;
		case EconomyType:			break;
		case EffectType:			break;
		case Image:					break;
		case ItemTable:				break;
		case ItemType:				break;
		case MissionType:
			e.addOptionalSingleSubElements(
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
			break;
		case NameGenerator:			break;
		case OverlayType:			break;
		case Power:					break;
		case ShipClass:				break;
		case ShipTable:				break;
		case Sound:					break;
		case Sovereign:
			e.addOptionalSingleSubElements(
					new Text("AttackTarget"),				//	msgAttack
					new Text("AttackTargetBroadcast"),	//	msgDestroyBroadcast
					new Text("HitByFriendlyFire"),		//	msgHitByFriendlyFire
					new Text("QueryEscortStatus"),		//	msgQueryEscortStatus
					new Text("QueryFleetStatus"),			//	msgQueryFleetStatus
					new Text("EscortAttacked"),			//	msgEscortAttacked
					new Text("EscortReportingIn"),		//	msgEscortReportingIn
					new Text("WatchYourTargets"),			//	msgWatchTargets
					new Text("NiceShooting"),				//	msgNiceShooting
					new Text("FormUp"),					//	msgFormUp
					new Text("BreakAndAttack"),			//	msgBreakAndAttack
					new Text("QueryComms"),				//	msgQueryCommunications
					new Text("AbortAttack"),				//	msgAbortAttack
					new Text("Wait"),						//	msgWait
					new Text("QueryWaitStatus"),			//	msgQueryWaitStatus
					new Text("AttackInFormation"),		//	msgAttackInFormation
					new Text("DeterTarget"),				//	msgAttackDeter
					new Text("QueryAttackStatus"),		//	msgQueryAttackStatus
					new Text("DockingSequenceEngaged"),	//	msgDockingSequenceEngaged
					new Text("HitByHostileFire"),			//	msgHitByHostileFire
					new Text("DestroyedByFriendlyFire"),	//	msgDestroyedByFriendlyFire
					new Text("DestroyedByHostileFire"),	//	msgDestroyedByHostileFire
					new Text("BaseDestroyedByTarget")		//	msgBaseDestroyedByTarget
					);
			break;
		case SpaceEnvironmentType:	break;
		case StationType:			break;
		case SystemMap:				break;
		case SystemTable:			break;
		case SystemType:			break;
		case TemplateType:			break;
		case Type:					break;
		default:
			break;
		}
		return e;
	}

}
class Text extends DesignElement {
	String displayName;
	public Text(String id) {
		super();
		displayName = id;
		addAttributes(att("id", STRING, id));
	}
	public String getDisplayName() {
		return displayName;
	}
	//Make uneditable
	public void initializeFrame(Frame frame) {
		JPanel labelPanel = frame.getAttributeLabelPanel();
		JPanel fieldPanel = frame.getAttributeFieldPanel();
		JPanel subElementPanel = frame.getSubElementPanel();
		JTextArea textArea = frame.getTextArea();
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		
		for(DesignAttribute a : getAttributes()) {
			JLabel label = new JLabel(a.getName() + "=");
			label.setFont(Window.FONT_MEDIUM);
			labelPanel.add(label);
			JLabel value = new JLabel(a.getValue());
			value.setFont(Window.FONT_MEDIUM);
			fieldPanel.add(value);
		}
		
		textArea.setText(getText());
	}
}
