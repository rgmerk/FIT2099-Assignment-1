package hobbit.entities.actors;


import hobbit.HobbitActor;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;
import hobbit.Team;
import hobbit.hobbitinterfaces.HobbitGridController;


import java.util.List;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

/**
 * A very minimal <code>HobbitActor</code> that the user can control.  Its <code>act()</code> method
 * prompts the user to select a command.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017/02/22	Schedule actions in the act method instead of tick. 
 * 				A controller used to get user input rather than the UI directly (Asel)
 */
public class Player extends HobbitActor {

	/**
	 * Constructor for the <code>Player</code> class. This constructor will,
	 * <ul>
	 * 	<li>Initialize the message renderer for the <code>Player</code></li>
	 * 	<li>Initialize the world for this <code>Player</code></li>
	 *  <li>Initialize the <code>Team</code> for this <code>Player</code></li>
	 * 	<li>Initialize the hit points for this <code>Player</code></li>
	 * 	<li>Set this <code>Player</code> as a human controlled <code>HobbitActor</code></li>
	 * </ul>
	 * 
	 * @param team the <code>Team</code> to which the this <code>Player</code> belongs to
	 * @param hitpoints the hit points of this <code>Player</code> to get started with
	 * @param m <code>MessageRenderer</code> to display messages.
	 * @param world the <code>MiddleEarth</code> world to which this <code>Player</code> belongs to
	 * 
	 */
	public Player(Team team, int hitpoints, MessageRenderer m, MiddleEarth world) {
		super(team, hitpoints, m, world);
		humanControlled = true; // this feels like a hack. Surely this should be dynamic
	}
	
	/**
	 * This method will describe this <code>Player</code>'s scene and prompt for user input through the controller 
	 * to schedule the command.
	 * <p>
	 * This method will only be called if this <code>Player</code> is alive and is not waiting.
	 * 
	 * @see {@link #describeScene()}
	 * @see {@link hobbit.hobbitinterfaces.HobbitGridController}
	 */
	@Override
	public void act() {	
		describeScene();
		scheduler.schedule(HobbitGridController.getUserDecision(this), this, 1);
		
	}
	/**
	 * This method will describe, 
	 * <ul>
	 * 	<li>the this <code>Player</code>'s location</li>
	 * 	<li>items carried (if this <code>Player</code> is carrying any)</li>
	 * 	<li>the contents of this <code>Player</code> location (what this <code>Player</code> can see) other than itself</li>
	 * <ul>
	 * <p>
	 * The output from this method would be through the <code>MessageRenderer</code>.
	 * 
	 *  @see {@link edu.monash.fit2024.simulator.userInterface.MessageRenderer}
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
