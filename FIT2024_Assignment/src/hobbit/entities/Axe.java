/**
 * A very minimal entity that has the CHOPPER attribute and so can
 * be used to chop down trees.
 * 
 *  @author ram
 */
/*
 * Change log
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Axes hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER capability from the Axe as it should not be possible
 * 			  to chop with a broken Axe. (asel)
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
	
	/**
	 * Method insists damage on this <code>Axe</code> by reducing a certain 
	 * amount of <code>damage</code> from this <code>Axe</code>s <code>hitpoints</code>
	 * <p>
	 * This method will also change this <code>Axe</code>s <code>longDescription</code> to
	 * "A broken axe that was once shining" and this <code>Axe</code>s <code>shortDescription</code> to
	 * "a broken axe"if the <code>hitpoints</code> after taking the damage is zero or less.
	 * <p>
	 * If the <code>hitpoints</code> after taking the damage is zero or less, this method will remove the 
	 * <code>CHOPPER</code> capability from this <code>Axe</code> since a broken axe
	 * cannot be used to <code>Chop</code>.
	 * 
	 * @author 	Asel
	 * @param 	damage the amount of <code>hitpoints</code> to be reduced
	 */
	@Override
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		
		if (this.hitpoints<=0) {
			this.shortDescription = "a broken axe";
			this.longDescription  = "A broken axe that was once shining";
			
			this.capabilities.remove(Capability.CHOPPER);
		}
	}

}
