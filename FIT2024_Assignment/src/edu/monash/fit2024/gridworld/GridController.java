
package edu.monash.fit2024.gridworld;

import edu.monash.fit2024.simulator.userInterface.MapRenderer;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import edu.monash.fit2024.simulator.userInterface.SimulationController;

/**
 * Controller of the MVC. Model is the <code>Grid</code> and the view is <code>GridRenderer</code> 
 * <p>
 * <code>GridController</code> will control the rendering of the map (Grid) and messages on the <code>GridRenderer</code> (User Interface)
 * 
 * @author Asel
 *
 */
public interface GridController extends MessageRenderer,MapRenderer,SimulationController{

	/**
	 * Render and display the Grid on the user interface. 
	 * <p>
	 * Part of the <code>MapRenderer</code> interface.
	 */
	public abstract void render();
	
	/**
	 * Render and display messages on the user interface. 
	 * <p>
	 * Part of the <code>MessageRenderer</code> interface.
	 * 
	 * @param message A string to be displayed on the user interface.
	 */
	public abstract void render(String message);
			
}

 