package hobbit;

import edu.monash.fit2024.simulator.matter.EntityInterface;

/**
 * All <code>Entities</code> and <code>Actors</code> in the hobbit client package should implement this interface.
 * 
 * It allows them to be managed by the <code>EntityManager</code>.
 * 
 * @author ram
 * @see	{@link edu.monash.fit2024.simulator.matter.EntityInterface}
 * @see {@link edu.monash.fit2024.simulator.matter.EntityManager}
 */
public interface HobbitEntityInterface extends EntityInterface {

	/**
	 * Returns a string symbol that represents this <code>HobbitEntity</code> or <code>HobbitActor</code>.
	 * <p>
	 * The Views use this method to obtain the symbols that are used to query for resources(images) and for display.
	 *  
	 * @return a String representing the <code>HobbitEntity</code> or <code>HobbitActor</code>.
	 * 
	 * @see 	{@link hobbit.hobbitinterfaces.HobbitGridTextInterface}
	 * @see 	{@link hobbit.hobbitinterfaces.HobbitGridBasicGUI}
	 * @see 	{@link hobbit.hobbitinterfaces.HobbitGridGUI}
	 */
	public abstract String getSymbol();
	
	/**
	 * Sets the symbol of this <code>HobbitEntity</code> or <code>HobbitActor</code> with a new string <code>string</code>.
	 * <p>
	 * Although not a must the new symbol is preferably, 
	 * <ul>
	 * 	<li>single character</li>
	 * 	<li>unique for each <code>HobbitEntity</code> or <code>HobbitActor</code></li>
	 * </ul>
	 * <p>
	 * The Views use this method to obtain the symbols that are used to query for resources(images) and for display.
	 * 
	 * @param string a string to represent this <code>HobbitEntity</code> or <code>HobbitActor</code>
	 */
	public abstract void setSymbol(String string);
	
	/**
	 * Returns true if this <code>HobbitEntity</code> or <code>HobbitActor</code> has the given 
	 * capability <code>c</code>, false otherwise.
	 * 
	 * @param 	c the <code>Capability</code> to search for
	 * @return	true if this <code>Capability c</code> is manifested, false otherwise
	 * @see 	{@link hobbit.Capability}
	 */
	public boolean hasCapability(Capability c);
	
	/**
	 * Returns the hitpoints of this <code>HobbitEntity</code> or <code>HobbitActor</code>.
	 * 
	 * @return the amount of hitpoints
	 */
	public int getHitpoints();
	
	/**
	 * Method that reduces the <code>hitpoints</code> to insist damage on of this 
	 * <code>HobbitEntity</code> or <code>HobbitActor</code>.
	 * 
	 * @param damage the amount of <code>hitpoints</code> to be reduced
	 * @pre <code>damage</code> should be greater than or equal to zero to avoid any increase in the number of <code>hitpoints</code>
	 */
	public void takeDamage(int damage);

}