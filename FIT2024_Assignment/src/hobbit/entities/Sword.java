/**
 * An entity that has the CHOPPER and WEAPON attributes and so can
 * be used to chop down trees, attack others, etc.
 * 
 *  @author dsquire
 */
/*
 * 2017/02/04 Removed the Unicode symbol of the sword to a 's' (asel)
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Swords hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER and WEAPON capabilities from the Sword as it should not be possible
 * 			  to attack or chop with a broken Sword (asel)
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
	 * @return a single character string "s" representing a sword
	 */
	public String getSymbol() {
		return "s"; 
	}

	/**
	 * Method insists damage on this <code>Sword</code> by reducing a certain 
	 * amount of <code>damage</code> from this <code>Sword</code>s <code>hitpoints</code>
	 * <p>
	 * This method will also change this <code>Sword</code>s <code>longDescription</code> to
	 * "A broken sword that was once gleaming"  and this <code>Sword</code>s <code>shortDescription</code> to
	 * "a broken sword"if the <code>hitpoints</code> after taking the damage is zero or less.
	 * <p>
	 * If the <code>hitpoints</code> after taking the damage is zero or less, this method will remove the 
	 * <code>CHOPPER</code> and <code>WEAPON</code> capabilities from this <code>Sword</code> since a broken sword 
	 * cannot be used to <code>Chop</code> or <code>Attack</code>.
	 * 
	 * @author 	Asel
	 * @param 	damage the amount of <code>hitpoints</code> to be reduced
	 */
	@Override
	public void takeDamage(int damage) {
		super.takeDamage(damage);
		
		if (this.hitpoints<=0) {
			this.shortDescription = "a broken sword";
			this.longDescription  = "A broken sword that was once gleaming";
			
			this.capabilities.remove(Capability.CHOPPER);
			this.capabilities.remove(Capability.WEAPON);
		}
	}
	
	

}
