package starwars.actions;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;

/**
 * Command to chop down trees.
 * 
 * This affordance is attached to <code>Trees</code>, and is only available to actors that are
 * carrying a <code>CHOPPER</code>.
 * 
 * @author 	ram
 * @see 	{@link starwars.entities.Reservoir}
 */
/*Change Log
 * 2017/02/08	Chop given a priority of 1 in constructor (asel)
 * 2017/02/09	Chopping would blunt the CHOPPER (asel)
 * 2017/02/22	Chooping shouldnt blunt the CHOPPER (asel)
 */
public class Chop extends SWAffordance implements SWActionInterface {

	/**
	 * Constructor for the <code>Chop</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being chopped
	 * @param m the message renderer to display messages
	 */
	public Chop(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}
	
	/**
	 *Returns the time taken to perform this <code>Chop</code> action.
	 *
	 *@return the duration of the <code>Chop</code> action. Currently hard coded to return 1
	 */
	@Override	
	public int getDuration() {
		return 1;
	}

	
	/**
	 * A String describing what this <code>Chop</code> will do, suitable for display in a user interface
	 * 
	 * @return String comprising "chop " and the short description of the target of this Affordance
	 */
	@Override
	public String getDescription() {
		return "chop " + this.target.getShortDescription();
	}


	@Override
	/**
	 * Determine whether a particular <code>SWActor a</code> can chop the target.
	 * The item carried by <code>a</code> needs to have the <code>CHOPPER Capability</code>
	 * in order to chop the target.
	 * 
	 * @author 	ram
	 * @param 	a the <code>SWActor</code> being queried
	 * @return 	true if <code>a</code>'s item carried has a <code>CHOPPER Capability</code>, false otherwise.
	 */
	public boolean canDo(SWActor a) {
		if (a.getItemCarried() == null)
			return false;
		return a.getItemCarried().hasCapability(Capability.CHOPPER);
	}

	
	/**
	 * Perform the <code>Chop</code> command on a <code>Reservoir</code>. This replaces the <code>Reservoir</code> with 
	 * a pile of wood.
	 * <p>
	 * This method assumes the targets with the Chop Affordance (targets that can be chopped) are trees. No harm 
	 * is done to the <code>CHOPPER</code> after the chop.
	 *
	 * @author 	ram
	 * @param 	the actor who is chopping 
	 * @pre 	the <code>SWActor a</code> is alive
	 * @see 	{@link starwars.entities.Reservoir}
	 */
	@Override
	public void act(SWActor a) {
		// tree becomes a pile of wood
		// remove chop affordance (so it can no longer be chopped) and change the way it appears
		target.removeAffordance(this);
				
		//change the descriptions to a pile of wood and the symbols
		target.setLongDescription("A pile of wood that was once " + target.getShortDescription());
		target.setShortDescription("a pile of wood");
		getTarget().setSymbol("w");
	}
}
