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
	
	/**A string symbol that represents this <code>HobbitEntity</code>, suitable for display*/
	private String symbol;
	
	/**A set of <code>Capability</code>s of this <code>HobbitEntity</code>*/
	protected HashSet<Capability> capabilities;
	
	/**The amount of <code>hitpoints</code> of this Hobbit Entity.*/
	protected int hitpoints = 0; // Not all non-actor entities will make use of this

	protected HobbitEntity(MessageRenderer m) {
		super(m);//will assign the MessageRenderer to the HobbitEntity and initialize the affordance Hashset
		capabilities = new HashSet<Capability>();
	}

	/* 
	 * @see hobbit.HobbitEntityInterface#getSymbol()
	 */
	/**
	 * Getter for the <code>symbol</code> of the <code>HobbitEntity</code>
	 * @return symbol @see {@link #symbol}
	 */
	@Override
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Setter for the <code>symbol</code> of the <code>HobbitEntity</code>
	 *
	 * @param s @see {@link #symbol} 
	 */
	public void setSymbol(String s) {
		assert (!s.matches(".") && (s.length()==1)):"Symbol should be a single charater and not a '.'";
		symbol = s;
	}

	/**
	 * <p>Returns true if this Entity has the given capability, false otherwise.</p>
	 * <p>Wrapper for HashSet<Capability>.contains().</p>
	 * 
	 * @param c the Capability to search for
	 */
	@Override
	public boolean hasCapability(Capability c) {
		return capabilities.contains(c);
	}

	/**
	 * Getter for the <code>hitpoints</code> of <code>HobbitEntity</code>
	 * 
	 * @return @see {@link #hitpoints}
	 */
	public int getHitpoints() {
		return hitpoints;
	}
	
	/**
	 * Setter for the <code>hitpoints</code> of <code>HobbitEntity</code>
	 * 
	 * @param p @see {@link #hitpoints}
	 */
	public void setHitpoints(int p) {
		hitpoints = p;
	}
	
	/**
	 * Method insists damage on this <code>HobbitEntity</code> by reducing a certain amount of <code>damage</code> from this <code>HobbitEntity</code>'s <code>hitpoints</code>
	 * 
	 * @param damage the amount of <code>hitpoints</code> to be reduced
	 */
	public void takeDamage(int damage) {
		//assertion to ensure the damage is not negative. Negative damage could increase the HobbitEntity's hitpoints
		assert (damage >= 0)	:"damage on HobbitActor must not be negative";
		this.hitpoints -= damage;
	}
	
}
