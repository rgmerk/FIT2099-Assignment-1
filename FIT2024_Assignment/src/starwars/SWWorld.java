package starwars;

import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.space.World;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.actions.Take;
import starwars.entities.*;
import starwars.entities.actors.*;

/**
 * Class representing a world in the Star Wars universe. 
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-02:  Render method was removed from Middle Earth
 * 				Displaying the Grid is now handled by the TextInterface rather 
 * 				than by the Grid or MiddleWorld classes (asel)
 */
public class SWWorld extends World {
	
	/**
	 * <code>SWGrid</code> of this <code>SWWorld</code>
	 */
	private SWGrid myGrid;
	
	/**The entity manager of the world which keeps track of <code>SWEntities</code> and their <code>SWLocation</code>s*/
	private static final EntityManager<SWEntityInterface, SWLocation> entityManager = new EntityManager<SWEntityInterface, SWLocation>();
	
	/**
	 * Constructor of <code>SWWorld</code>. This will initialize the <code>SWLocationMaker</code>
	 * and the grid.
	 */
	public SWWorld() {
		SWLocation.SWLocationMaker factory = SWLocation.getMaker();
		myGrid = new SWGrid(factory);
		space = myGrid;
		
	}

	/** 
	 * Returns the height of the <code>Grid</code>. Useful to the Views when rendering the map.
	 * 
	 * @author ram
	 * @return the height of the grid
	 */
	public int height() {
		return space.getHeight();
	}
	
	/** 
	 * Returns the width of the <code>Grid</code>. Useful to the Views when rendering the map.
	 * 
	 * @author ram
	 * @return the height of the grid
	 */
	public int width() {
		return space.getWidth();
	}
	
	/**
	 * Set up the world, setting descriptions for locations and placing items and actors
	 * on the grid.
	 * 
	 * @author 	ram
	 * @param 	iface a MessageRenderer to be passed onto newly-created entities
	 */
	public void initializeWorld(MessageRenderer iface) {
		SWLocation loc;
		// Set default location string
		for (int row=0; row < height(); row++) {
			for (int col=0; col < width(); col++) {
				loc = myGrid.getLocationByCoordinates(col, row);
				loc.setLongDescription("SWWorld (" + col + ", " + row + ")");
				loc.setShortDescription("SWWorld (" + col + ", " + row + ")");
				loc.setSymbol('.');				
			}
		}
		
		
		// The Shire
		for (int row = 5; row < 8; row++) {
			for (int col = 4; col < 7; col++) {
				loc = myGrid.getLocationByCoordinates(col, row);
				loc.setLongDescription("Moisture Farm (" + col + ", " + row + ")");
				loc.setShortDescription("Moisture Farm (" + col + ", " + row + ")");
				loc.setSymbol('S');
			}
		}
		
		//Bag End
		loc = myGrid.getLocationByCoordinates(5, 6);
		loc.setLongDescription("Bag End");
		loc.setShortDescription("Bag End");
		loc.setSymbol('b');
		// let's put an object here - just for a test
		SWEntity chainMail = new SWEntity(iface);
		chainMail.setShortDescription("chain mail");
		chainMail.setLongDescription("a small chain-mail suit");
		chainMail.setSymbol("c");
		chainMail.setHitpoints(5);
		// add a Take affordance to the chain mail, so that an actor can take it
		entityManager.setLocation(chainMail, loc);
		chainMail.addAffordance(new Take(chainMail, iface));
		// Luke
		Player luke = new Player(Team.GOOD, 100, iface, this);
		luke.setShortDescription("Luke");
		entityManager.setLocation(luke, loc);
		luke.resetMoveCommands(loc);
		
		
		// The River Sherbourne
		for (int col = 3; col < 8; col++) {
			loc = myGrid.getLocationByCoordinates(col, 8);
			loc.setShortDescription("The River Sherboune (" + col + ", " + 8 + ")");
			loc.setLongDescription("The River Sherboune (" + col + ", " + 8 + ")");
			loc.setSymbol('R');
			loc.setEmptySymbol('~'); // to represent water
		}
		
		// Mirkwood Forest
		for (int row = 0; row < 10; row++) {
			for (int col = 8; col < 10; col++) {
				loc = myGrid.getLocationByCoordinates(col, row);
				loc.setLongDescription("Mirkwood Forest (" + col + ", " + row + ")");
				loc.setShortDescription("Mirkwood Forest (" + col + ", " + row + ")");
				loc.setSymbol('F');
				
				// forests have trees
				for (int i=0; i<4; i++)
					entityManager.setLocation(new Tree(iface), loc);				
			}
		}
		
		/*
		 * Scatter some other entities and actors around
		 */
		// a ring
		loc = myGrid.getLocationByCoordinates(3,1);
		SWEntity ring = new SWEntity(iface);
		ring.setShortDescription("a ring");
		ring.setLongDescription("a dully shining ring");
		ring.setSymbol("o");
		ring.setHitpoints(500);
		// add a Take affordance to the ring, so that an actor can take it
		entityManager.setLocation(ring, loc);
		ring.addAffordance(new Take(ring, iface));

		// a troll treasure
		loc = myGrid.getLocationByCoordinates(1,5);
		SWEntity treasure = new SWEntity(iface);
		treasure.setShortDescription("a treasure");
		treasure.setLongDescription("a troll treasure");
		treasure.setSymbol("×");
		treasure.setHitpoints(100);
		// add a Take affordance to the treasure, so that an actor can take it
		entityManager.setLocation(treasure, loc);
		treasure.addAffordance(new Take(treasure, iface));
		
		// "And my axe!"
		Axe axe = new Axe(iface);
		loc = myGrid.getLocationByCoordinates(5,5);
		entityManager.setLocation(axe, loc);
		
		// A sword
		Sword sword = new Sword(iface);
		loc = myGrid.getLocationByCoordinates(3, 4);
		entityManager.setLocation(sword, loc);
		
		// A bad guy
		Goblin goblin = new Goblin(iface, this);
		goblin.setShortDescription("Glorp the goblin");
		goblin.setSymbol("g");
		loc = myGrid.getLocationByCoordinates(7,7);
		entityManager.setLocation(goblin, loc);	
		
		// A target actor for testing actions
		TuskenRaider tim = new TuskenRaider(10, "Tim", iface, this);
		
		tim.setSymbol("T");
		loc = myGrid.getLocationByCoordinates(3,3);
		entityManager.setLocation(tim, loc);

	}

