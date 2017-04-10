package starwars.actions;

import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntityInterface;
import starwars.entities.Fillable;

public class Fill extends SWAffordance {

	public Fill(SWEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canDo(SWActor a) {
		return false;
	}

	@Override
	public void act(SWActor a) {
		// TODO Auto-generated method stub
		assert(this.getTarget().hasCapability(Capability.FILLABLE));
		Fillable fillableTarget = (Fillable) (this.getTarget());
		fillableTarget.fill();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "";
	}

}
