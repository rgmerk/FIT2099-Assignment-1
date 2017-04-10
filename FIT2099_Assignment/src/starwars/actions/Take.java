package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;

/**
 * <code>SWAction</code> that lets a <code>SWActor</code> pick up an object.
 * 
 * @author ram
 */
/*
 * Changelog
 * 2017/01/26	- candDo method changed. An actor can only take if it's not holding any items already.
 * 				- act method modified. Take affordance removed from the item picked up, since an item picked up
 * 				  cannot be taken. This is just a safe guard.
 * 2017/02/03	- Actors are no longer given a leave action after taking an item.
 * 				- Leave action was removed since students had to add this functionality. (yes there was a leave action
 * 				  but I've failed to document it here)
 * 				- canDo method changed to return true only if the actor is not carrying an item (asel)
 */
public class Take extends SWAffordance {

	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Take(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}


	/**
	 * Returns if or not this <code>Take</code> can be performed by the <code>SWActor a</code>.
	 * <p>
	 * This method returns true if and only if <code>a</code> is not carrying any item already.
	 *  
	 * @author 	ram
	 * @author 	Asel (26/01/2017)
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true if the <code>SWActor</code> is can take this item, false otherwise
	 * @see		{@link starwars.SWActor#getItemCarried()}
	 */
	@Override
	public boolean canDo(SWActor a) {
		return a.getItemCarried()==null;
	}

	/**
	 * Perform the <code>Take</code> action by setting the item carried by the <code>SWActor</code> to the target (
	 * the <code>SWActor a</code>'s item carried would be the target of this <code>Take</code>).
	 * <p>
	 * This method should only be called if the <code>SWActor a</code> is alive.
	 * 
	 * @author 	ram
	 * @author 	Asel (26/01/2017)
	 * @param 	a the <code>SWActor</code> that is taking the target
	 * @see 	{@link #theTarget}
	 * @see		{@link starwars.SWActor#isDead()}
	 */
	@Override
	public void act(SWActor a) {
		if (target instanceof SWEntityInterface) {
			SWEntityInterface theItem = (SWEntityInterface) target;
			a.setItemCarried(theItem);
			SWAction.getEntitymanager().remove(target);//remove the target from the entity manager since it's now held by the SWActor
			
			//remove the take affordance
			target.removeAffordance(this);
			
			// add a leave affordance
			target.addAffordance(new Leave(theItem, messageRenderer));
		}
	}

	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @author ram
	 * @return String comprising "take " and the short description of the target of this <code>Take</code>
	 */
	@Override
	public String getDescription() {
		return "take " + target.getShortDescription();
	}

}
