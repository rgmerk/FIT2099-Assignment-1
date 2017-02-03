/**
 * Action that lets a HobbitActor pick up an object.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017/02/03	Actors are no longer given a leave action after taking an item
 * 				Leave action was removed since students had to add this functionality
 * 				canDo method changed to return true only if the actor is not carrying an item (asel)
 */
package hobbit.actions;

import hobbit.HobbitAction;
import hobbit.HobbitActor;
import hobbit.HobbitAffordance;
import hobbit.HobbitEntityInterface;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Take extends HobbitAffordance {

	public Take(HobbitEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	@Override
	/**
	 * asel - The HobbitActor can only take an item if it's not carrying an item already
	 * 
	 * @author ram
	 * @modified 26/01/2017 by asel
	 * @param a the HobbitActor we are querying
	 * @return true if the HobbitActor can take the item (in other words, it's not carrying another item), false otherwise
	 */
	public boolean canDo(HobbitActor a) {
		//can only take if the actor is not carrying any items
		return a.getItemCarried()==null;
	}

	@Override
	/**
	 * Perform the take action by setting the item carried by the Hobbit Actor to the target
	 * <p>
	 * The Hobbit Actor a's item carried would be the target of this Affordance (Take)
	 * <p>
	 * This method would add a Leave action to the HobbitActor and remove the take affordance from the target -Asel
	 * 
	 * @author ram
	 * @modified 26/01/2017 by asel
	 * @param a the HobbitActor that is taking the target
	 */
	public void act(HobbitActor a) {
		if (target instanceof HobbitEntityInterface) {
			a.setItemCarried((HobbitEntityInterface)target);
			HobbitAction.getEntitymanager().remove(target);//remove the target from the entity manager since it's now held by the HobbitActor
			
			//remove the take affordance
			target.removeAffordance(this);
			
			
		}
	}


	@Override
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @author ram
	 * @return String comprising "take " and the short description of the target of this Affordance
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return "take " + target.getShortDescription();
	}

}
