package starwars.entities.actors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.matter.Affordance;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Attack;
import starwars.actions.Move;
import starwars.hobbitinterfaces.SWGridController;

public class TuskenRaider extends SWActor {

	private String name;

	/**
	 * Create a Tusken Raider.  Tusken Raiders will randomly wander
	 * around the playfield (on any given turn, there is a 50% probability
	 * that they will move) and attack anything they can (if they can attack
	 * something, they will).  They 
	 * are all members of team TUSKEN, so their attempts to attack
	 * other Tusken Raiders won't be effectual.
	 * 
	 * @param hitpoints
	 *            the number of hit points of this Tusken Raider. If this
	 *            decreases to below zero, the Raider will die.
	 * @param name
	 *            this raider's name. Used in displaying descriptions.
	 * @param m
	 *            <code>MessageRenderer</code> to display messages.
	 * @param world
	 *            the <code>SWWorld</code> world to which this
	 *            <code>TuskenRaider</code> belongs to
	 * 
	 */
	public TuskenRaider(int hitpoints, String name, MessageRenderer m, SWWorld world) {
		super(Team.TUSKEN, 10, m, world);
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public void act() {
		say(describeLocation());

		// find out what's here
		SWLocation location = this.world.getEntityManager().whereIs(this);
		EntityManager<SWEntityInterface, SWLocation> em = this.world.getEntityManager();
		List<SWEntityInterface> entities = em.contents(location);

		// select the attackable things that are here

		HashSet<SWEntityInterface> attackables = new HashSet<SWEntityInterface>();
		for (SWEntityInterface e : entities) {
			// Even Tusken Raiders are smart enough to not attack themselves.
			if( e != this ) {
				for (Affordance a : e.getAffordances()) {
					if (a instanceof Attack) {

						attackables.add(e);
						break;
					}
				}
			}
		}

		// if there's at least one thing we can attack, randomly choose
		// something to attack
		if (attackables.size() > 0) {

			SWEntityInterface[] targets = attackables.toArray(new SWEntityInterface[0]);
			SWEntityInterface target = targets[(int) Math.floor(Math.random() * targets.length)];

			// get the attack affordance
			Affordance attack = null;
			for (Affordance a : target.getAffordances()) {
				if (a instanceof Attack) {
					attack = a;
					break;
				}
			}
			// schedule the attack
			say(getShortDescription() + " decides to attack " + target.getShortDescription());
			scheduler.schedule(attack, this, 1);
			
		} else if (Math.random() > 0.5){
			
			ArrayList<Direction> possibledirections = new ArrayList<Direction>();

			// build a list of available directions
			for (Grid.CompassBearing d : Grid.CompassBearing.values()) {
				if (SWWorld.getEntitymanager().seesExit(this, d)) {
					possibledirections.add(d);
				}
			}

			Direction heading = possibledirections.get((int) (Math.floor(Math.random() * possibledirections.size())));
			say(getShortDescription() + "is heading " + heading + " next.");
			Move myMove = new Move(heading, messageRenderer, world);

			scheduler.schedule(myMove, this, 1);
		}
	}

	@Override
	public String getShortDescription() {
		return name + " the Tusken Raider";
	}

	@Override
	public String getLongDescription() {
		return this.getShortDescription();
	}

	private String describeLocation() {
		SWLocation location = this.world.getEntityManager().whereIs(this);
		return this.getShortDescription() + " [" + this.getHitpoints() + "] is at " + location.getShortDescription();

	}
}
