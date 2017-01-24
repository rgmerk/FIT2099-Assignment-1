package hobbit.entities.actors;

import hobbit.HobbitActor;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;
import hobbit.Team;

import java.util.List;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class TestActor extends HobbitActor {

	/**
	 * Constructor for the Test Actor
	 * @param m : message renderer to 
	 * @param world : the world the Test Actor is in
	 */
	public TestActor(MessageRenderer m, MiddleEarth world) {
		super(Team.GOOD, 50, m, world);
	}
	
	@Override
	/**
	 * Stands still and waits for other actors to try its affordances. Introduced so that Attack could be tested
	 * 
	 * @author dsquire
	 */
	public void act() {
		say(this.getShortDescription() + " is standing still at " + this.world.getEntityManager().whereIs(this).getShortDescription());
		describeScene();
	}
	
	/**
	 * This method will describe 
	 * 	- the Test Actor's location
	 * 	- items carried (if the Test Actor is carrying any)
	 * 	- the contents of the Test Actor's location (what the player can see) other than itself
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
		if (contents.size() > 1) { // if it is equal to one, the only thing here is this Test Actor, so there is nothing to report
			say(this.getShortDescription() + " can see:");
			for (HobbitEntityInterface entity : contents) {
				if (entity != this) { // don't include self in scene description
					say("\t " + entity.getSymbol() + " - " + entity.getLongDescription() + " [" + entity.getHitpoints() + "]");
				}
			}
		}
	}

}
