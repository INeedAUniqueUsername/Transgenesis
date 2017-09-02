package designType.subElements;

import static xml.DesignAttribute.att;
import static xml.DesignAttribute.ValueType.STRING;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import designType.Types;
import panels.XMLPanel;
import window.Frame;
import window.Window;
import xml.DesignAttribute;
import xml.DesignElement;
import static window.Window.Fonts.*;
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
	public static Text createText(String id) {
		Text result = new Text(id);
		return result;
	}
}
class Text extends DesignElement {
	public Text(String id) {
		super();
		addAttributes(att("id", STRING, id));
	}
	public String getDisplayName() {
		//return String.format("%-16s%s", "Text", getAttributeByName("id").getValue());
		return getAttributeByName("id").getValue();
	}
	//Make uneditable
	public void initializeFrame(XMLPanel panel) {
		JPanel labelPanel = panel.labelPanel;
		JPanel fieldPanel = panel.fieldPanel;
		JPanel subElementPanel = panel.subElementPanel;
		JTextArea textArea = panel.textArea;
		labelPanel.removeAll();
		fieldPanel.removeAll();
		subElementPanel.removeAll();
		
		List<DesignAttribute> attributes = getAttributes();
		
		createAttributePanelHeaders(attributes.size() > 0, labelPanel, fieldPanel);
		
		for(DesignAttribute a : attributes) {
			JLabel label = XMLPanel.createLabel((String.format("%-28s[%s]", a.getName() + "=", a.getValueType().toString().toLowerCase())));
			JTextField value = XMLPanel.createTextField(a.getValue(), false);
			value.setPreferredSize(label.getPreferredSize());
			labelPanel.add(label);
			fieldPanel.add(value);
		}
		
		textArea.setText(getText());
	}
}
