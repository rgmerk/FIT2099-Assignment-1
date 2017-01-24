package edu.monash.fit2024.simulator.space;

import edu.monash.fit2024.simulator.userInterface.MapRenderer;

/**
 * LocationContainer: base class for collections of Location subclasses.
 * 
 * This class should be subclassed to implement the geometry required
 * in the World.
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

	protected MapRenderer mapRenderer;
	protected LocationContainer() {}
	public abstract int getHeight();
	public abstract int getWidth();
}