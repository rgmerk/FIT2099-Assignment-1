package starwars.entities.actors;

import java.util.List;
import java.util.Scanner;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWAffordance;
import starwars.SWEntity;
import starwars.SWEntityInterface;
import starwars.SWLegend;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Healing;
import starwars.actions.Leave;
import starwars.actions.Move;
import starwars.actions.Train;
import starwars.entities.Canteen;
import starwars.entities.LightSaber;
import starwars.entities.actors.behaviors.AttackInformation;
import starwars.entities.actors.behaviors.AttackNeighbours;
import starwars.entities.actors.behaviors.Patrol;
import starwars.SWLocation;


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
		super(Team.GOOD, 1000,1000, m, world);
		path = new Patrol(moves);
		this.setShortDescription("Ben Kenobi");
		this.setLongDescription("Ben Kenobi, an old man who has perhaps seen too much");
		LightSaber bensweapon = new LightSaber(m);
		setItemCarried(bensweapon);
		
		
	}

	public static BenKenobi getBenKenobi(MessageRenderer m, SWWorld world, Direction [] moves) {
		ben = new BenKenobi(m, world, moves);
		ben.activate();
		return ben;
	}
	
	@Override
	protected void legendAct() {

		if(isDead()) {
			return;
		}
		SWLocation location = this.world.getEntityManager().whereIs(this);
		//get the contents of the location
		List<SWEntityInterface> contents = this.world.getEntityManager().contents(location);
		AttackInformation attack;
		attack = AttackNeighbours.attackLocals(ben,  ben.world, true, true);
		
		if (attack != null) {
			say(getShortDescription() + " suddenly looks sprightly and attacks " +
		attack.entity.getShortDescription());
			scheduler.schedule(attack.affordance, ben, 1);
		}
		else if(contents.size() > 1){
			 // if it is equal to one, the only thing here is this Player, so there is nothing to report
				//say(this.getShortDescription() + " can see:");
				for (SWEntityInterface entity : contents) {
					// if Ben finds a canteen and he is holding a light Saber he will f=drop the light saber
					if (entity.getSymbol() == "o"  && ben.getHitpoints() < 1000 && itemCarried != null && ((Canteen) entity).getlevel()> 0) { // don't include self in scene description
						this.addAffordance(new Leave(this, messageRenderer));
						say(getShortDescription() + "is standing to drink from the canteen");
						
					}
					// if ben is not holding anything he will use the canteen to increase his hitpoints
					else if (entity.getSymbol() == "o"  && ben.getHitpoints() < 1000 && itemCarried == null && ((Canteen) entity).getlevel()> 0) { // don't include self in scene description
						this.addAffordance(new Healing(this, messageRenderer));
						say(getShortDescription() + "is drinking from the canteen");
				}
					// if ben finds Luke he asks for training luke
					else if (entity.getSymbol() == "@" && ((SWActor) entity).getForce() <1000 ) { 
						Scanner reader = new Scanner(System.in);  // Reading from System.in
						System.out.println("If you want to train Luke press 11 else any number: ");
						int n = reader.nextInt(); 
						// keeps on training luke until he is fully trained
						while (n == 11 && ((SWActor) entity).getForce() <1000){
							SWAffordance x = new Train(entity,messageRenderer);
							x.act(this);
							entity.say(entity.getShortDescription() + " gained " + ((SWActor) entity).getForce() );
							System.out.println("If you want to train Luke press 11 else any number: ");
							n = reader.nextInt();
						}
					}
					else {
						// Ben continues its patrol route
								Direction newdirection = path.getNext();
								say(getShortDescription() + " moves " + newdirection);
								Move myMove = new Move(newdirection, messageRenderer, world);

								scheduler.schedule(myMove, this, 1);
							}
				
				}	
		
		}
		
		else {
			Direction newdirection = path.getNext();
			say(getShortDescription() + " moves " + newdirection);
			Move myMove = new Move(newdirection, messageRenderer, world);

			scheduler.schedule(myMove, this, 1);
		}
		}
	

	@Override
	public void setHitpoints(int i) {
		// TODO Auto-generated method stub
		
	}

}