	/*
	 * Render method was removed from here
	 */
	
	/**
	 * Determine whether a given <code>SWActor a</code> can move in a given direction
	 * <code>whichDirection</code>.
	 * 
	 * @author 	ram
	 * @param 	a the <code>SWActor</code> being queried.
	 * @param 	whichDirection the <code>Direction</code> if which they want to move
	 * @return 	true if the actor can see an exit in <code>whichDirection</code>, false otherwise.
	 */
	public boolean canMove(SWActor a, Direction whichDirection) {
		SWLocation where = (SWLocation)entityManager.whereIs(a); // requires a cast for no reason I can discern
		return where.hasExit(whichDirection);
	}
	
	/**
	 * Accessor for the grid.
	 * 
	 * @author ram
	 * @return the grid
	 */
	public SWGrid getGrid() {
		return myGrid;
	}

	/**
	 * Move an actor in a direction.
	 * 
	 * @author ram
	 * @param a the actor to move
	 * @param whichDirection the direction in which to move the actor
	 */
	public void moveEntity(SWActor a, Direction whichDirection) {
		
		//get the neighboring location in whichDirection
		Location loc = entityManager.whereIs(a).getNeighbour(whichDirection);
		
		// Base class unavoidably stores superclass references, so do a checked downcast here
		if (loc instanceof SWLocation)
			//perform the move action by setting the new location to the the neighboring location
			entityManager.setLocation(a, (SWLocation) entityManager.whereIs(a).getNeighbour(whichDirection));
	}

	/**
	 * Returns the <code>Location</code> of a <code>SWEntity</code> in this grid, null if not found.
	 * Wrapper for <code>entityManager.whereIs()</code>.
	 * 
	 * @author 	ram
	 * @param 	e the entity to find
	 * @return 	the <code>Location</code> of that entity, or null if it's not in this grid
	 */
	public Location find(SWEntityInterface e) {
		return entityManager.whereIs(e); //cast and return a SWLocation?
	}

	/**
	 * This is only here for compliance with the abstract base class's interface and is not supposed to be
	 * called.
	 */

	@SuppressWarnings("unchecked")
	public EntityManager<SWEntityInterface, SWLocation> getEntityManager() {
		return SWWorld.getEntitymanager();
	}

	/**
	 * Returns the <code>EntityManager</code> which keeps track of the <code>SWEntities</code> and
	 * <code>SWLocations</code> in <code>SWWorld</code>.
	 * 
	 * @return 	the <code>EntityManager</code> of this <code>SWWorld</code>
	 * @see 	{@link #entityManager}
	 */
	public static EntityManager<SWEntityInterface, SWLocation> getEntitymanager() {
		return entityManager;
	}
}
