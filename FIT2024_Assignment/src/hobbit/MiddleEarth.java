/**
 * Class representing the Hobbit world.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-02:  Render method was removed from Middle Earth
 * 				Displaying the Grid is now handled by the TextInterface rather than by the Grid or MiddleWorld classes (asel)
 */
package hobbit;

import hobbit.actions.Take;
import hobbit.entities.actors.*;
import hobbit.entities.*;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.space.World;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;

public class MiddleEarth extends World {
	private HobbitGrid myGrid;
	
	private static final EntityManager<HobbitEntityInterface, HobbitLocation> entityManager = new EntityManager<HobbitEntityInterface, HobbitLocation>();
	
	public MiddleEarth() {
		HobbitLocation.HobbitLocationMaker factory = HobbitLocation.getMaker();
		myGrid = new HobbitGrid(factory);
		space = myGrid;
		
	}

	/** Accessor for the height of the grid.
	 * 
	 * @author ram
	 * @return the height of the grid
	 */
	public int height() {
		return space.getHeight();
	}
	
	/**
	 * Accessor for the width of the grid
	 * 
	 * @author ram
	 * @return the width of the grid
	 */
	public int width() {
		return space.getWidth();
	}
	
	/**
	 * Set up the world, setting descriptions for locations and placing items and actors
	 * on the grid.
	 * 
	 * @author ram
	 * @param iface a MessageRenderer to be passed onto newly-created entities
	 */
	public void initializeWorld(MessageRenderer iface) {
		HobbitLocation loc;
		// Set default location string
		for (int row=0; row < height(); row++) {
			for (int col=0; col < width(); col++) {
				loc = myGrid.getLocationByCoordinates(col, row);
				loc.setLongDescription("Middle Earth (" + col + ", " + row + ")");
				loc.setShortDescription("Middle Earth (" + col + ", " + row + ")");
				loc.setSymbol('.');				
			}
		}
		
		
		// The Shire
		for (int row = 5; row < 8; row++) {
			for (int col = 4; col < 7; col++) {
				loc = myGrid.getLocationByCoordinates(col, row);
				loc.setLongDescription("The Shire (" + col + ", " + row + ")");
				loc.setShortDescription("The Shire (" + col + ", " + row + ")");
				loc.setSymbol('S');
			}
		}
		
		//Bag End
		loc = myGrid.getLocationByCoordinates(5, 6);
		loc.setLongDescription("Bag End");
		loc.setShortDescription("Bag End");
		loc.setSymbol('b');
		// let's put an object here - just for a test
		HobbitEntity chainMail = new HobbitEntity(iface);
		chainMail.setShortDescription("chain mail");
		chainMail.setLongDescription("a small chain-mail suit");
		chainMail.setSymbol("c");
		chainMail.setHitpoints(5);
		// add a Take affordance to the chain mail, so that an actor can take it
		entityManager.setLocation(chainMail, loc);
		chainMail.addAffordance(new Take(chainMail, iface));
		// Bilbo
		Player bilbo = new Player(Team.GOOD, 100, iface, this);
		bilbo.setShortDescription("Bilbo");
		entityManager.setLocation(bilbo, loc);
		bilbo.resetMoveCommands(loc);
		
		/*
		// Asel
		Player asel = new Player(Team.GOOD, 100, iface, this);
		asel.setShortDescription("Asel The Legend");
		entityManager.setLocation(asel, loc);
		asel.resetMoveCommands(loc);
		*/
		
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
		HobbitEntity ring = new HobbitEntity(iface);
		ring.setShortDescription("a ring");
		ring.setLongDescription("a dully shining ring");
		ring.setSymbol("o");
		ring.setHitpoints(500);
		// add a Take affordance to the ring, so that an actor can take it
		entityManager.setLocation(ring, loc);
		ring.addAffordance(new Take(ring, iface));

		// a troll treasure
		loc = myGrid.getLocationByCoordinates(1,5);
		HobbitEntity treasure = new HobbitEntity(iface);
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
		TestActor testActor = new TestActor(iface, this);
		testActor.setShortDescription("Dorko the nobody");
		testActor.setSymbol("d");
		loc = myGrid.getLocationByCoordinates(3,3);
		entityManager.setLocation(testActor, loc);

	}

	/*
	 * Render method was removed from here
	 */
	
	/**
	 * Determine whether a given actor can move in a given direction.
	 * 
	 * @author ram
	 * @param a the actor
	 * @param whichDirection the direction if which they want to move
	 * @return true if the actor can see an exit in that direction, false otherwise.
	 */
	public boolean canMove(HobbitActor a, Direction whichDirection) {
		HobbitLocation where = (HobbitLocation)entityManager.whereIs(a); // requires a cast for no reason I can discern
		return where.hasExit(whichDirection);
	}
	
	/**
	 * Accessor for the grid.
	 * 
	 * @author ram
	 * @return the grid
	 */
	public HobbitGrid getGrid() {
		return myGrid;
	}

	/**
	 * Move an actor in a direction.
	 * 
	 * @author ram
	 * @param a the actor to move
	 * @param whichDirection the direction in which to move the actor
	 */
	public void moveEntity(HobbitActor a, Direction whichDirection) {
		
		//get the neighboring location in whichDirection
		Location loc = entityManager.whereIs(a).getNeighbour(whichDirection);
		
		// Base class unavoidably stores superclass references, so do a checked downcast here
		if (loc instanceof HobbitLocation)
			//perform the move action by setting the new location to the the neighboring location
			entityManager.setLocation(a, (HobbitLocation) entityManager.whereIs(a).getNeighbour(whichDirection));
	}

	/**
	 * Wrapper for entityManager.whereIs().
	 * 
	 * @author ram
	 * @param e the entity to find
	 * @return the Location of that entity, or null if it's not in the grid
	 */
	public Location find(HobbitEntityInterface e) {
		return entityManager.whereIs(e);
	}

	@SuppressWarnings("unchecked")
	/**
	 * This is only here for compliance with the abstract base class's interface and is not supposed to be
	 * called.
	 */
	public EntityManager<HobbitEntityInterface, HobbitLocation> getEntityManager() {
		return MiddleEarth.getEntitymanager();
	}

	public static EntityManager<HobbitEntityInterface, HobbitLocation> getEntitymanager() {
		return entityManager;
	}
}
