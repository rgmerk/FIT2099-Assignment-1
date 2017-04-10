package starwars;

import edu.monash.fit2099.simulator.matter.ActionInterface;

/**
 * Interface that allows <code>SWAction</code> and <code>SWAffordance</code> to have a common ancestor (and thus
 * be stored in the same structures) despite one extending <code>Action</code> and the other extending <code>Affordance</code>.
 * 
 * @author ram
 */
public interface SWActionInterface extends ActionInterface {
	
	/**Returns if or not the action is a move command. Returns true if so, false otherwise*/
	public boolean isMoveCommand();
	
	/**
	 * Returns if the given <code>SWActor a</code> can perform this action
	 * 
	 * @param 	a the <code>SWActor</code> being queried
	 * @return	true if <code>a</code> can perform this action, false otherwise
	 */	
	public boolean canDo(SWActor a);
	
	/**
	 * The method that defines what needs to be performed when this action is performed.
	 * 
	 * @param a the <code>SWActor</code> who performs this action.
	 */
	public void act(SWActor a);	
}