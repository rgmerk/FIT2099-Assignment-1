/**
 * Action that lets Actors leave the item carried.
 * 
 * @author asel
 */
package hobbit.actions;

import hobbit.HobbitAction;
import hobbit.HobbitActor;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Leave extends HobbitAction {
	/*
	 * Implementing Leave as a HobbitAffordance didn't work since the take removes the item from the entity manager
	 * -asel
	 */
	
	/**
	 * Constructor for Leave class
     *
	 * @param m : message renderer to display messages
	 */
	public Leave(MessageRenderer m) {
		super(m);
	}

	/**
	 * Perform the leave action
	 * <p>
	 * The item carried will be left at the HobbitActor's current location and the item will be given a Take affordance
	 * so that it could be picked up.
	 * <p>
	 * The HobbitActor's leave action will be removed after leaving the item
	 *
	 * @param a the HobbitActor who is leaving its item carried
	 */
	public void act(HobbitActor a) {
		//get the item carried by the HobbitActor
		HobbitEntityInterface itemCarried = a.getItemCarried();
		
		//Set it to null so that it's not carrying anything
		a.setItemCarried(null);
		
		//the item can be taken by HobbitActor
		itemCarried.addAffordance(new Take(itemCarried,messageRenderer));
		
		//get the current location of the actor
		HobbitLocation loc = getEntitymanager().whereIs(a);
		
		//leave the item that was carried in the actor's current location
		getEntitymanager().setLocation(itemCarried, loc);
		
		//remove the leave action from the actor
		a.removeAction(this);
				
	}


	@Override
	/**
	 * This is a wrapper for getDescription().
	 *
	 * @return a String describing this leave action, suitable for display to the user
	 */
	public String toString() {
		return getDescription();
	}

	@Override
	/**
	 * Returns a String describing this leave, suitable for display to the user.
	 * 
	 * @return String "leave item carried"
	 * 
	 *TODO
	 * asel -we need a better string that says "leave"+ the short description of the item carried
	 */
	public String getDescription() {
		return "leave item carried";
	}

	
	@Override
	/**
	 * Return the duration of this command, currently hard-coded to be 1.
	 * 
	 */
	public int getDuration() {
		return 1;
	}

	@Override
	/**
	 * The actor can only perform the leave if it's carrying an item with it
	 * 
	 * @param the Actor being queried
	 * @return true if the actor is carrying an item, false otherwise
	 */
	public boolean canDo(HobbitActor a) {
		return a.getItemCarried()!=null;
	}


}
