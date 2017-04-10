package edu.monash.fit2099.simulator.space;

import edu.monash.fit2099.simulator.userInterface.MapRenderer;

/**
 * LocationContainer: base class for collections of <code>Location</code> subclasses.
 * <p>
 * This class should be subclassed to implement the geometry required in the <code>World</code>.
 * 
 * @author ram
 *
 * @param <T> type of Location that this class is to contain
 */

/*
 * Changelog
 * 
 * 2013-03-07: added EntityManager parameter to render method so that clients can also render items (ram)
 */

public abstract class LocationContainer<T extends Location> {

	/**
	 * <code>MapRenderer</code> that allows a map of Locations to be displayed on a View/User Interface
	 * @see {@link edu.monash.fit2099.gridworld.GridController}
	 * @see {@link edu.monash.fit2099.simulator.userInterface.MapRenderer}
	 */
	protected MapRenderer mapRenderer;
	
	/**
	 * Java doc for this? Asel
	 */
	protected LocationContainer() {}
	
	/**Returns the height of this <code>LocationContainer</code>*/
	public abstract int getHeight();
	
	/**Returns the width of this <code>LocationContainer</code>*/
	public abstract int getWidth();
}