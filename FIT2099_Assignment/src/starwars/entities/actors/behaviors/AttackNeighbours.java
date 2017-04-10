package starwars.entities.actors.behaviors;

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.matter.EntityManager;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.actions.Attack;

public class AttackNeighbours {



	
	public static AttackInformation attackLocals(SWActor actor, SWWorld world, boolean avoidFriendlies, boolean avoidNonActors) {
		SWLocation location = world.getEntityManager().whereIs(actor);
		EntityManager<SWEntityInterface, SWLocation> em = world.getEntityManager();
		List<SWEntityInterface> entities = em.contents(location);

		// select the attackable things that are here

		ArrayList<AttackInformation> attackables = new ArrayList<AttackInformation>();
		for (SWEntityInterface e : entities) {
			// Figure out if we should be attacking this entity
			if( e != actor && 
					(e instanceof SWActor && 
							(avoidFriendlies==false || ((SWActor)e).getTeam() != actor.getTeam()) 
					|| (avoidNonActors == false && !(e instanceof SWActor)))) {
				for (Affordance a : e.getAffordances()) {
					if (a instanceof Attack) {

						attackables.add(new AttackInformation(e, a));
						break;
					}
				}
			}
		}

		// if there's at least one thing we can attack, randomly choose
		// something to attack
		if (attackables.size() > 0) {
			return attackables.get((int) (Math.floor(Math.random() * attackables.size())));
		} else {
			return null;
		}
	}
}
