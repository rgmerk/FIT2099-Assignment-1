package hobbit;

import edu.monash.fit2024.simulator.matter.Action;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

/**
 * Base class for all intransitive commands/<code>Actions</code> in the hobbit world. 
 * This class is an implementation of the <code>HobbitActionInterface</code>
 * 
 * @author 	ram
 * @see 	{@link edu.monash.fit2024.simulator.matter.Action}
 * @see 	{@link HobbitActionInterface}
 */
/*
 * Change log
 * 2017-02-08: The execute will only call the act of the HobbitActor is alive (asel) 
 * 2017-02-20: Removed the redundant compareTo method. The compareTo method is already implemented in the 
 * 			   ActionInterface. (asel)
 */
public abstract class HobbitAction extends Action implements HobbitActionInterface {
	
	/**The entity manager of the world which keeps track of <code>HobbitEntities</code> and their <code>HobbitLocations</code>*/
	private static final EntityManager<HobbitEntityInterface, HobbitLocation> entityManager = MiddleEarth.getEntitymanager();

	/**
	 * Constructor for the <code>HobbitAction</code>. 
	 * Will initialize the message renderer for this <code>HobbitAction</code>
	 * 
	 * @param m the <code>MessageRenderer</code> to display messages.
	 */
	public HobbitAction(MessageRenderer m) {
		super(m);
	}

	/**
	 * Returns if or not this <code>HobbitAction</code> is a move command.
	 * By default, <code>HobbitActions</code> are not move commands hence this
	 * method will always return false.  
	 * <p>
	 * This method needs to be overridden in any <code>HobbitAction</code> is a move command 
	 * (i.e. must be overridden to return true if the action can potentially change the location
	 * of a <code>HobbitEntity</code> or <code>HobbitActor</code>).
	 * 
	 * @return false
	 */
	@Override
	public boolean isMoveCommand() {
		return false;
	}
	
	/**
	 * Execute this <code>HobbitAction</code> by calling the <code>act()</code> method with an <code>Actor a</code>
	 * to perform the <code>HobbitAction</code>.
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
	public void execute(Actor<?> a) {
		if (a instanceof HobbitActor && !((HobbitActor)a).isDead()) 
			act((HobbitActor) a);

	}

	@Override
	public abstract void act(HobbitActor a);
	

	/**
	 * Returns the <code>EntityManager</code> which keeps track of <code>HobbitEntities</code> and their <code>HobbitLocations</code>
	 * 
	 * @return 	entityManager of the <code>World</code> 
	 * @see 	{@link #entityManager}
	 */
	public static EntityManager<HobbitEntityInterface, HobbitLocation> getEntitymanager() {
		return entityManager;
	}


}
