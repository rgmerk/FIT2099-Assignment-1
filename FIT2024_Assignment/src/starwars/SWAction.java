package starwars;

import edu.monash.fit2099.simulator.matter.Action;
import edu.monash.fit2099.simulator.matter.Actor;
import edu.monash.fit2099.simulator.matter.EntityManager;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;

/**
 * Base class for all intransitive commands/<code>Actions</code> in the starwars world. 
 * This class is an implementation of the <code>SWActionInterface</code>
 * 
 * @author 	ram
 * @see 	edu.monash.fit2099.simulator.matter.Action
 * @see 	SWActionInterface
 */
/*
 * Change log
 * 2017-02-08: The execute will only call the act of the SWActor is alive (asel) 
 * 2017-02-20: Removed the redundant compareTo method. The compareTo method is already implemented in the 
 * 			   ActionInterface. (asel)
 */
public abstract class SWAction extends Action implements SWActionInterface {
	
	/**The entity manager of the world which keeps track of <code>SWEntities</code> and their <code>SWLocation</code>s*/
	private static final EntityManager<SWEntityInterface, SWLocation> entityManager = SWWorld.getEntitymanager();

	/**
	 * Constructor for the <code>SWAction</code>. 
	 * Will initialize the message renderer for this <code>SWAction</code>
	 * 
	 * @param m the <code>MessageRenderer</code> to display messages.
	 */
	public SWAction(MessageRenderer m) {
		super(m);
	}

	/**
	 * Returns if or not this <code>SWAction</code> is a move command.
	 * By default, <code>SWAction</code>s are not move commands hence this
	 * method will always return false.  
	 * <p>
	 * This method needs to be overridden in any <code>SWAction</code> is a move command 
	 * (i.e. must be overridden to return true if the action can potentially change the location
	 * of a <code>SWEntity</code> or <code>SWActor</code>).
	 * 
	 * @return false
	 */
	@Override
	public boolean isMoveCommand() {
		return false;
	}
	
	/**
	 * Execute this <code>SWAction</code> by calling the <code>act()</code> method with an <code>Actor a</code>
	 * to perform the <code>SWAction</code>.
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
	public void execute(Actor<?> a) {
		if (a instanceof SWActor && !((SWActor)a).isDead()) 
			act((SWActor) a);

	}

	@Override
	public abstract void act(SWActor a);
	

	/**
	 * Returns the <code>EntityManager</code> which keeps track of <code>SWEntities</code> and their <code>SWLocation</code>s
	 * 
	 * @return 	entityManager of the <code>World</code> 
	 * @see 	#entityManager
	 */
	public static EntityManager<SWEntityInterface, SWLocation> getEntitymanager() {
		return entityManager;
	}


}
