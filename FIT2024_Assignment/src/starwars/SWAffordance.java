package starwars;

import edu.monash.fit2099.simulator.matter.Actor;
import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.actions.Fill;

/**
 * Class that represents <code>Affordances</code> in the <code>starwars</code> world.
 * <p> 
 * This class implements such methods in <code>SWActionInterface</code> as can reasonably
 * be written at this stage, to minimize the amount of work that needs to be done to add new <code>Affordances</code>.
 * 
 * @author 	ram
 * @see 	edu.monash.fit2099.simulator.matter.Affordance
 * @see 	starwars.SWActionInterface
 */
/*
 * Change log
 * 2017-02-04 Added conditions in tick method to avoid dead actors from performing actions (asel)
 * 2017-02-20: Removed the redundant compareTo method. The compareTo method is already implemented in the 
 * 			   ActionInterface. (asel)
 */
public abstract class SWAffordance extends Affordance implements SWActionInterface {

	/**
	 * Constructor for the <code>SWAffordance</code>. 
	 * This would initialize the <code>MessageRenderer</code> and the target for this <code>SWAffordance</code>.
	 * 
	 * @param theTarget a sub class of <code>SWEntityInterface</code> on which the action needs to be performed on
	 * @param m the <code>messageRenderer</code> to display messages for this <code>SWAffordance</code>.
	 */
	public SWAffordance(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	/**
	 * Returns if or not this <code>SWAffordance</code> is a move command.
	 * By default, <code>SWAffordances</code> are not move commands hence this
	 * method will always return false.  
	 * <p>
	 * This method needs to be overridden in any <code>SWAffordance</code> is a move command 
	 * (i.e. must be overridden to return true if the action can potentially change the location
	 * of a <code>SWEntity</code> or <code>SWActor</code>).
	 * 
	 * @return false
	 */
	@Override
	public boolean isMoveCommand() {
		return false;
	}


	@Override
	public int getDuration() {
		return 1;
	}
	
	
	/**
	 * Accessor for the target of this <code>SWAffordance</code>. The target is a subclass of 
	 * <code>SWEntityInterface</code> on which the action is performed on.
	 * <p>
	 * This method checks that the target conforms to the <code>SWEntityInterface</code> and
	 * downcasts it to a SWEntityInterface and returns it if so; otherwise returns null.
	 * 
	 * @return the current target downcasted to <code>SWEntityInterface</code>
	 * 		   or null if the current target isn't a <code>SWEntityInterface</code>
	 */
	protected SWEntityInterface getTarget() {
		if (target instanceof SWEntityInterface)
			//return the downcasted target
			return (SWEntityInterface) target;
		
		return null;//if the target is not a SWEntityInterface
	}

	/**
	 * Execute this <code>SWAffordance</code> by calling the <code>act()</code> method with an <code>Actor a</code>
	 * to perform the <code>SWAffordance</code>.
	 * <p>
	 * This method acts as a wrapper for the <code>act()</code> method that downcasts its parameter 
	 * to minimize dangerous downcasting in subclasses.
	 * <p>
	 * This method only calls the <code>act()</code> method if and only,
	 * <ul>
	 * 	<li>The sub class of <code>Actor a</code> is a <code>SWActor</code>, and</li>
	 * 	<li>The <code>SWActor</code> is not dead</li>
	 * </ul>
	 * else this method does nothing.
	 * 
	 * @param 	a a sub class of <code>Actor</code>
	 * @see 	#act(SWActor)
	 * @see 	SWActor#isDead()
	 */
	@Override
	public void execute(Actor<?> actor) {
		if (this instanceof Fill) {
			System.out.println("Fill execute called");
		}
		if (actor instanceof SWActor && !((SWActor)actor).isDead())
			act((SWActor) actor);

	}
	
}
