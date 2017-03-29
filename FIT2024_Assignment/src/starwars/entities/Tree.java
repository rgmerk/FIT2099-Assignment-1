package hobbit.entities;

import hobbit.HobbitAffordance;
import hobbit.HobbitEntity;
import hobbit.actions.Chop;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

/**
 * Class to represent a tree.  <code>Trees</code> are currently pretty passive.  They can be chopped down
 * to produce wood.
 * 
 * @author 	ram
 * @see 	{@link hobbit.actions.Chop} 
 */
public class Tree extends HobbitEntity {

	/**
	 * Constructor for the <code>Tree</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Tree</code></li>
	 * 	<li>Set the short description of this <code>Tree</code> to "an tree"</li>
	 * 	<li>Set the long description of this <code>Tree</code> to "A beautiful spreading oak tree."</li>
	 * 	<li>Add a <code>Chop</code> affordance to this <code>Tree</code> so it can be taken</li> 
	 *	<li>Set the symbol of this <code>Tree</code> to "T"</li>
	 * </ul>
	 * 
	 * @param 	m <code>MessageRenderer</code> to display messages.
	 * @see 	{@link hobbit.actions.Chop} 
	 */
	public Tree(MessageRenderer m) {
		super(m);
		HobbitAffordance chop = new Chop(this, m);
		this.addAffordance(chop);	
		
		this.setLongDescription("A beautiful spreading oak tree.");
		this.setShortDescription("a tree");
		this.setSymbol("T");
	}

}
