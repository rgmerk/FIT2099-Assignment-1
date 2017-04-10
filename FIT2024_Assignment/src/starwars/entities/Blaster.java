package starwars.entities;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWEntity;
import starwars.actions.Take;

/**
 * An entity that has the <code>WEAPON</code> attribute and so can
 * be used to <code>Attack</code> others, etc.
 * 
 * @author 	dsquire
 * @see 	{@link starwars.entities.Reservoir}
 * @see 	{@link starwars.actions.Chop}
 * @see 	{@link starwars.actions.Attack}
 */
/*
 * 2017/02/04 Removed the Unicode symbol of the sword to a 's' since it wasn't displayed on the text interface(asel)
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Swords hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER and WEAPON capabilities from the Blaster as it should not be possible
 * 			  to attack or chop with a broken Blaster (asel)
 */
public class Blaster extends SWEntity {

	/**
	 * Constructor for the <code>Blaster</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Blaster</code></li>
	 * 	<li>Set the short description of this <code>Blaster</code> to "a blaster"</li>
	 * 	<li>Set the long description of this <code>Blaster</code> to "A shiny blaster pistol"</li>
	 * 	<li>Set the hit points of this <code>Blaster</code> to 100</li>
	 * 	<li>Add a <code>Take</code> affordance to this <code>Blaster</code> so it can be taken</li> 
	 *	<li>Add a <code>WEAPON Capability</code> to this <code>Blaster</code> so it can be used to <code>Attack</code></li>
	 * </ul>
	 * 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * 
	 * @see {@link starwars.actions.Take}
	 * @see {@link starwars.Capability}
	 */
	public Blaster(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "a blaster";
		this.longDescription = "A shiny blaster pistol";
		this.hitpoints = 100; // start with a fully charged pistol
		
		this.addAffordance(new Take(this, m));//add the Take affordance so that the blaster can be picked up
		
													//the blaster has capabilities 
		this.capabilities.add(Capability.WEAPON);   // and WEAPON so that it can be used to attack
	}
	
	
	/**
	 * A symbol that is used to represent the Blaster on a text based user interface
	 * 
	 * @return 	Single Character string "s"
	 * @see 	{@link starwars.SWEntityInterface#getSymbol()}
	 */
	public String getSymbol() {
		return "s"; 
	}

	/**
	 * Method insists damage on this <code>Blaster</code> by reducing a certain 
	 * amount of <code>damage</code> from this <code>Swords</code> <code>hitpoints</code>
	 * <p>
	 * This method will also change this <code>Blaster</code>s <code>longDescription</code> to
	 * "A broken sword that was once gleaming"  and this <code>Blaster</code>s <code>shortDescription</code> to
	 * "a broken sword" if the <code>hitpoints</code> after taking the damage is zero or less.
	 * <p>
	 * If the <code>hitpoints</code> after taking the damage is zero or less, this method will remove the 
	 * <code>CHOPPER</code> and <code>WEAPON</code> capabilities from this <code>Blaster</code> since a broken sword 
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
			this.shortDescription = "an empty blaster";
			this.longDescription  = "An empty blaster that makes a pitiful \"ping\" sound when fired";
			
			this.capabilities.remove(Capability.WEAPON);
		}
	}
	
	

}
