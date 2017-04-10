package starwars.actions;

import starwars.SWAffordance;
import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.entities.Fillable;

/**
 * An Affordance for dipping things into large bodies of liquid.
 * 
 * The affordance is offered by the large bodies of liquid and can only be
 * applied in partnership with an <code>Entity</code> that is  <code>Fillable</code>
 * and implements the <code>Fill</code> interface.
 * 
 * @author Robert Merkel
 * @see {@link starwars.actions.Fill}
 * @see {@link starwars.entities.Fillable}
 */
public class Dip extends SWAffordance implements SWActionInterface {

	public Dip(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isMoveCommand() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDo(SWActor a) {
		SWEntityInterface item = a.getItemCarried();
		if (item!= null) {
			return item.hasCapability(Capability.FILLABLE);
		}
		return false;
	}

	@Override
	public void act(SWActor a) {
		SWEntityInterface item = a.getItemCarried();
		assert(item instanceof Fillable);

		for(Affordance aff: item.getAffordances()) {
			if (aff instanceof Fill) {
				aff.execute(a);
			}
		}
		a.say(item.getShortDescription() + "has been refilled to capacity");
	}
	
	@Override
	public String getDescription() {
		return "dip carried item in" + target.getShortDescription();
	}
}
