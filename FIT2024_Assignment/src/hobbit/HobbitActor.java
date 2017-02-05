/**
 * Class that represents an Actor (i.e. something that can perform actions) in the hobbit world.
 * 
 * @author ram
 * 
 * @modified 20130414 dsquire
 * 	- changed constructor so that affordances that all HobbitActors must have can be added
 * 	- changed team to be an enum rather than a string
 */
/*
 * Change log
 * 2017-01-20: Added missing Javadocs and improved comments (asel)
 */
package hobbit;

import hobbit.actions.Attack;
import hobbit.actions.Move;

import java.util.HashSet;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.gridworld.Grid.CompassBearing;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.time.Scheduler;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public abstract class HobbitActor extends Actor<HobbitAction> implements HobbitEntityInterface {
	private Team team;
	private int hitpoints;
	private HobbitEntityInterface itemCarried;
	protected MiddleEarth world;
	private String symbol;
	protected boolean humanControlled = false;
	private HashSet<Capability> capabilities;
	protected static Scheduler scheduler;
	
	// Constructor
	/**
	 * Constructor for the Hobbit Actor which creates a Hobbit Actor
	 * 
	 * @param team		: the team to which the New Hobbit Actor belong to.
	 * @param hitpoints	: The number of hitpoints to get started with. If the number of hit points 
	 * 					  for the Actor is initialized with 0 or a negative integer the actor starts dead 
	 * @param m			: message renderer for the actor to display messages
	 * @param world		: the world the actor belong to
	 */
	public HobbitActor(Team team, int hitpoints, MessageRenderer m, MiddleEarth world) {
		super(m);
		actions = new HashSet<HobbitAction>();
		this.team = team;
		this.hitpoints = hitpoints;
		this.world = world;
		this.symbol = "@";
		
		// Add affordances that all HobbitActors must have
		// All the Hobbit Actors can be attacked hence the Attack affordance is added
		// which allows these actors to *be attacked*
		HobbitAffordance attack = new Attack(this, m);
		this.addAffordance(attack);
	}
	
	//Accessors
	//setters and getters for the Hobbit Actor attributes
	public static void setScheduler(Scheduler s) {
		scheduler = s;
	}
	
	public void removeEvents(){
		scheduler.removeActorsEvents(this);
	}
	

	public Team getTeam() {
		return team;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * @return the item carried by the hobbit actor
	 */
	public HobbitEntityInterface getItemCarried() {
		return itemCarried;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Method insist damage by reducing a certain amount of damage from the actor's hit points
	 * 
	 * @param the amount of damage to be done on the actor. 
	 * 
	 *TODO: Add an assertion to make sure the damage given is a positive value or zero since a negative value can increase the hitpoints
	 */
	public void takeDamage(int damage) {
		//assertion to ensure the damage is not negative
		//this assertion might have to be removed if the actors can be healed, sounds like a hack
		assert (damage < 0)	:"damage on actor must not be negative";
		
		this.hitpoints -= damage;
	}

	/**
	 * <p>Set the item carried by the Hobbit Actor</p>
	 * <p>This method will replace any old items carried by the actor with the new the new target 
	 * hence it would be wise to make sure any items that are carried are returned before this method is implemented</p>
	 * @param target the new item to be set as item carried
	 */
	public void setItemCarried(HobbitEntityInterface target) {
		this.itemCarried = target;
	}
	
	
	/**
	 * Returns true if this actor is dead (which is when the actor's hit points have reached 0 or less)
	 * , false otherwise.
	 * 
	 * @author ram
	 * @return true if and only if this actor is dead
	 */
	public boolean isDead() {
		//an actor is dead if their hit points are less than or equal to 0
		return hitpoints <= 0;
	}
	
	/**
	 * Getter for the symbol
	 * 
	 * @returns symbol :symbol or string by which the actor is represented by
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Setter for the symbol of the Actor
	 *<p>Single character strings are preferred over multi character strings to avoid confusion in the text interface</p>
	 *
	 *<p>Try avoiding "." as an empty space is represented on the text interface using the ".", hence your Actor might not be visible 
	 * 	and be mistaken for an empty space </p>
	 * <p>These constraints are however for the current Text interface and would not have to be followed for a GUI for example</p>
	 * 
	 * TODO : assertions for a single character and .?
	 * 
	 * @param the symbol by which the actor is represented by. 
	 *
	 */
	public void setSymbol(String s) {
		assert (!s.matches(".") && (s.length()==1)):"Symbol should be a single charater and not a '.'";
		symbol = s;
	}
	
	/**
	 * Returns if or not the Hobbit Actor is controlled by a human
	 * @return true if the Actor is controlled by a human, false otherwise
	 */
	public boolean isHumanControlled() {
		return humanControlled;
	}
	
	/**
	 * Returns true if this actor has the given capability, false otherwise.
	 * 
	 * Wrapper for HashSet<Capability>.contains().
	 * 
	 * @author ram
	 * @param the Capability to search for
	 */
	public boolean hasCapability(Capability c) {
		//return true if the given Capability c is in the list of capabilities of the actor
		return capabilities.contains(c);
	}
	
	/**
	 * Polls the current location to find potential exits, and replaces all the instances of 
	 * Move in this actor's command set with moves to the new exits.
	 * 
	 * @author ram
	 * @param loc : the actor's location
	 */
	public void resetMoveCommands(Location loc) {
		HashSet<HobbitAction> newActions = new HashSet<HobbitAction>();
		
		// Copy all the existing non-movement options to newActions
		for (HobbitAction a: actions) {
			if (!a.isMoveCommand())
				newActions.add(a);
		}
		
		// add new movement possibilities
		for (CompassBearing d: CompassBearing.values()) { //for each CompassBearing d in the compass bearing values NORTH,NORTHEAST,EAST,SOUTHEAST,SOUTH....
														  //(@see CompassBearing enum class in edu.monash.fit2024.gridworld.Grid.java)
														  //see if there is a neighbor to the current location of the actor in that direction d
														  
			if (loc.getNeighbour(d) != null) //there is a neighbor in the direction d
				newActions.add(new Move(d,messageRenderer, world)); //add new move action that will allow the actor to move in that direction (d) to newActions
		}
		
		// replace old action list with new
		this.actions = newActions;		// TODO: This assumes that the only actions are the Move actions. This will clobber any others. Needs to be fixed.
						/* Actually, that's not the case: all non-movement actions are transferred to newActions before the movements are transferred. --ram */
	}


	/**
	 * Action to be performed when the actor is alive
	 */
	public abstract void act();
	
	/**
	 * Allow the actor to act.
	 * 
	 * @param the actor's current location
	 * @author ram
	 */
	/*public void tick(Location l) {
		act();
	}
	*/
	
	
}
