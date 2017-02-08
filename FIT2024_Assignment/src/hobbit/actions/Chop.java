/**
 * Command to chop down trees.
 * 
 * This affordance is attached to trees, and is only available to actors that are
 * carrying a CHOPPER.
 * 
 * @author ram
 */
/*Change Log
 * 2017/02/08	Chop given a priority of 1 in constructor (asel)
 */
package hobbit.actions;

import hobbit.Capability;
import hobbit.HobbitActionInterface;
import hobbit.HobbitActor;
import hobbit.HobbitAffordance;
import hobbit.HobbitEntityInterface;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Chop extends HobbitAffordance implements HobbitActionInterface {

	/**
	 * Constructor for the Chop Class
	 * @param theTarget that is being chopped
	 * @param m the message renderer to display messages
	 */
	public Chop(HobbitEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}

	@Override
	/**
	 *returns the duration of the Attack action. Currently hard coded to return 1
	 */
	public int getDuration() {
		return 1;
	}

	@Override
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @return String comprising "chop " and the short description of the target of this Affordance
	 */
	public String getDescription() {
		return "chop " + this.target.getShortDescription();
	}


	@Override
	/**
	 * <p>Determine whether a particular actor can chop the target.</p>
	 * <p>The item carried by the actor need to have the CHOPPER capability in order to chop</p>
	 * 
	 * @author ram
	 * @param a the actor being queried
	 * @return true if the actor is carrying something with the CHOPPER capability. 
	 * False if the item carried by the actor has no CHOPPER capability or if the actor isn't carrying any item
	 */
	public boolean canDo(HobbitActor a) {
		if (a.getItemCarried() == null)
			return false;
		return a.getItemCarried().hasCapability(Capability.CHOPPER);
	}

	@Override
	/**
	 * <p>Perform the Chop command on a tree.</p>
	 * 
	 * <p>This replaces the tree with a pile of wood.</p>
	 * 
	 * <p>Assumes the targets with the Chop Affordance (targets that can be chopped) are trees</p> 
	 * 
	 * <p>This method will only be called if the <code>HobbitActor a</code> is alive</p>
	 * 
	 * @author ram
	 * @param the actor who is chopping
	 */
	public void act(HobbitActor a) {
		// tree becomes a pile of wood
		// remove chop affordance and change the way it appears
		target.removeAffordance(this);
		
		//change the descriptions to a pile of wood and the symbols
		target.setLongDescription("A pile of wood that was once " + target.getShortDescription());
		target.setShortDescription("a pile of wood");
		getTarget().setSymbol("w");
	}
}
