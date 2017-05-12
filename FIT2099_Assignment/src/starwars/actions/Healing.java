package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWAction;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Canteen;

/**
 * This class implements the healing of actor
 * @author user
 *
 */
public class Healing extends SWAffordance {

	/**
	 * Constructor for the <code>Take</code> Class. Will initialize the message renderer, the target and 
	 * set the priority of this <code>Action</code> to 1 (lowest priority is 0).
	 * 
	 * @param theTarget a <code>SWEntity</code> that is being taken
	 * @param m the message renderer to display messages
	 */
	public Healing(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		priority = 1;
	}


	
	@Override
	/**
	 * If the hitpoint of a character is less than 1000 healing increases the hitpoint of the character
	 */
	public void act(SWActor a) {
		if (target instanceof SWEntityInterface) {
			if((a.getHitpoints() + ((Canteen)target).getHitpoints()) < 1000 ){
				a.setHitpoints(a.getHitpoints() +  ((Canteen)target).getHitpoints());
				Canteen.reduceLevel(-1);
			}
			else{
				a.setHitpoints(1000) ;
				
				}
		}
	}


	@Override
	public boolean canDo(SWActor a) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Healing from the Canteen " + target.getShortDescription();
	}



}
