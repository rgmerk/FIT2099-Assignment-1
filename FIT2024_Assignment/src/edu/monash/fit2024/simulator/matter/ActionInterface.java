package edu.monash.fit2024.simulator.matter;


public interface ActionInterface {

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

}