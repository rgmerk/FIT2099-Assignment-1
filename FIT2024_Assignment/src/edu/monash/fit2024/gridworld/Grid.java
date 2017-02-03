package edu.monash.fit2024.gridworld;
/**
 * Grid: base class for a 2D array of Locations.
 * 
 * Generated using UML Lab
 * 
 * @author ram
 * @date 17 February 2013
 */
/*
 * Changelog
 * 
 * 2013-02-17: Initial version
 * 2013-02-18: introduced abstract base class so that simulations can have virtual
 * 		spaces of different dimensionalities, layouts, etc. (ram)
 * 2013-02-27: Moved into gridworld package. (ram)
 * 2013-03-07: Made abstract, since the render() method will be client specific. (ram)
 * 	    Added EntityManager parameter so that clients can render items if they like. (ram)
 * 2013-04-10: Gave CompassBearing the ability to map back and forth from degrees (ram)
 * 
 * 2017-01-20: 	Added comments to methods for better understanding (asel)
 * 				All such comments end with (asel)
 * 
 * 2017-02-03:	Added a getter method for the angle since it's required by the GUI to show move buttons in their corresponding directions
 */

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.space.LocationContainer;
import edu.monash.fit2024.simulator.space.LocationMaker;

public abstract class Grid<T extends Location> extends LocationContainer<T> {
	
	public static enum CompassBearing implements Direction {
		NORTH(0),//NORTH (0) means the direction NORTH has a compass bearing angle of 0 
		NORTHEAST(45),//NORTHEAST(45) means the direction NORTHEAST has a compass bearing angle of 45. Other directions are read in the same fashion
		EAST(90),
		SOUTHEAST(135),
		SOUTH(180),
		SOUTHWEST(225),
		WEST(270),
		NORTHWEST(315);
		
		/**Angle of the bearing. Measure in degrees in the clockwise direction starting North(0)*/
		private int angle;

		/**
		 * @author Asel
		 * @return @see {@link #angle}
		 */
		public int getAngle() {
			return angle;
		}

		/* (non-Javadoc)
		 * Constructor of a CompassBearing
		 * @param angle : the angle of the bearing in degrees (Between 0 and 360, measured in degrees)
		 */
		private CompassBearing(int angle) {
			//precondition to ensure the angle is between 0 and 360
			assert (angle >=0 && angle <= 360)	:"angle measured in degrees, should be between 0 and 360";
			
			this.angle = angle;
		}
		
		/**
		 * <p>This method will return the antipode (the opposite bearing) of a compass bearing</p>
		 * <p>Ex: Antipode of NORTH is SOUTH</p>
		 * 
		 * @param cb : the compass bearing for which the opposite bearing (antipode) needs to be found
		 * @return the opposite bearing (antipode) of the given compass bearing 'cb'
		 */
		public static CompassBearing opposite(CompassBearing cb) {
			
			//turn 180 degrees and round to an angle between 0 and 360
			int newAngle = (cb.angle + 180) % 360;
			
			//return the compass bearing of the angle
			return compassBearingOfAngle(newAngle);
		}
		
		/**
		 * This method will return the Compass bearing after performing an 'angle' turn clockwise
		 * @param angle : the size of the angle in degrees that needs to be turned clockwise
		 * @return the compass bearing after turning 'angle' degrees clockwise and rounding up to the nearing 45 degrees.
		 */
		public CompassBearing turn(int angle) {
			// round to closest multiple of 45
			int newAngle = (((angle + this.angle)/45) * 45) % 360;
			
			//return the compass bearing of the angle
			return compassBearingOfAngle(newAngle);
			
		}
		
		/**
		 * This method will return a random bearing from the set of compass bearings (NORTH,NORTHEAST,SOUTHEAST,SOUTH,SOUTHWEST...)
		 * @return a random compass bearing from the set of compass bearings available
		 */
		public static CompassBearing getRandomBearing() {
			return values()[(int)(Math.random() * values().length)];
		}
		
