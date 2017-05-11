package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Canteen;

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
public class Train extends SWAffordance {

	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Train(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		//priority = 1;
	}


	
	@Override
	public void act(SWActor a) {
		((SWActor) target).setForce(((SWActor) target).getForce() + 10);

		//if (target instanceof SWEntityInterface) {
			//a.setForce(300);  
				
				}
	


	@Override
	public boolean canDo(SWActor a) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getDuration() {
		return 2;
     }
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		
		return "";
	}
	
	


}
