/**
 * Interface that allows HobbitAction and HobbitAffordance to have a common ancestor (and thus
 * be stored in the same structures) despite one extending Action and the other extending Affordance.
 * 
 * @author ram
 */
package hobbit;

import edu.monash.fit2024.simulator.matter.ActionInterface;

public interface HobbitActionInterface extends ActionInterface {
	public boolean isMoveCommand();
	public boolean canDo(HobbitActor a);
	public void act(HobbitActor a);	
}