package edu.monash.fit2099.simulator.userInterface;

/**
 * Interface for messages that need to be displayed.  <code>MessageRenderers</code> are used
 * by <code>Actions</code> and <code>Entities</code>.
 * <p>
 * All messages that needs to be displayed needs to be displayed through the <code>messageRenderer</code> so that the Views
 * can determine they way in which they could be displayed.
 * 
 * TODO: they might not be needed in Actions if Actions can delegate their messaging to Entities.
 * 
 * @author 	ram
 * @date 	28 February 2013
 *
 */
public interface MessageRenderer {
	
	/**
	 * Handles the display of messages in a View
	 * 
	 * @param message a string to be displayed
	 */
	public abstract void render(String message);
}
