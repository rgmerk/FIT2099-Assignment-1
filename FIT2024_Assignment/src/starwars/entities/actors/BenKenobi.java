package starwars.entities.actors;

import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.SWLegend;
import starwars.SWWorld;
import starwars.Team;
import starwars.entities.LightSaber;
import starwars.entities.actors.behaviors.AttackInformation;
import starwars.entities.actors.behaviors.AttackNeighbours;
import starwars.entities.actors.behaviors.Patrol;

/**
 * Ben (aka Obe-Wan) Kenobi.  
 * 
 * At this stage, he's an extremely strong critter with a <code>Lightsaber</code>
 * who wanders around in a fixed pattern and neatly slices any Actor not on his
 * team with his lightsaber.
 * 
 * Note that you can only create ONE Ben, like all SWLegends.
 * @author rober_000
 *
 */
public class BenKenobi extends SWLegend {

	private static BenKenobi ben = null; // yes, it is OK to return the static instance!
	private Patrol path;
	private BenKenobi(MessageRenderer m, SWWorld world, Direction [] moves) {
		super(Team.GOOD, 1000, m, world);
		path = new Patrol(moves);
		this.setShortDescription("Ben Kenobe");
		this.setLongDescription("Ben Kenobe, an old man who has perhaps seen too much");
		LightSaber bensweapon = new LightSaber(m);
		setItemCarried(bensweapon);
	}

	public static BenKenobi getBenKenobe(MessageRenderer m, SWWorld world, Direction [] moves) {
		ben = new BenKenobi(m, world, moves);
		ben.activate();
		return ben;
	}
	
	@Override
	protected void legendAct() {

		AttackInformation attack;
		attack = AttackNeighbours.attackLocals(ben,  ben.world, true, true);
		
		if (attack != null) {
			say(getShortDescription() + " suddenly looks sprightly and attacks " +
		attack.entity.getShortDescription());
			scheduler.schedule(attack.affordance, ben, 1);
		}
		else {
			Direction newdirection = path.getNext();
			say(getShortDescription() + " moves " + newdirection);
			
		}
	}

}
