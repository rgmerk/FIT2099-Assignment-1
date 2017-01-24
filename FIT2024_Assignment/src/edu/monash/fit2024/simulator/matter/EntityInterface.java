package edu.monash.fit2024.simulator.matter;

import edu.monash.fit2024.simulator.space.Location;

public interface EntityInterface {

	
	/**
	 * Get the short description of the Entity
	 * @return a short string describing the Entity
	 */
	public abstract String getShortDescription();

	/**
	 * Get the long description of the Entity
	 * @return a longer string than the shortDescription, describing the Entity in more detail than the shortDescription
	 */
	public abstract String getLongDescription();

	/**
	 * Sets the short description
	 * @param shortDescription : a short string describing the entity, suitable for display
	 */
	public abstract void setShortDescription(String shortDescription);

	/**
	 * Sets the long description
	 * @param longDescription : a longer string describing the entity in a bit more detail than the shortDescription. The string must be suitable for display
	 */
	public abstract void setLongDescription(String longDescription);

	/**
	 * <p>By default, Entities do nothing on a tick.  Override this method if you want an Entity to 
	 * respond to the passage of time, e.g. by displaying a message or changing state.</p>
	 * @param loc
	 */
	public abstract <L extends Location> void tick(L loc);

	/**
	 * <p>Output a message to the Entity's MessageRenderer.</p>
	 * 
	 * @param message the String to display
	 */
	public abstract void say(String message);

	/**
	 * <p>Add an Affordance to the set of Affordance this Entity manifests</p>
	 * 
	 * @param a the new Affordance
	 */
	public abstract void addAffordance(Affordance a);

	/**
	 * <p> Remove an Affordance from the set of Affordance this Entity manifests</p>
	 * 
	 * @param a the Affordance to remove
	 */
	public abstract void removeAffordance(Affordance a);

	/**
	 * <p>Generate and return an array of references to the Affordances of this Entity.</p>
	 * 
	 * @return an array of references to this entity's Affordances
	 */
	public abstract Affordance[] getAffordances();

}