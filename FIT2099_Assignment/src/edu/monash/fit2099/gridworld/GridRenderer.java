package edu.monash.fit2099.gridworld;
import java.util.ArrayList;

import edu.monash.fit2099.simulator.matter.ActionInterface;

/**
 * Interface for the View/User Interface of the Model View Controller design pattern. Model is the {@link #Grid} and the Controller is {@link #GridController}
 * <p>
 * 
 * Implementations of <code>GridRenderer</code> contains,
 * <ul>
 * 	<li><code>displayMap()</code> method that allows the map of the <code>Grid</code> to be displayed on the User Interface.</li>
 * 	<li><code>displayMessage(String)</code> method that allows messages to be displayed on the User Interface.</li>
 * 	<li><code>getSelection(ArrayList)</code> method that allows user selection to be recieved through the User Interface.</li>
 * </ul>
 * 
 * The above mentioned methods must ONLY be called from an implementation of the <code>GridController</code>.
 * 
 * @author Asel
 */
public interface GridRenderer {
	
	/**
	 * Method called by {@link GridController#render()} to display the map of the grid on the user interface
	 */
	public abstract void displayMap();
	
	/**
	 * Method called by {@link GridController#render(String)} to display messages on the user interface
	 * 
	 * @param message A string to be displayed on the user interface
	 */
	public abstract void displayMessage(String message);
	
	/**
	 * Method that presents the user the list of commands (<code>cmds</code>) in a suitable form (either text or graphical) and 
	 * returns the command selected by the user.
	 * <p>
	 * This method will always display to the user all commands in <code>cmds</code>. This method should never be called with a null or an 
	 * empty list of commands (<code>cmds</code>) in order to avoid an infinite wait.
	 *
	 * @param 	cmds list of commands from which a single selection must be made and returned
	 * @pre 	<code>cmds</code> should not be null and its size should be greater than zero (0)
	 * @return 	a chosen command from the list of commands (<code>cmds</code>)
	 * @post 	the command returned should be an element of the list of commands (<code>cmds</code>) provided
	 */
	public abstract ActionInterface getSelection(ArrayList<ActionInterface> cmds);
	
	
}
