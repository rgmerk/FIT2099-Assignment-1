/**
 * Class that represents an Actor (i.e. something that can perform actions) in the starwars world.
 * 
 * @author ram
 * 
 * @modified 20130414 dsquire
 * 	- changed constructor so that affordances that all SWActors must have can be added
 * 	- changed team to be an enum rather than a string
 */
/*
 * Change log
 * 2017-01-20: Added missing Javadocs and improved comments (asel)
 * 2017-02-08: Removed the removeEventsMethod as it's no longer required.
 * 			   Removed the tick and act methods for SWActor as they are never called
 */
package starwars;

import java.util.ArrayList;
import java.util.HashSet;

import edu.monash.fit2099.gridworld.Grid.CompassBearing;
import edu.monash.fit2099.simulator.matter.Actor;
import edu.monash.fit2099.simulator.matter.Affordance;
import edu.monash.fit2099.simulator.space.Location;
import edu.monash.fit2099.simulator.time.Scheduler;
import edu.monash.fit2099.simulator.userInterface.MessageRenderer;
import starwars.actions.Attack;
import starwars.actions.Move;

public abstract class SWActor extends Actor<SWActionInterface> implements SWEntityInterface {
	
	/**the <code>Team</code> to which this <code>SWActor</code> belongs to**/
	private Team team;
	
	/**The amount of <code>hitpoints</code> of this actor. If the hitpoints are zero or less this <code>Actor</code> is dead*/
	private int hitpoints;
	
	/**The world this <code>SWActor</code> belongs to.*/
	protected SWWorld world;
	
	/**Scheduler to schedule this <code>SWActor</code>'s events*/
	protected static Scheduler scheduler;
	
	/**The item carried by this <code>SWActor</code>. <code>itemCarried</code> is null if this <code>SWActor</code> is not carrying an item*/
	private SWEntityInterface itemCarried;
	
	/**If or not this <code>SWActor</code> is human controlled. <code>SWActor</code>s are not human controlled by default*/
	protected boolean humanControlled = false;
	
	/**A string symbol that represents this <code>SWActor</code>, suitable for display*/
	private String symbol;
	
	/**A set of <code>Capabilities</code> of this <code>SWActor</code>*/
	private HashSet<Capability> capabilities;
	
	/**
	 * Constructor for the <code>SWActor</code>.
	 * <p>
	 * The constructor initializes the <code>actions</code> list of this <code>SWActor</code>.
	 * <p>
	 * By default,
	 * <ul>
	 * 	<li>All <code>SWActor</code>s can be attacked.</li>
	 * 	<li>Have their symbol set to '@'</li>
	 * </ul>
	 * 
	 * @param 	team to which this <code>SWActor</code> belongs to
	 * @param 	hitpoints initial hitpoints of this <code>SWActor</code> to start with
	 * @param 	m	message renderer for this <code>SWActor</code> to display messages
	 * @param 	world the <code>World</code> to which <code>SWActor</code> belongs to
	 * 
	 * @see 	#team
	 * @see 	#hitpoints
	 * @see 	#world
	 * @see 	starwars.actions.Attack
	 */
	public SWActor(Team team, int hitpoints, MessageRenderer m, SWWorld world) {
		super(m);
		actions = new HashSet<SWActionInterface>();
		this.team = team;
		this.hitpoints = hitpoints;
		this.world = world;
		this.symbol = "@";
		
		//SWActors are given the Attack affordance hence they can be attacked
		SWAffordance attack = new Attack(this, m);
		this.addAffordance(attack);
	}
	
	/**
	 * Sets the <code>scheduler</code> of this <code>SWActor</code> to a new <code>Scheduler s</code>
	 * 
	 * @param	s the new <code>Scheduler</code> of this <code>SWActor</code> 
	 * @see 	#scheduler
	 */
	public static void setScheduler(Scheduler s) {
		scheduler = s;
	}
	
	/**
	 * Returns the team to which this <code>SWActor</code> belongs to.
	 * <p>
	 * Useful in comparing the teams different <code>SWActor</code> belong to.
	 * 
	 * @return 	the team of this <code>SWActor</code>
	 * @see 	#team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Returns the hit points of this <code>SWActor</code>.
	 * 
	 * @return 	the hit points of this <code>SWActor</code> 
	 * @see 	#hitpoints
	 * @see 	#isDead()
	 */
	@Override
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * Returns an ArrayList containing this Actor's available Actions, including the Affordances of items
	 * that the Actor is holding.
	 * 
	 * @author ram
	 */
	public ArrayList<SWActionInterface> getActions() {
		ArrayList<SWActionInterface> actionList = super.getActions();
		
		//If the HobbitActor is carrying anything, look for its affordances and add them to the list
		SWEntityInterface item = getItemCarried();
		if (item != null)
			for (Affordance aff : item.getAffordances())
				if (aff instanceof SWAffordance)
				actionList.add((SWAffordance)aff);
		return actionList;
	}
	
