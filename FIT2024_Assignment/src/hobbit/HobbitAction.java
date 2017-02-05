/**
 * Base class for all intransitive commands in the Hobbit world.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: Extensions to the Javadoc
 * 
 */
package hobbit;

import edu.monash.fit2024.simulator.matter.Action;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public abstract class HobbitAction extends Action implements HobbitActionInterface {
	
	private static final EntityManager<HobbitEntityInterface, HobbitLocation> entityManager = MiddleEarth.getEntitymanager();

	public HobbitAction(MessageRenderer m) {
		super(m);
	}

	/**
	 * By default, commands are not move commands.  This method needs to be overridden in any
	 * command that is a move command (i.e. must be overridden to return true if the action can potentially change the actor's location).
	 * 
	 * @return false
	 */
	public boolean isMoveCommand() {
		return false;
	}
	
	/**
	 * Execute the action by the actor. If the Actor is not a HobbitActor, is dead or is null this method does nothing
	 * 
	 * TODO A cleaner way to make sure dead actors don't act would be to remove the event of the dead actor from the scheduled events
	 */
	public void execute(Actor<?> a) {
		if (a instanceof HobbitActor && !((HobbitActor) a).isDead()) 
			act((HobbitActor) a);

	}

	public abstract void act(HobbitActor a);
	
	public int compareTo(HobbitActionInterface other) {
		return this.getDescription().compareTo(other.getDescription());
	}

	/**
	 * Getter for the entity manager which keeps track of Entities and their Locations
	 * 
	 * @return entityManager of the world
	 */
	public static EntityManager<HobbitEntityInterface, HobbitLocation> getEntitymanager() {
		return entityManager;
	}


}
