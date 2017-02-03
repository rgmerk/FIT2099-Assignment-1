
/**
 * Command to attack entities.
 * 
 * This affordance is attached to all attackable entities
 * 
 * @author David.Squire@monash.edu (dsquire)
 */
/*
 * Change log
 * 2017/02/03	Fixed the bug where the an actor could attack another actor in the same team (asel)
 */
package hobbit.actions;

import hobbit.Capability;
import hobbit.HobbitActionInterface;
import hobbit.HobbitActor;
import hobbit.HobbitAffordance;
import hobbit.HobbitEntityInterface;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class Attack extends HobbitAffordance implements HobbitActionInterface {

	
	/**
	 *
	 * Constructor for the Attack class
	 * @param theTarget the target being attacked
	 * @param m message renderer to display messages
	 */
	public Attack(HobbitEntityInterface theTarget, MessageRenderer m) {
		super(theTarget, m);
		
	}


	@Override
	/**
	 * @return The duration of the Attack action. Currently hard coded to return 1
	 */
	public int getDuration() {
		return 1;
	}

	@Override
	/**
	 * A String describing what this action will do, suitable for display in a user interface
	 * 
	 * @return String comprising "attack " and the short description of the target of this Affordance
	 */
	public String getDescription() {
		return "attack " + this.target.getShortDescription();
	}


	@Override
	/**
	 * Determine whether a particular actor can attack the target.
	 * 
	 * @author dsquire
	 * @param a the actor being queried
	 * @return true: any actor can always try an attack, it just won't do much good unless the actor has a suitable weapon
	 */
	public boolean canDo(HobbitActor a) {
		
		return true;
	}

	@Override
	/**
	 * Perform the Attack command on an entity.
	 * 
	 * This damages the entity attacked, tires the attacker, and blunts any weapon used for the attack
	 * 
	 * @author dsquire
	 *  - adapted from the equivalent class in the old Eiffel version
	 * @param the actor who is attacking
	 */
	public void act(HobbitActor a) {
		HobbitEntityInterface target = this.getTarget();
		boolean targetIsActor = target instanceof HobbitActor;
		HobbitActor targetActor = null;
		int energyForAttackWithWeapon = 1;//the amount of energy required to attack with a weapon
		
		if (targetIsActor) {
			targetActor = (HobbitActor) target;
		}
					
		
		if (targetIsActor && (a.getTeam() == targetActor.getTeam())) {
			//don't attack HobbitActors in the same team
			a.say("\t" + a.getShortDescription() + " says: Silly me! We're on the same team, " + target.getShortDescription() + ". No harm done");
		}
		else if (a.isHumanControlled() // a human-controlled player can attack anyone
			|| (targetIsActor && (a.getTeam() != targetActor.getTeam())) // others will only attack actors on different teams
				) { 
			a.say(a.getShortDescription() + " is attacking " + target.getShortDescription() + "!");
			HobbitEntityInterface itemCarried = a.getItemCarried();
			if (itemCarried != null) {//if the actor is carrying an item 
				if (itemCarried.hasCapability(Capability.WEAPON)) {
					target.takeDamage(itemCarried.getHitpoints() + 1); // blunt weapon won't do much, but it will still do some damage
					itemCarried.takeDamage(1); // weapon gets blunt
					a.takeDamage(energyForAttackWithWeapon); // actor uses energy to attack
				}
				else {
					if (targetIsActor) {
						targetActor.say("\t" + targetActor.getShortDescription()
								+ " is amused by " + a.getShortDescription()
								+ "'s attempted attack with "
								+ itemCarried.getShortDescription());
					}
				} // if is carrying a weapon
			} // if carrying something
			else { // attack with bare hands
				target.takeDamage((a.getHitpoints()/20) + 1); // a bare-handed attack doesn't do much damage.
				a.takeDamage(2*energyForAttackWithWeapon); // actor uses energy. It's twice as tiring as using a weapon
			} // if carrying something
			if (a.isDead()) {//the actor who attacked is dead after the attack
			
				a.setLongDescription(a.getLongDescription() + ", that died of exhaustion while attacking someone");
				
				//remove the attack affordance of the dead actor so it can no longer be attacked
				a.removeAffordance(this);
				
				
			}
			if (this.getTarget().getHitpoints() <= 0) {  // can't use isDead(), as we don't know that the target is an actor
				target.setLongDescription(target.getLongDescription() + ", that was killed in a fight");
				
			
				//remove the attack affordance of the dead actor so it can no longer be attacked
				targetActor.removeAffordance(this);

				
			}
		} // not game player and different teams
		
	}
}
