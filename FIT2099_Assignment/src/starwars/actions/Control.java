package starwars.actions;

import edu.monash.fit2099.gridworld.Grid.CompassBearing;
import edu.monash.fit2099.simulator.matter.EntityManager;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWAction;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.SWWorld;
import starwars.actions.Move;
/**
 *
 */

public class Control extends SWAffordance implements SWActionInterface {

	
	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Control(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);	
		priority = 1;
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
	 * Return the description of 
	 */
	@Override
	public String getDescription() {
		return "Control the mind of " + this.target.getShortDescription();
	}


	@Override
	public boolean canDo(SWActor a) {
		return !a.isDead();
	}
	
	
	@Override
	public void act(SWActor a) {
		SWWorld world = ((SWActor) a).getworld();
		world.moveEntity((SWActor) target, CompassBearing.NORTHWEST);
		a.say(a.getShortDescription() + "is controlling" +target.getShortDescription());
		
	}
}