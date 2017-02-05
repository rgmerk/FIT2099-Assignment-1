/**
 * An entity that has the CHOPPER and WEAPON attributes and so can
 * be used to chop down trees, attack others, etc.
 * 
 *  @author dsquire
 */
/*
 * 2017/02/04 Removed the Unicode symbol of the sword to a 's' (asel)
 */
package hobbit.entities;

import hobbit.Capability;
import hobbit.HobbitEntity;
import hobbit.actions.Take;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Sword extends HobbitEntity {

	public Sword(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "a sword";
		this.longDescription = "A gleaming sword";
		this.hitpoints = 100; // start with a nice powerful, sharp sword
		
		this.addAffordance(new Take(this, m));//add the Take affordance so that the sword can be taken by Hobbit Actors
		
													//the sword has capabilities 
		this.capabilities.add(Capability.CHOPPER);  // CHOPPER so that it can chop
		this.capabilities.add(Capability.WEAPON);   // and WEAPON so that it can be used to attack
	}
	
	/**
	 * A symbol to represent the Sword on the user interface
	 * 
	 * @return a single character string " " representing a sword
	 */
	public String getSymbol() {
		return "s"; 
	}

}
