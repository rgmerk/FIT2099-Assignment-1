
package edu.monash.fit2099.gridworld;

import edu.monash.fit2099.simulator.userInterface.MapRenderer;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import edu.monash.fit2099.simulator.userInterface.SimulationController;

/**
 * Interface for the Controller of the Model View Controller design pattern. Model is the {@link #Grid} and the View is {@link #GridRenderer}
 * <p>
 * Implementations of <code>GridController</code> will implement <code>render()</code> method of the <code>MapRenderer</code> to display the <code>Grid</code> on the View.
 * <p>
 * They should also implement the <code>render(String)</code> method of the <code>MessageRenderer</code> to display messages on the View.
 * 
 * @author  Asel
 * @see		{@link edu.monash.fit2099.simulator.userInterface.MapRenderer#render()}
 * @see 	{@link edu.monash.fit2099.simulator.userInterface.MessageRenderer#render(String)}
 */
public interface GridController extends MessageRenderer,MapRenderer,SimulationController{

	/**
	 * Implementation of the <code>MapRenderer</code> interface that calls <code>displayMap()</code> method
	 * of the View (<code>GridRenderer</code>) in order to display the Grid on the View, in a suitable form. 
	 * <p>
	 * Concrete implementations of this method should never handle the display of the <code>Grid</code> directly. 
	 * Display of the map is a responsibility of the View (<code>GridRenderer</code>).
	 * 
	 * @see		{@link edu.monash.fit2099.simulator.userInterface.MapRenderer#render()}
	 * @see		{@link edu.monash.fit2099.gridworld.GridRenderer#displayMap()}
	 */
	public abstract void render();
	
	/**
	 * Implementation of the <code>MessageRenderer</code> interface that calls
	 * <code>displayMessage(String)</code> method of the View (<code>GridRenderer</code>) in order to display messages on the View, 
	 * in a suitable form. 
	 * <p>
	 * Concrete implementations of this method should never handle the display of the messages directly. 
	 * Display of messages is a responsibility of the View (<code>GridRenderer</code>).
	 * 
	 * @param 	message the message string to be displayed
	 * @see		{@link edu.monash.fit2099.simulator.userInterface.MessageRenderer#render(String)}
	 * @see		{@link edu.monash.fit2099.gridworld.GridRenderer#displayMessage(String)}
	 */
	public abstract void render(String message);
			
}

 