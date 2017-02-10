package edu.monash.fit2024.simulator.matter;
/*
 * Change Log
 * 2017/02/08	Added an abstract getter for priority (asel)
 */

import hobbit.HobbitActionInterface;

public interface ActionInterface extends Comparable<ActionInterface> {

	/**
	 * <p>Returns the length of time the Action will take.  This can be longer or shorter than
	 * the duration of a tick, but even if it is shorter than half a tick, the Actor performing it doesn't get an
	 * extra move.  If you want Actors to be able to act more than once in a turn, have your turns comprise more than
	 * one tick.</p>
	 * 
	 * <p>The scheduler uses this method to determine the order in which Actions will be completed.  Actions
	 * of short duration complete before Actions of long duration.</p>
	 * 
	 * @return the length of time an Action takes
	 */
	public abstract int getDuration();

	/**
	 * Carries out the actual action.
	 * 
	 * @param actor the Actor performing the action
	 */
	public abstract void execute(Actor<?> actor);

	/**
	 * Returns a description of the Action suitable for display.  This may need to be
	 * computed, e.g. to include the name of the Actor or the target.
	 * 
	 * @return description of the Action
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the priority of the Action. Priority is represented by an integer where a smaller integer 
	 * corresponds to lower priority and vice versa. 
	 * <p>
	 * Actions with higher priority are executed before all actions with lower priority. 
	 * Actions with the same priority will be executed in an arbitrary order.
	 * <p>
	 * The scheduler use the priority of Actions to order Events within a tick.
	 * 
	 * @return the {@link edu.monash.fit2024.simulator.matter.Action#priority}
	 */
	public abstract int getPriority();
	
	
	/**
	 * compareTo method to order HobbitActionInterface objects in alphabetical order
	 */
	public default int compareTo(ActionInterface other) {
		return this.getDescription().compareTo(other.getDescription());
	}

}