package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Canteen;

/**
 * This function allows the c=training of a player
 * @author user
 *
 */
public class Train extends SWAffordance {

	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Train(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		//priority = 1;
	}


	
	@Override
	/**
	 * Increases the force power of a character
	 */
	public void act(SWActor a) {
		((SWActor) target).setForce(((SWActor) target).getForce() + 250);
				
				}
	


	@Override
	public boolean canDo(SWActor a) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public int getDuration() {
		return 2;
     }
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		
		return "";
	}
	
	


}
