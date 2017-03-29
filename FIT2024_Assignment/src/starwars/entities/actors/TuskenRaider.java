package starwars.entities.actors;

import java.util.ArrayList;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.SWActor;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.Team;
import starwars.actions.Move;

public class TuskenRaider extends SWActor {

	private String name;
	
	public TuskenRaider(int hitpoints, String name, MessageRenderer m, SWWorld world) {
		super(Team.TUSKEN, 10, m, world);
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	@Override
	public void act() {
		say(describeLocation());
		ArrayList<Direction> possibledirections=new ArrayList<Direction>();
		
		// build a list of available directions 
		for(Grid.CompassBearing d: Grid.CompassBearing.values()) {
			if(SWWorld.getEntitymanager().seesExit(this, d)) {
				possibledirections.add(d);
			}
		}
		
		Direction heading = possibledirections.get((int) (Math.floor(Math.random() * possibledirections.size())));
		say(getShortDescription() + "is heading " + heading + "next.");
		Move myMove = new Move(heading, messageRenderer, world);

		scheduler.schedule(myMove, this, 1);


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
