package starwars.entities;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWEntity;
import starwars.actions.Take;

/**
 * A very minimal entity that has the <code>CHOPPER</code> attribute and so can
 * be used to <code>Chop</code> down <code>Trees</code>.
 * 
 *  @author ram
 *  @see {@link starwars.entities.Reservoir}
 *  @see {@link starwars.actions.Chop}
 */
/*
 * Change log
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Axes hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER capability from the LightSaber as it should not be possible
 * 			  to chop with a broken LightSaber. (asel)
 * 2017/02/22 removed the overriding method. Chopping don't have to damage the axe.
 */
public class LightSaber extends SWEntity {

	/**
	 * Constructor for the <code>LightSaber</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>LightSaber</code></li>
	 * 	<li>Set the short description of this <code>LightSaber</code>>
	 * 	<li>Set the long description of this <code>LightSaber</code> 
	 * 	<li>Add a <code>Take</code> affordance to this <code>LightSaber</code> so it can be taken</li> 
	 * </ul>
	 * 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * 
	 * @see {@link starwars.actions.Take}
	 * @see {@link starwars.Capability}
	 * @see {@link starwars.actions.Chop} 1
	 */
	public LightSaber(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "A Lightsaber";
		this.longDescription = "A lightsaber.  Bzzz-whoosh!";
		this.hitpoints = 100000; // start with a nice powerful, sharp axe
		
		this.addAffordance(new Take(this, m));//add the take affordance so that the LightSaber can be taken by Hobbit Actors
		this.capabilities.add(Capability.WEAPON);// it's a weapon.  
	}
	
	
	
	/**
	 * Lightsabers are indestructible, so doing damage to them has no effect
	 * @param damage - the amount of damage that would be inflicted on a non-mystical Entity
	 */
	@Override
	public void takeDamage(int damage) {
		
		return;
	}
	
	/**
	 * A symbol that is used to represent the LightSaber on a text based user interface
	 * 
	 * @return 	Single Character string "Æ"
	 * @see 	{@link starwars.SWEntityInterface#getSymbol()}
	 */
	@Override
	public String getSymbol() {
		return "Æ";
	}
	
	

}
