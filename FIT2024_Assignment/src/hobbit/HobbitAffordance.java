package hobbit;

import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.Affordance;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

/**
 * Class that represents <code>Affordances</code> in the <code>hobbit</code> world.
 * <p> 
 * This class implements such methods in <code>HobbitActionInterface</code> as can reasonably
 * be written at this stage, to minimize the amount of work that needs to be done to add new <code>Affordances</code>.
 * 
 * @author 	ram
 * @see 	{@link edu.monash.fit2024.simulator.matter.Affordance}
 * @see 	{@link hobbit.HobbitActionInterface}
 */
/*
 * Change log
 * 2017-02-04 Added conditions in tick method to avoid dead actors from performing actions (asel)
 * 2017-02-20: Removed the redundant compareTo method. The compareTo method is already implemented in the 
 * 			   ActionInterface. (asel)
 */
public abstract class HobbitAffordance extends Affordance implements HobbitActionInterface {

	/**
	 * Constructor for the <code>HobbitAffordance</code>. 
	 * This would initialize the <code>MessageRenderer</code> and the target for this <code>HobbitAffordance</code>.
	 * 
	 * @param theTarget a sub class of <code>HobbitEntityInterface</code> on which the action needs to be performed on
	 * @param m the <code>messageRenderer</code> to display messages for this <code>HobbitAffordance</code>.
	 */
	public HobbitAffordance(HobbitEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
	}

	/**
	 * Returns if or not this <code>HobbitAffordance</code> is a move command.
	 * By default, <code>HobbitAffordances</code> are not move commands hence this
	 * method will always return false.  
	 * <p>
	 * This method needs to be overridden in any <code>HobbitAffordance</code> is a move command 
	 * (i.e. must be overridden to return true if the action can potentially change the location
	 * of a <code>HobbitEntity</code> or <code>HobbitActor</code>).
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
	 * Accessor for the target of this <code>HobbitAffordance</code>. The target is a subclass of 
	 * <code>HobbitEntityInterface</code> on which the action is performed on.
	 * <p>
	 * This method checks that the target conforms to the <code>HobbitEntityInterface</code> and
	 * downcasts it to a HobbitEntityInterface and returns it if so; otherwise returns null.
	 * 
	 * @return the current target downcasted to <code>HobbitEntityInterface</code>
	 * 		   or null if the current target isn't a <code>HobbitEntityInterface</code>
	 */
	protected HobbitEntityInterface getTarget() {
		if (target instanceof HobbitEntityInterface)
			//return the downcasted target
			return (HobbitEntityInterface) target;
		
		return null;//if the target is not a HobbitEntityInterface
	}

	/**
	 * Execute this <code>HobbitAffordance</code> by calling the <code>act()</code> method with an <code>Actor a</code>
	 * to perform the <code>HobbitAffordance</code>.
	 * <p>
	 * This method acts as a wrapper for the <code>act()</code> method that downcasts its parameter 
	 * to minimize dangerous downcasting in subclasses.
	 * <p>
	 * This method only calls the <code>act()</code> method if and only,
	 * <ul>
	 * 	<li>The sub class of <code>Actor a</code> is a <code>HobbitActor</code>, and</li>
	 * 	<li>The <code>HobbitActor</code> is not dead</li>
	 * </ul>
	 * else this method does nothing.
	 * 
	 * @param 	a a sub class of <code>Actor</code>
	 * @see 	{@link #act(HobbitActor)}
	 * @see 	{@link HobbitActor#isDead()}
	 */
	@Override
	public void execute(Actor<?> actor) {
		if (actor instanceof HobbitActor && !((HobbitActor)actor).isDead())
			act((HobbitActor) actor);

	}
	
}
