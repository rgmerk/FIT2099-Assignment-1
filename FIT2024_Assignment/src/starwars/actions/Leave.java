/**
 * Action that lets a SWActor drop the object it is holding.
 * 
 * Ported from the Hobbit version; original author David Squire.
 * 
 * @author ram
 * 
 */
package starwars.actions;

import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import edu.monash.fit2099.simulator.matter.EntityManager;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;

public class Leave extends SWAffordance {

	public Leave(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	@Override
	/**
	 * This should always returns true -- anything that's got a visible Leave affordance
	 * should be being held by the actor that can see the affordance.
	 * 
	 * @author dsquire
	 * @param a the SWActor we are querying
	 * @return true
	 */
	public boolean canDo(SWActor a) {
		return a.getItemCarried().equals(target);
	}

	@Override
	/**
	 * Perform the Leave action.
	 * 
	 * Puts the item carried by the actor in the actor's location.
	 * 
	 * @author dsquire
	 * @param a the SWActor that is leaving the target
	 */
	public void act(SWActor a) {
		if (a.getItemCarried() == null) { // the actor is not holding something
			// This should really throw an exception, but let's just use a message for now.
			a.say("Leave affordance called by actor that is not holding anything. This should never happen");
		}
		else {
			// put the item in the actor's location
			if (target instanceof SWEntityInterface) {
				EntityManager<SWEntityInterface, SWLocation> entityManager = SWAction.getEntitymanager();
				entityManager.setLocation((SWEntityInterface)target, entityManager.whereIs(a));
				a.setItemCarried(null);
				target.removeAffordance(this);
				target.addAffordance(new Take((SWEntityInterface)target, this.messageRenderer)); // add a Take affordance
			}
		}
	}


	@Override
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @author dsquire
	 * @return String comprising "leave " and the short description of the target of this Affordance
	 */
	public String getDescription() {
		return "leave " + target.getShortDescription();
	}

}
