/**
 * A very minimal entity that has the CHOPPER attribute and so can
 * be used to chop down trees.
 * 
 *  @author ram
 */
package hobbit.entities;

import hobbit.Capability;
import hobbit.HobbitEntity;
import hobbit.actions.Take;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Axe extends HobbitEntity {

	public Axe(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "an axe";
		this.longDescription = "A shiny axe.";
		this.hitpoints = 100; // start with a nice powerful, sharp axe
		
		this.addAffordance(new Take(this, m));//add the take affordance so that the Axe can be taken by Hobbit Actors
		this.capabilities.add(Capability.CHOPPER);//the axe has the capability CHOPPER so it can be used to chop
	}
	
	/**
	 * A symbol that is used to represent the Axe on the user interface
	 * 
	 * @return Single Character string "Æ"
	 */
	public String getSymbol() {
		return "Æ";
	}

}
