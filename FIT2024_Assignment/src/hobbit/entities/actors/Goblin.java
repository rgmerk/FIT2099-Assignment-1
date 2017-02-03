/**
 * A simple bad guy.  
 * 
 * Goblins select a direction and move in it until they walk into something, then they turn around.  They
 * notify the user when they turn around, or if they get stuck in a position with no exits.
 * 
 * @author ram
 * 
 */
package hobbit.entities.actors;

import hobbit.HobbitActor;
import hobbit.MiddleEarth;
import hobbit.Team;
import hobbit.actions.Move;
import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Goblin extends HobbitActor {

	private Grid.CompassBearing myDirection;

	/**
	 * Contructor for the Goblin
	 * @param m :message renderer to display messages
	 * @param world :the world to which the Goblin belongs to
	 */
	public Goblin(MessageRenderer m, MiddleEarth world) {
		super(Team.EVIL, 50, m, world);//Goblins belong to the EVIL team
		myDirection = Grid.CompassBearing.getRandomBearing();//given a random bearing to start with
	}

	@Override
	/**
	 * Tries to walk forward, turns by 45 degrees until either an exit is found or it is established that
	 * no exit exists.
	 * 
	 * @author ram
	 * @param a this(The Goblin itself)
	 */
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
		
		
		//TESTING Delay and cool down
		//myMove.setDelay(10);
		//myMove.setCooldown(2);
		
		//schedule the move (myMove) for the Goblin (this) and is to take a duration of 1 (1)
		scheduler.schedule(myMove, this, 1);
		
	}

}
