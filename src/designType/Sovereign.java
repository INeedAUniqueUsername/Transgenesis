package designType;

import designType.subElements.Text;
import xml.Attribute;
import xml.Attribute.ValueType;
import xml.Element;

public class Sovereign extends Type {
	public Sovereign() {
		super();
		addRequiredAttributes(
				new Attribute("name", ValueType.STRING),
				new Attribute("shortName", ValueType.STRING),
				new Attribute("adjective", ValueType.STRING),
				new Attribute("demonym", ValueType.STRING),
				new Attribute("plural", ValueType.STRING),
				new Attribute("alignment", ValueType.ALIGNMENT)
				);
		addOptionalSingleSubElements(
				new Relationships()
				);
		getOptionalSingleByName("Language").addOptionalSingleSubElements(
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
	}
}
class Relationships extends Element {
	public Relationships() {
		super();
		addOptionalMultipleSubElements(
				new Relationship()
				);
	}
}
class Relationship extends Element {
	public Relationship() {
		super();
		addRequiredAttributes(
				new Attribute("sovereign", ValueType.TYPE_SOVEREIGN),
				new Attribute("disposition", ValueType.DISPOSITION),
				new Attribute("mutual", ValueType.BOOLEAN)
				);
	}
}