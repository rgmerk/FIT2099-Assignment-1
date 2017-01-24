package edu.monash.fit2024.simulator.userInterface;

/**
 * Interface for messages that need to be displayed.  MessageRenderers are used
 * by Actions and Entities.
 * 
 * TODO: they might not be needed in Actions if Actions can delegate their messaging to Entities.
 * 
 * @author ram
 * @date 28 February 2013
 *
 */
public interface MessageRenderer {
	public abstract void render(String message);
}
