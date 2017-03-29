package starwars.entities;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWEntity;
import starwars.actions.Take;

/**
 * An entity that has the <code>CHOPPER</code> and <code>WEAPON</code> attributes and so can
 * be used to <code>Chop</code> down <code>Trees</code>, <code>Attack</code> others, etc.
 * 
 * @author 	dsquire
 * @see 	{@link starwars.entities.Tree}
 * @see 	{@link starwars.actions.Chop}
 * @see 	{@link starwars.actions.Attack}
 */
/*
 * 2017/02/04 Removed the Unicode symbol of the sword to a 's' since it wasn't displayed on the text interface(asel)
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Swords hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER and WEAPON capabilities from the Sword as it should not be possible
 * 			  to attack or chop with a broken Sword (asel)
 */
public class Sword extends SWEntity {

	/**
	 * Constructor for the <code>Sword</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Sword</code></li>
	 * 	<li>Set the short description of this <code>Sword</code> to "a sword"</li>
	 * 	<li>Set the long description of this <code>Sword</code> to "A gleaming sword"</li>
	 * 	<li>Set the hit points of this <code>Sword</code> to 100</li>
	 * 	<li>Add a <code>Take</code> affordance to this <code>Sword</code> so it can be taken</li> 
	 *	<li>Add a <code>CHOPPER Capability</code> to this <code>Sword</code> so it can be used to <code>Chop</code></li>
	 *	<li>Add a <code>WEAPON Capability</code> to this <code>Sword</code> so it can be used to <code>Attack</code></li>
	 * </ul>
	 * 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * 
	 * @see {@link starwars.actions.Take}
	 * @see {@link starwars.Capability}
	 * @see {@link starwars.actions.Chop} 
	 */
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
	 * A symbol that is used to represent the Sword on a text based user interface
	 * 
	 * @return 	Single Character string "s"
	 * @see 	{@link starwars.SWEntityInterface#getSymbol()}
	 */
	public String getSymbol() {
		return "s"; 
	}

	/**
	 * Method insists damage on this <code>Sword</code> by reducing a certain 
	 * amount of <code>damage</code> from this <code>Swords</code> <code>hitpoints</code>
	 * <p>
	 * This method will also change this <code>Sword</code>s <code>longDescription</code> to
	 * "A broken sword that was once gleaming"  and this <code>Sword</code>s <code>shortDescription</code> to
	 * "a broken sword" if the <code>hitpoints</code> after taking the damage is zero or less.
	 * <p>
	 * If the <code>hitpoints</code> after taking the damage is zero or less, this method will remove the 
	 * <code>CHOPPER</code> and <code>WEAPON</code> capabilities from this <code>Sword</code> since a broken sword 
	 * cannot be used to <code>Chop</code> or <code>Attack</code>.
	 * <p>
	 * 
	 * @author 	Asel
	 * @param 	damage the amount of <code>hitpoints</code> to be reduced
	 * @see 	{@link starwars.actions.Attack}
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
