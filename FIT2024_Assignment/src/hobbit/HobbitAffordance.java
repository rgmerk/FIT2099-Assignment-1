/**
 * Class that represents Affordances in the hobbit world.
 * 
 * Affordances are the actions that can be performed on an Entity
 * i.e An Entity with Attack affordance can be attacked and an Entity with a Chop affordance can be chopped etc..
 * 
 * This class implements such methods in HobbitActionInterface as can reasonably
 * be written at this stage, to minimize the amount of work that needs to be done to add new Affordances.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-04 Added conditions to avoid dead actors from performing actions
 */
package hobbit;

import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.Affordance;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public abstract class HobbitAffordance extends Affordance implements HobbitActionInterface {

	
	public HobbitAffordance(HobbitEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	@Override
	/**
	 * By default commands are non movement commands hence this method will return false
	 * If the command could potentially cause an Entity to change it's location,
	 * this method needs to be overridden to return true.
	 * 
	 * 
	 * @author ram
	 * @return false : by default meaning the command is not a move command
	 */
	public boolean isMoveCommand() {
		return false;
	}


	@Override
	public int getDuration() {
		return 1;
	}
	
	
	/**
	 * Accessor for the target of this affordance.
	 * 
	 * Checks that the target conforms to the HobbitEntityInterface and
	 * downcasts it to a HobbitEntityInterface and returns it if so; otherwise returns null.
	 * 
	 * @return the current target downcasted to HobbitEntityInterface
	 * 		   or null if the current target isn't a HobbitEntityInterface
	 */
	protected HobbitEntityInterface getTarget() {
		if (target instanceof HobbitEntityInterface)
			//return the downcasted target
			return (HobbitEntityInterface) target;
		return null;//if the target is not a HobbitEntityInterface
	}

	@Override
	/**
	 * Wrapper for the act() method that downcasts its parameter to minimize
	 * dangerous downcasting in subclasses.
	 * 
	 * @author ram
	 */
	public void execute(Actor<?> actor) {
		if (actor instanceof HobbitActor && !((HobbitActor) actor).isDead()) //added conditions to avoid dead actors from performing actions
			act((HobbitActor) actor);

	}
	
	/**
	 * Compares two HobbitActionInterfaces.
	 * 
	 * 
	 * The comparisons of the getDescriptions can be used to alphabetically sort the command lists for pretty output
	 * 
	 * @author ram
	 * @param the HobbitActionInterface to compare to
	 * @return 	0 if the strings returned from the getDescription methods for this and other are the same
	 * 			negative if the string returned by the getDescription method of this is less than the string 
	 * 				returned by the getDescription method of the other
	 * 			positive if the string returned by the getDescription method of this is greater than the string 
	 * 				returned by the getDescription method of the other
	 */
	public int compareTo(HobbitActionInterface other) {
		return this.getDescription().compareTo(other.getDescription());
	}
}
