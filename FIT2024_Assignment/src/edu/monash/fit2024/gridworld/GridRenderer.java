package edu.monash.fit2024.gridworld;
import java.util.ArrayList;

import edu.monash.fit2024.simulator.matter.ActionInterface;

/**
 * View of the MVC. Model is the <code>Grid</code> and the controller is <code>GridController</code> 
 * <p>
 * <code>GridRenderer</code> will render the map (Grid) and messages on the User Interface.
 * 
 * @author Asel
 *
 */
public interface GridRenderer {
	
	/**
	 * Method called by {@link GridController#render()} to display the map of the grid on the user interface
	 */
	public abstract void displayMap();
	
	/**
	 * Method called by {@link GridController#render(String)} to display messages on the user interface
	 * 
	 * @param message A string to be displayed on the user interface.
	 */
	public abstract void displayMessage(String message);
	
	/**
	 * Method that presents the user the list of commands (<code>cmds</code>) in a suitable form (either text or graphical) and returns the command selected by the user. 
	 *
	 * @param 	cmds list of commands from which a single selection must be made and returned
	 * @pre 	cmds should always contain elements to avoid an infinite wait
	 * @return 	a chosen command from the list of commands (<code>cmds</code>)
	 * @post 	the command returned should be an element of the list of commands (<code>cmds</code>) provided
	 */
	public abstract ActionInterface getSelection(ArrayList<ActionInterface> cmds);
	
	
}
