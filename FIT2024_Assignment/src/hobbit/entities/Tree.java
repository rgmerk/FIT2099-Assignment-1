/**
 * Class to represent a tree.  Trees are currently pretty passive.  They can be chopped down
 * to produce wood; see the Chop class.
 * 
 * @author ram
 */
package hobbit.entities;

import hobbit.HobbitAffordance;
import hobbit.HobbitEntity;
import hobbit.actions.Chop;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Tree extends HobbitEntity {

	public Tree(MessageRenderer m) {
		super(m);
		HobbitAffordance chop = new Chop(this, m);
		this.addAffordance(chop);	//Chop affordance added to Trees since they can 'be' chopped 
		  							//Trees aren't given the CHOPPER Capability since they cannot be used to Chop
		this.setLongDescription("A beautiful spreading oak tree.");
		this.setShortDescription("a tree");
		this.setSymbol("T");
	}

}
