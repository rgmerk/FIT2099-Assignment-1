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
 * 2017-02-08: Removed the removeEventsMethod as it's no longer required.
 * 			   Removed the tick and act methods for HobbitActor as they are never called
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
	
	/**the <code>Team</code> to which this <code>HobbitActor</code> belongs to**/
	private Team team;
	
	/**The amount of <code>hitpoints</code> of this actor. If the hitpoints are zero or less this <code>Actor</code> is dead*/
	private int hitpoints;
	
	/**The world this <code>HobbitActor</code> belongs to.*/
	protected MiddleEarth world;
	
	/**Scheduler to schedule this <code>HobbitActor</code>'s events*/
	protected static Scheduler scheduler;
	
	/**The item carried by this <code>HobbitActor</code>. <code>itemCarried</code> is null if this <code>HobbitActor</code> is not carrying an item*/
	private HobbitEntityInterface itemCarried;
	
	/**If or not this <code>HobbitActor</code> is human controlled. <code>HobbitActor</code>s are not human controlled by default*/
	protected boolean humanControlled = false;
	
	/**A string symbol that represents this <code>HobbitActor</code>, suitable for display*/
	private String symbol;
	
	/**A set of <code>Capability</code>s of this <code>HobbitActor</code>*/
	private HashSet<Capability> capabilities;
	
	/**
	 * Constructor for the Hobbit Actor 
	 * 
	 * @param team @see {@link #team}
	 * @param hitpoints @see {@link #hitpoints}
	 * @param m	message renderer for the actor to display messages
	 * @param world @see {@link #world}
	 */
	public HobbitActor(Team team, int hitpoints, MessageRenderer m, MiddleEarth world) {
		super(m);
		actions = new HashSet<HobbitAction>();
		this.team = team;
		this.hitpoints = hitpoints;
		this.world = world;
		this.symbol = "@";
		
		//HobbitActors are given the Attack affordance hence they can be attacked
		HobbitAffordance attack = new Attack(this, m);
		this.addAffordance(attack);
	}
	
	/**
	 * Setter for the scheduler
	 * 
	 * @param s @see {@link #scheduler}
	 */
	public static void setScheduler(Scheduler s) {
		scheduler = s;
	}
	
	/**
	 * Getter for the team
	 * 
	 * @return @see {@link #team}
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Getter for hitpoints
	 * 
	 * @return @see {@link #hitpoints}
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**Getter for the item carried
	 * 
	 * @return @see {@link #itemCarried}
	 */
	public HobbitEntityInterface getItemCarried() {
		return itemCarried;
	}

	/**Setter for the team
	 * 
	 * @param team @see {@link #team}
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Method insists damage on this <code>HobbitActor</code> by reducing a certain amount of <code>damage</code> from this  <code>HobbitActor</code>'s <code>hitpoints</code>
	 * 
	 * @param damage the amount of <code>hitpoints</code> to be reduced
	 */
	public void takeDamage(int damage) {
		//assertion to ensure the damage is not negative. Negative damage could increase the HobbitActor's hitpoints
		assert (damage >= 0)	:"damage on HobbitActor must not be negative";
		
		this.hitpoints -= damage;
	}

	/**
	 * Setter for the item carried by this <code>HobbitActor</code>
	 * <p>
	 * This method will replace items already held by the <code>HobbitActor</code> with the <code>target</code>
	 * 
	 * @param target the new item to be set as item carried
	 * @see {@link #itemCarried}
	 */
	public void setItemCarried(HobbitEntityInterface target) {
		this.itemCarried = target;
	}
	
	
	/**
	 * Returns true if this actor is dead, false otherwise.
	 * 
	 * @author ram
	 * @return true if and only if this actor is dead
	 * @see {@link #hitpoints}
	 */
	public boolean isDead() {
		//an actor is dead if their hit points are less than or equal to 0
		return hitpoints <= 0;
	}
	
	/**
	 * Getter for the symbol
	 * 
	 * @returns symbol @see {@link #symbol}
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Setter for the symbol of the Actor
	 * 
	 * @param s @see {@link #symbol} 
	 *
	 */
	public void setSymbol(String s) {
		assert (!s.matches(".") && (s.length()==1)):"Symbol should be a single charater and not a '.'";
		symbol = s;
	}
	
	/**
	 * Returns if or not the <code>HobbitActor</code> is human controlled.
	 * 
	 * @return true if the <code>HobbitActor</code> is controlled by a human, false otherwise
	 * @see {@link #humanControlled}
	 */
	public boolean isHumanControlled() {
		return humanControlled;
	}
	
	/**
	 * Returns true if this actor has the given capability <code>c</code>, false otherwise.
	 * 
	 * Wrapper for HashSet<Capability>.contains().
	 * 
	 * @author ram
	 * @param c the Capability to search for
	 */
	public boolean hasCapability(Capability c) {
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
														  //(@see #CompassBearing enum class in edu.monash.fit2024.gridworld.Grid.java)
														  
			if (loc.getNeighbour(d) != null) //see if there is a neighbor to the current location of the actor in that direction d
				newActions.add(new Move(d,messageRenderer, world)); //add new move action that will allow the actor to move in that direction (d) to newActions
		}
		
		// replace old action list with new
		this.actions = newActions;		
		
		// TODO: This assumes that the only actions are the Move actions. This will clobber any others. Needs to be fixed.
		/* Actually, that's not the case: all non-movement actions are transferred to newActions before the movements are transferred. --ram */
	}


	
	
	
}
