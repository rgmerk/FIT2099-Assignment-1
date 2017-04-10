package starwars.actions;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWWorld;

/**
 * <code>SWAction</code> that lets <code>SWActor</code>s walk around the map.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-03	Added a getter for whichDirection attribute. Need it for the GUI to display the move 
 * 				commands in a nice way (asel)
 */
public class Move extends SWAction {

	/**Direction in which this <code>Move</code> action must be performed*/
	Direction whichDirection;
	
	/**The world in which this <code>Move</code> action should occur*/
	SWWorld world;

	/**
	 * Constructor for <code>Move</code> class. Will initialize the direction and the world for the <code>Move</code>.
	 * 
	 * @param d the <code>Direction</code> in which the Entity is supposed to move
	 * @param m <code>MessageRenderer</code> to display messages
	 * @param world the world in which the <code>Move</code> action needs to happen
	 */
	public Move(Direction d, MessageRenderer m, SWWorld world) {
		super(m);
		this.whichDirection = d;
		this.world = world;
	}

	/**
	 * Perform the <code>Move</code> action.
	 * <p>
	 * If it is possible for <code>SWActor a</code> to move in the given direction, tell the world to move them
	 * and then reset <code>a</code>'s move commands to take into account a possible new set of available <code>Moves</code>. 
	 * If it is not possible for <code>a</code> to move in that direction, this method does nothing.
	 * <p>
	 * This method will only be called if the <code>SWActor a</code> is alive
	 * 
	 * @author 	ram
	 * @param 	a the <code>SWActor</code> who is moving
	 */
	public void act(SWActor a) {
		
		if (world.canMove(a, whichDirection)) {
			world.moveEntity(a, whichDirection);
			a.resetMoveCommands(world.find(a));//reset the new possible set of moves based on the new location of the entity
			messageRenderer.render(a.getShortDescription() + " is moving " + whichDirection);
		}
				
	}


	
	/**
	 * This is a wrapper for getDescription().
	 * 
	 * @author ram
	 * @return a String describing this <code>Move</code>, suitable for display to the user
	 */
	@Override
	public String toString() {
		return getDescription();
	}

	
	/**
	 * Returns a String describing this <code>Move</code>, suitable for display to the user.
	 * 
	 * @author ram
	 * @return String comprising "move " and the direction.
	 */
	@Override
	public String getDescription() {
		return "move " + whichDirection.toString();
	}

	/**
	 * Returns true, since this is a move command.  
	 * 
	 * TODO: This may be able to be replaced with a Capability.
	 * 
	 * @author ram
	 * @return true
	 */
	public boolean isMoveCommand() {
		return true;
	}

	
	/**
	 *Returns the time taken to perform this <code>Move</code> action.
	 *
	 *@return the duration of the <code>Move</code> action. Currently hard coded to return 1
	 */
	@Override
	public int getDuration() {
		return 1;
	}

	/**
	 * Returns if or not a <code>SWActor a</code> can perform a <code>Move</code> command.
	 * <p>
	 * This method returns true if and only if <code>a</code> is not dead.
	 * <p>
	 * We assume that actors don't get movement commands attached to them unless they can
	 * in fact move in the appropriate direction.  If this changes, then this method will
	 * need to be altered or overridden.
	 * 
	 * @author 	ram
	 * @param 	a the <code>SWActor</code> doing the moving
	 * @return 	true if and only if <code>a</code> is not dead, false otherwise.
	 * @see 	{@link starwars.SWActor#isDead()}
	 */
	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}

	/**
	 * Returns the <code>Direction</code> in which this <code>Move</code> is directed.
	 * 
	 * @author 	Asel
	 * @return 	The <code>Direction</code> of this <code>Move</code>
	 * @see 	{@link #whichDirection}
	 */
	public Direction getWhichDirection() {
		return whichDirection;
	}

	
	


}
