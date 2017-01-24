/**
 * A very minimal Actor that the user can control.  Its tick() method
 * prompts the user to select a command.
 * 
 * @author ram
 */
package hobbit.entities.actors;


import hobbit.HobbitActor;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;
import hobbit.Team;
import hobbit.TextInterface;

import java.util.List;

import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Player extends HobbitActor {


	/**
	 * Constructor for the Player that can be controlled by the user
	 * @param team : to which the player belongs to
	 * @param hitpoints : the hit points of the player to get started with
	 * @param m : message renderer to display messages
	 * @param world : the world the player belongs to
	 */
	public Player(Team team, int hitpoints, MessageRenderer m, MiddleEarth world) {
		super(team, hitpoints, m, world);
		humanControlled = true; // this feels like a hack. Surely this should be dynamic
	}

	/**
	 * Allow the user to select an action to perform, and then add that action to
	 * the event queue.
	 * 
	 * @param l the Location of the player (not actually used)
	 * @author ram
	 */
	//public void tick(Location l) {}

	@Override
	/**
	 * Does nothing -- the user selects what this Player does.
	 */
	public void act() {
	
		describeScene();
		scheduler.schedule(TextInterface.getUserDecision(this), this, 1);
		
	}
	/**
	 * This method will describe 
	 * 	- the player's location
	 * 	- items carried (if the player is carrying any)
	 * 	- the contents of the players location (what the player can see) other than itself
	 */
	public void describeScene() {
		//get the location of the player and describe it
		HobbitLocation location = this.world.getEntityManager().whereIs(this);
		say(this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription());
		
		//get the items carried for the player
		HobbitEntityInterface itemCarried = this.getItemCarried();
		if (itemCarried != null) {
			//and describe the item carried if the player is actually carrying an item
			say(this.getShortDescription() 
					+ " is holding " + itemCarried.getShortDescription() + " [" + itemCarried.getHitpoints() + "]");
		}
		
		//get the contents of the location
		List<HobbitEntityInterface> contents = this.world.getEntityManager().contents(location);
		
		//and describe the contents
		if (contents.size() > 1) { // if it is equal to one, the only thing here is this Player, so there is nothing to report
			say(this.getShortDescription() + " can see:");
			for (HobbitEntityInterface entity : contents) {
				if (entity != this) { // don't include self in scene description
					say("\t " + entity.getSymbol() + " - " + entity.getLongDescription() + " [" + entity.getHitpoints() + "]");
				}
			}
		}
	}
}
