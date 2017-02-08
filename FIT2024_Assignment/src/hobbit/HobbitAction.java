/**
 * Base class for all intransitive commands in the Hobbit world.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: Extensions to the Javadoc
 * 2017-02-08: The execute will only call the act of the HobbitActor is alive
 * 
 */
package hobbit;

import edu.monash.fit2024.simulator.matter.Action;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public abstract class HobbitAction extends Action implements HobbitActionInterface {
	
	/**The entity manager of the world which keeps track of <code>HobbitEntity</code>s and their <code>HobbitLocation</code>s*/
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
	 * Execute the action by the actor. If the Actor is not a HobbitActor, is null or is a dead HobbitActor this method does nothing
	 * 
	 */
	public void execute(Actor<?> a) {
		if (a instanceof HobbitActor && !((HobbitActor)a).isDead()) 
			act((HobbitActor) a);

	}

	public abstract void act(HobbitActor a);
	
	/**
	 * compareTo method to order HobbitActionInterface objects in alphabetical order
	 */
	public int compareTo(HobbitActionInterface other) {
		return this.getDescription().compareTo(other.getDescription());
	}

	/**
	 * Getter for the <code>entityManager</code>
	 * 
	 * @return entityManager of the world @see {@link #entityManager}
	 */
	public static EntityManager<HobbitEntityInterface, HobbitLocation> getEntitymanager() {
		return entityManager;
	}


}
