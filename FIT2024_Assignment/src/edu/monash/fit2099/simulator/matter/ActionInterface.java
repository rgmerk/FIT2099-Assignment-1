package edu.monash.fit2099.simulator.matter;
/*
 * Change Log
 * 2017/02/08	Added an abstract getter for priority (asel)
 */

/**
 * Interface for <code>Actions</code>
 * 
 * @see {@link edu.monash.fit2099.simulator.matter.Action}
 */
public interface ActionInterface extends Comparable<ActionInterface> {

	/**
	 * Returns the length of time the <code>Action</code> will take.  This can be longer or shorter than
	 * the duration of a tick, but even if it is shorter than half a tick, the <code>Actor</code> performing it doesn't get an
	 * extra move.  If you want <code>Actors</code> to be able to act more than once in a turn, have your turns comprise more than
	 * one tick.
	 * <p>
	 * The <code>Scheduler</code> uses this method to determine the order in which <code>Actions</code> will be completed.  <code>Actions</code>
	 * of short duration complete before <code>Actions</code> of long duration.
	 * 
	 * @return 	the length of time an <code>Action</code> takes
	 * @see		edu.monash.fit2099.simulator.time.Scheduler
	 */
	public abstract int getDuration();

	/**
	 * Carries out the actual <code>Action</code>.
	 * 
	 * @param actor the <code>Actor</code> performing the <code>Action</code>
	 */
	public abstract void execute(Actor<?> actor);

	/**
	 * Returns a description of the <code>Action</code> suitable for display.  This may need to be
	 * computed, e.g. to include the name of the <code>Actor</code> or the target.
	 * 
	 * @return description of the <code>Action</code>
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the priority of the <code>Action</code>. Priority is represented by an integer where a smaller integer 
	 * corresponds to lower priority than a larger one. The lowest possible priority is zero (0).
	 * <p>
	 * <code>Actions</code> with higher priority are executed before all <code>Actions</code> with lower priority scheduled within the same tick. 
	 * <code>Actions</code> with the same priority scheduled within the same tick, will be executed in an arbitrary order.
	 * <p>
	 * The <code>Scheduler</code> use the priority of <code>Action</code> to order Events within a tick.
	 * 
	 * @return the priority of the <code>Action</code>
	 */
	public abstract int getPriority();
	
	
	/**
	 * compareTo method to order <code>ActionInterface</code> objects in alphabetical order based on their descriptions
	 */
	public default int compareTo(ActionInterface other) {
		return this.getDescription().compareTo(other.getDescription());
	}
	
}