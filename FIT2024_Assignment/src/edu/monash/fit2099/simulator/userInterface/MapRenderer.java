package edu.monash.fit2099.simulator.userInterface;

/**
 * Interface for rendering the Map. This interface must be implemented by any controllers that calls the View.
 * 
 * @see {@link edu.monash.fit2099.gridworld.GridController}
 */

public interface MapRenderer {
	
	/**
	 * Handles the display of the map in a View.
	 */
	public void render();
}
