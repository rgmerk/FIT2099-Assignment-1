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
 *  @see {@link starwars.entities.Tree}
 *  @see {@link starwars.actions.Chop}
 */
/*
 * Change log
 * 2017/02/08 takeDamage method was overridden to change it's descriptions when the Axes hitpoints are zero or less
 * 			  the takeDamage method will also remove the CHOPPER capability from the Axe as it should not be possible
 * 			  to chop with a broken Axe. (asel)
 * 2017/02/22 removed the overriding method. Chopping don't have to damage the axe.
 */
public class Axe extends SWEntity {

	/**
	 * Constructor for the <code>Axe</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Axe</code></li>
	 * 	<li>Set the short description of this <code>Axe</code> to "an axe"</li>
	 * 	<li>Set the long description of this <code>Axe</code> to "A shiny axe"</li>
	 * 	<li>Set the hit points of this <code>Axe</code> to 100</li>
	 * 	<li>Add a <code>Take</code> affordance to this <code>Axe</code> so it can be taken</li> 
	 *	<li>Add a <code>CHOPPER Capability</code> to this <code>Axe</code> so it can be used to <code>Chop</code></li>
	 * </ul>
	 * 
	 * @param m <code>MessageRenderer</code> to display messages.
	 * 
	 * @see {@link starwars.actions.Take}
	 * @see {@link starwars.Capability}
	 * @see {@link starwars.actions.Chop} 
	 */
	public Axe(MessageRenderer m) {
		super(m);
		
		this.shortDescription = "an axe";
		this.longDescription = "A shiny axe.";
		this.hitpoints = 100; // start with a nice powerful, sharp axe
		
		this.addAffordance(new Take(this, m));//add the take affordance so that the Axe can be taken by Hobbit Actors
		this.capabilities.add(Capability.CHOPPER);//the axe has the capability CHOPPER so it can be used to chop
	}
	
	/**
	 * A symbol that is used to represent the Axe on a text based user interface
	 * 
	 * @return 	Single Character string "Æ"
	 * @see 	{@link starwars.SWEntityInterface#getSymbol()}
	 */
	@Override
	public String getSymbol() {
		return "Æ";
	}
	
	

}