		/**
		 * Gets the corresponding bearing of the the angle given
		 * 
		 * @author Asel
		 * @param angle an angle for which the bearing needs to be found. Measured in degrees hence a value between 0 and 360 is expected
		 * @return the corresponding compass bearing of the angle
		 * @throws IllegalArgumentException if the angle passed doesn't correspond to a bearing
		 */
		private static CompassBearing compassBearingOfAngle(int angle){
			
			//go through all the bearings values
			for (CompassBearing dir: values()) {
				if (dir.angle == angle) //return the bearing if angle is the same as the bearing angle
					return dir;
			}
			
			//this should be impossible since the angle should correspond to a bearing
			throw new IllegalArgumentException();
		}
	}
	
	

	protected int height;
	protected int width;
	private LocationMaker<T> factory;

	// We can't instantiate a 2D array, but we can use a factory to populate a List.
	// Declared as a List because UML Lab has trouble identifying implementations of the
	// generic collections.
	protected List<List<T>> locations;
	
	/**
	 * Given coordinates (x, y), return Location at that position.
	 * 
	 * TODO: this is the place for an assert.
	 * 	Precondition: x < width
	 * 	Precondition: y < height
	 * Currently returns null if the preconditions fail.
	 * 
	 * @param x east/west coordinate of desired Location
	 * @param y north/south coordinate of desired Location
	 * 
	 * @author ram
	 */
	public T getLocationByCoordinates(int x, int y) {
		
		//assertions added to check the precondition 
		assert (x >=0 && x < width)	:"x coordinate is not within 0 and "+width;
		assert (y >=0 && y < height):"y coordinate is not within 0 and "+height;
		
		if (x < width && y < height) //the method checks the precondition again?
			return locations.get(y).get(x);
		return null;
	}
	
	/**
	 * Accessor method to return the height of the Grid. Grid height of 'n' means the grid would have 'n' rows
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Accessor method to return the width of the Grid. Grid width of 'n' means the grid would have 'n' columns
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Creates a two-way path between loc1 and loc2 in direction d.  Sets
	 * up loc2 as the neighbour of loc1 in direction d, and loc1 as the neighbour of loc2 in
	 * direction Direction.opposite(d).
	 * 
	 * @author ram
	 * @param loc1 one end of the path
	 * @param loc2 other end of the path
	 * @param d the direction of the path
	 */
	protected void placePaths(Location loc1, Location loc2, CompassBearing cb) {
		loc1.addNeighbour(cb, loc2);
		loc2.addNeighbour(CompassBearing.opposite(cb), loc1);//setting the two way path by creating one in the opposite direction
	}
	
	/**
	 * Constructor that instantiates a Grid and sets up neighbour relationships
	 * between the Locations it contains.
	 * 
	 * @param x width of desired Grid
	 * @param y height of desired Grid
	 * @param maker factory for Location subclass
	 */
	public Grid(int x, int y, LocationMaker<T> maker) {
		height = y;
		width = x;
		factory = maker;

		locations = new ArrayList<List<T>>();

		for (int i = 0; i < height; i++) { //for each row of the grid
			List<T> row = new ArrayList<T>();
			locations.add(row);

			for (int j = 0; j < width; j++) {//for each column of the row
				
				//create and add a new location
				T newLoc = factory.make();
				row.add(newLoc);
				
				// place paths between new locations and already-created locations
				if (i > 0) {
					placePaths(newLoc, getLocationByCoordinates(j, i-1), CompassBearing.NORTH); //set a path to the location above 
					if (j > 0) {
						placePaths(newLoc, getLocationByCoordinates (j-1, i-1), CompassBearing.NORTHWEST);//place paths in a diagonal \ 
					}
					if (j < width-1) {
						placePaths(newLoc, getLocationByCoordinates(j+1, i-1), CompassBearing.NORTHEAST);//place paths in a diagonal /
					}
				}
				
				if (j > 0) {
					placePaths(newLoc, getLocationByCoordinates(j-1, i), CompassBearing.WEST); //set a path to the location on the left
				}
			}
		}
	}
}