	/**
	 * Returns the item carried by this <code>SWActor</code>. 
	 * <p>
	 * This method only returns the reference of the item carried 
	 * and does not remove the item held from this <code>SWActor</code>.
	 * <p>
	 * If this <code>SWActor</code> is not carrying an item this method will return null.
	 * 
	 * @return 	the item carried by this <code>SWActor</code> or null if no item is held by this <code>SWActor</code>
	 * @see 	#itemCarried
	 */
	public SWEntityInterface getItemCarried() {
		return itemCarried;
	}

	/**
	 * Sets the team of this <code>SWActor</code> to a new team <code>team</code>.
	 * <p>
	 * Useful when the <code>SWActor</code>'s team needs to change dynamically during the simulation.
	 * For example, a bite from an evil actor makes a good actor bad.
	 *
	 * @param 	team the new team of this <code>SWActor</code>
	 * @see 	#team
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Method insists damage on this <code>SWActor</code> by reducing a 
	 * certain amount of <code>damage</code> from this <code>SWActor</code>'s <code>hitpoints</code>
	 * 
	 * @param 	damage the amount of <code>hitpoints</code> to be reduced
	 * @pre 	<code>damage</code> should not be negative
	 */
	@Override
	public void takeDamage(int damage) {
		//Precondition 1: Ensure the damage is not negative. Negative damage could increase the SWActor's hitpoints
		assert (damage >= 0)	:"damage on SWActor must not be negative";
		this.hitpoints -= damage;
	}

	/**
	 * Assigns this <code>SWActor</code>'s <code>itemCarried</code> to 
	 * a new item <code>target</code>
	 * <p>
	 * This method will replace items already held by the <code>SWActor</code> with the <code>target</code>.
	 * A null <code>target</code> would signify that this <code>SWActor</code> is not carrying an item anymore.
	 * 
	 * @param 	target the new item to be set as item carried
	 * @see 	#itemCarried
	 */
	public void setItemCarried(SWEntityInterface target) {
		this.itemCarried = target;
	}
	
	
	/**
	 * Returns true if this <code>SWActor</code> is dead, false otherwise.
	 * <p>
	 * A <code>SWActor</code> is dead when it's <code>hitpoints</code> are less than or equal to zero (0)
	 *
	 * @author 	ram
	 * @return 	true if and only if this <code>SWActor</code> is dead, false otherwise
	 * @see 	#hitpoints
	 */
	public boolean isDead() {
		return hitpoints <= 0;
	}
	

	@Override
	public String getSymbol() {
		return symbol;
	}
	

	@Override
	public void setSymbol(String s) {
		symbol = s;
	}
	
	/**
	 * Returns if or not this <code>SWActor</code> is human controlled.
	 * <p>
	 * Human controlled <code>SWActors</code>' <code>SWActions</code> are selected by the user as commands from the Views.
	 * 
	 * @return 	true if the <code>SWActor</code> is controlled by a human, false otherwise
	 * @see 	#humanControlled
	 */
	public boolean isHumanControlled() {
		return humanControlled;
	}
	

	@Override
	public boolean hasCapability(Capability c) {
		return capabilities.contains(c);
	}
	
	/**
	 * This method will poll this <code>SWActor</code>'s current <code>Location loc</code>
	 * to find potential exits, and replaces all the instances of <code>Move</code>
	 * in this <code>SWActor</code>'s command set with <code>Moves</code> to the new exits.
	 * <p>
	 * This method doesn't affect other non-movement actions in this <code>SWActor</code>'s command set.
	 *  
	 * @author 	ram
	 * @param 	loc this <code>SWActor</code>'s location
	 * @pre		<code>loc</code> is the actual location of this <code>SWActor</code>
	 */
	public void resetMoveCommands(Location loc) {
		HashSet<SWActionInterface> newActions = new HashSet<SWActionInterface>();
		
		// Copy all the existing non-movement options to newActions
		for (SWActionInterface a: actions) {
			if (!a.isMoveCommand())
				newActions.add(a);
		}
		
		// add new movement possibilities
		for (CompassBearing d: CompassBearing.values()) { 														  
			if (loc.getNeighbour(d) != null) //if there is an exit from the current location in direction d, add that as a Move command
				newActions.add(new Move(d,messageRenderer, world)); 
		}
		
		// replace command list of this SWActor
		this.actions = newActions;		
		
		// TODO: This assumes that the only actions are the Move actions. This will clobber any others. Needs to be fixed.
		/* Actually, that's not the case: all non-movement actions are transferred to newActions before the movements are transferred. --ram */
	}


	
	
	
}
