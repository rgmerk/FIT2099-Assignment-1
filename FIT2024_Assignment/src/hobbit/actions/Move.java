/**
 * Action that lets Actors walk around the map.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-03	Added a getter for whichDirection attribute (asel)
 */
package hobbit.actions;

import hobbit.HobbitAction;
import hobbit.HobbitActor;
import hobbit.MiddleEarth;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Move extends HobbitAction {

	/**Direction in which the moveAction must be performed*/
	Direction whichDirection;
	MiddleEarth world;

	/**
	 * Constructor for Move class
	 * @param d : the direction in which the Entity is supposed to move
	 * @param m : message renderer to display messages
	 * @param world : the world in which the move action needs to happen
	 */
	public Move(Direction d, MessageRenderer m, MiddleEarth world) {
		super(m);
		this.whichDirection = d;
		this.world = world;
	}

	/**
	 * Perform the move action.
	 * 
	 * If it is possible for actor a to move in the given direction, tell the world to move them
	 * and then reset a's move commands to take into account a possible new set of available moves.  If
	 * it is not possible for the actor to move in that direction, this method does nothing.
	 * 
	 * @author ram
	 * @param a the HobbitActor who is moving
	 */
	public void act(HobbitActor a) {
		
		if (world.canMove(a, whichDirection)) {
			world.moveEntity(a, whichDirection);
			a.resetMoveCommands(world.find(a));//reset the new possible set of moves based on the new location of the entity
		}
		messageRenderer.render(a.getShortDescription() + " is moving " + whichDirection);
		
	}


	@Override
	/**
	 * This is a wrapper for getDescription().
	 * 
	 * @author ram
	 * @return a String describing this move, suitable for display to the user
	 */
	public String toString() {
		return getDescription();
	}

	@Override
	/**
	 * Returns a String describing this move, suitable for display to the user.
	 * 
	 * @author ram
	 * @return String comprising "move " and the direction.
	 */
	public String getDescription() {
		return "move " + whichDirection.toString();
	}

	/**
	 * Returns true, since this is a move command.  This may be able to be replaced with a Capability.
	 * 
	 * @author ram
	 */
	public boolean isMoveCommand() {
		return true;
	}

	@Override
	/**
	 * Return the duration of this command, currently hard-coded to be 1.
	 * 
	 * @author ram
	 */
	public int getDuration() {
		return 1;
	}

	@Override
	/**
	 * We assume that actors don't get movement commands attached to them unless they can
	 * in fact move in the appropriate direction.  If this changes, then this method will
	 * need to be altered or overridden.
	 * 
	 * @author ram
	 * @param the Actor doing the moving
	 * @return true
	 */
	public boolean canDo(HobbitActor a) {
		return !a.isDead();
	}

	/**
	 * @author Asel
	 * @return @see {@link #whichDirection}
	 */
	public Direction getWhichDirection() {
		return whichDirection;
	}

	
	


}
