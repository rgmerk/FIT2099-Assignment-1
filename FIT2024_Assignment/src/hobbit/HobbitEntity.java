/**
 * Class that represents inanimate objects in the Hobbit world.
 * Objects that cannot move for example trees
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: Extension to the Javadoc and added comments(asel)
 */
package hobbit;

import java.util.HashSet;

import edu.monash.fit2024.simulator.matter.Entity;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class HobbitEntity extends Entity implements HobbitEntityInterface {
	private String symbol;
	protected HashSet<Capability> capabilities;//a HashSet of what the entity can do (capabilities)
	protected int hitpoints = 0; // Not all non-actor entities will make use of this

	protected HobbitEntity(MessageRenderer m) {
		super(m);//will assign the MessageRenderer to the HobbitEntity and initialize the affordance Hashset
		capabilities = new HashSet<Capability>();
	}

	/* 
	 * @see hobbit.HobbitEntityInterface#getSymbol()
	 */
	/**
	 * Getter for the symbol
	 * @return symbol : by which the entity is represented by
	 */
	@Override
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Setter for the symbol of the Entity
	 *<p>Single character strings are preferred over multi character strings to avoid confusion in the text interface</p>
	 *
	 *<p>Try avoiding "." as an empty space is represented on the text interface using the ".", hence your Actor might not be visible 
	 * 	and be mistaken for an empty space </p>
	 * <p>These constraints are however for the current Text interface and would not have to be followed for a GUI for example</p>
	 * 
	 * TODO : assertions for a single character and .?
	 * 
	 * @param the symbol by which the entity is represented by. 
	 *
	 */
	public void setSymbol(String s) {
		assert (!s.matches(".") && (s.length()==1)):"Symbol should be a single charater and not a '.'";
		symbol = s;
	}

	/**
	 * <p>Returns true if this Entity has the given capability, false otherwise.</p>
	 * <p>Wrapper for HashSet<Capability>.contains().</p>
	 * 
	 * @param the Capability to search for
	 */
	@Override
	public boolean hasCapability(Capability c) {
		return capabilities.contains(c);
	}

	public int getHitpoints() {
		return hitpoints;
	}
	
	public void setHitpoints(int p) {
		hitpoints = p;
	}
	
	/**
	 * Method insist damage by reducing a certain amount of damage from the enitiy's hit points
	 * 
	 * @param the amount of damage to be done on the actor.
	 * 
	 *TODO: Add an assertion to make sure the damage given is a positive value or zero since a negative value could increase the hitpoints
	 */
	public void takeDamage(int damage) {
		//assertion to ensure the damage is not negative
		//this assertion might have to be removed if the entities can be healed, sounds like a hack
		assert (damage >= 0)	:"damage on entity must not be negative";
		
		this.hitpoints -= damage;
	}
}
