package hobbit;

import edu.monash.fit2024.simulator.matter.ActionInterface;

/**
 * Interface that allows <code>HobbitAction</code> and <code>HobbitAffordance</code> to have a common ancestor (and thus
 * be stored in the same structures) despite one extending <code>Action</code> and the other extending <code>Affordance</code>.
 * 
 * @author ram
 */
public interface HobbitActionInterface extends ActionInterface {
	
	/**Returns if or not the action is a move command. Returns true if so, false otherwise*/
	public boolean isMoveCommand();
	
	/**
	 * Returns if the given <code>HobbitActor a</code> can perform this action
	 * 
	 * @param 	a the <code>HobbitActor</code> being queried
	 * @return	true if <code>a</code> can perform this action, false otherwise
	 */	
	public boolean canDo(HobbitActor a);
	
	/**
	 * The method that defines what needs to be performed when this action is performed.
	 * 
	 * @param a the <code>HobbitActor</code> who performs this action.
	 */
	public void act(HobbitActor a);	
}