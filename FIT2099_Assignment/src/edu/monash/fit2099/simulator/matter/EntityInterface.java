package edu.monash.fit2099.simulator.matter;
import edu.monash.fit2099.simulator.space.Location;

/**
 * Interface for creating <code>Entities</code>. 
 * 
 * @see edu.monash.fit2099.simulator.matter.Entity
 * 
 */
public interface EntityInterface {

	
	/**
	 *Returns the short description of the <code>Entity</code>
	 * 
	 * @return a short string describing the <code>Entity</code>
	 */
	public abstract String getShortDescription();

	/**
	 * Returns the long description of the <code>Entity</code>. Long Descriptions usually provide 
	 * more detail than the short Descriptions.
	 * 
	 * @return a longer string describing the <code>Entity</code>
	 */
	public abstract String getLongDescription();

	/**
	 * Sets the short description
	 * 
	 * @param shortDescription a short string describing the <code>Entity</code>, suitable for display
	 */
	public abstract void setShortDescription(String shortDescription);

	/**
	 * Sets the long description. Long Descriptions usually provide 
	 * more detail than the short Descriptions and must be suitable for display.
	 * 
	 * @param longDescription a longer string describing the <code>Entity</code>.
	 */
	public abstract void setLongDescription(String longDescription);

	/**
	 * By default, Entities do nothing on a tick.  Override this method if you want an <code>Entity</code> to 
	 * respond to the passage of time, e.g. by displaying a message or changing state.
	 * 
	 * @param loc the <code>Location</code> of the <code>Entity</code>
	 */
	public abstract <L extends Location> void tick(L loc);

	/**
	 * Output a message to the <code>Entity</code>'s <code>MessageRenderer</code>.
	 * <p>
	 * This message would be displayed on the View (an implementation of <code>GridRenderer</code>)
	 * 
	 * @param 	message the String to display
	 * @see 	edu.monash.fit2099.gridworld.GridRenderer
	 */
	public abstract void say(String message);

	/**
	 * Add an <code>Affordance</code> to the set of <code>Affordances</code> the <code>Entity</code> manifests
	 * 
	 * @param a the new Affordance
	 */
	public abstract void addAffordance(Affordance a);

	/**
	 * Remove an <code>Affordance</code> from the set of <code>Affordances</code> this <code>Entity</code> manifests
	 * <p>
	 * This method does nothing if the <code>Entity</code> doesn't manifest the <code>Affordance a</code>
	 * 
	 * @param a the Affordance to remove
	 */
	public abstract void removeAffordance(Affordance a);

	/**
	 * Generate and return an array of references to the <code>Affordances</code> of this <code>Entity</code>.
	 * 
	 * @return an array of references to this <code>Entity</code>'s <code>Affordances</code>
	 */
	public abstract Affordance[] getAffordances();

}