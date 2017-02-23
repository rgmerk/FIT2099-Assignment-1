package hobbit.entities.actors;

import hobbit.HobbitActor;
import hobbit.MiddleEarth;
import hobbit.Team;
import hobbit.actions.Move;
import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

/**
 * A simple bad guy.  
 * <p>
 * <code>Goblins</code> select a direction and move in it until they walk into something, then they turn around.  They
 * notify the user when they turn around, or if they get stuck in a position with no exits.
 * 
 * @author ram 
 */

public class Goblin extends HobbitActor {

	/**
	 * <code>CompassBearing</code> to keep track of this <code>Goblin</code>'s direction
	 */
	private Grid.CompassBearing myDirection;

	/**
	 * Constructor for the <code>Goblin</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Goblin</code></li>
	 * 	<li>Initialize the world for this <code>Goblin</code></li>
	 * 	<li>Set this <code>Goblin</code>'s <code>Team</code> to <code>EVIL</code></li>
	 * 	<li>Set this <code>Goblin</code>'s hit points to 50</li>
	 * 	<li>Initialize the direction of this <code>Goblin</code> to a random <code>CompassBearing</code></li>
	 * </ul>
	 * 
	 * @param 	m <code>MessageRenderer</code> to display messages.
	 * @param 	world the <code>MiddleEarth</code> world to which this <code>Goblin</code> belongs to
	 * @see 	{@link edu.monash.fit2024.gridworld.Grid.CompassBearing}
	 */
	public Goblin(MessageRenderer m, MiddleEarth world) {
		super(Team.EVIL, 50, m, world);
		myDirection = Grid.CompassBearing.getRandomBearing();
	}

	/**
	 * Method that allows this <code>Goblin</code> to walk forward, turns by 45 degrees until 
	 * either an exit is found or it is established that no exit exists.
	 * <p>
	 * This method is responsible for scheduling this <code>Goblin</code>'s <code>Move</code> commands.
	 * This method will only be called if this <code>Goblin</code> is alive and is not waiting.
	 * 
	 * @author 	ram
	 * 
	 */
	@Override
	public void act() {
			
		// Did I hit something?  If so, bear right.
		Grid.CompassBearing oldDirection = myDirection;
		
		while (!MiddleEarth.getEntitymanager().seesExit(this, myDirection)) {//loop until there is an exist
			myDirection = myDirection.turn(45);
			if (myDirection == oldDirection) {
				// I've turned completely around and can't find an exit -- I'm stuck!
				messageRenderer.render(this.getShortDescription() + " is stuck!  Help!");
				return;//exit the loop as the goblin is stuck
			}
		}
		
		if (myDirection != oldDirection)	// we turned
			messageRenderer.render(this.getShortDescription() + " decides to go " + myDirection + " next.");
		
		// I can see an exit.
		Move myMove = new Move(myDirection, messageRenderer, world);
					
		scheduler.schedule(myMove, this, 1);
	
	}

}
