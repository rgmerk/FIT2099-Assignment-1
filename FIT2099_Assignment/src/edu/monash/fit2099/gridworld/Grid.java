package edu.monash.fit2099.gridworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.simulator.space.Direction;
import edu.monash.fit2099.simulator.space.Location;
import edu.monash.fit2099.simulator.space.LocationContainer;
import edu.monash.fit2099.simulator.space.LocationMaker;

/**
 * <code>Grid</code> is a base class for a 2D array of <code>Locations</code>. It supports a maximum of 8-way movements in directions 
 * N, NE, E, SE, S, SW, W and NW. Movements might be limited for <code>Entities</code> at <code>Locations</code> that have 
 * less than 8 neighboring <code>Locations</code>.
 * <p>
 * <code>CompassBearing</code> is an inner enum class that manages the Directions of this <code>Grid</code>
 * <p>
 * Generated using UML Lab
 * 
 * @author 		ram
 * @date 		17 February 2013
 * 
 * @modified	Asel
 * @date		03 February 2017
 */
/*
 * Changelog
 * 
 * 2013-02-17: 	Initial version
 * 2013-02-18: 	introduced abstract base class so that simulations can have virtual
 * 				spaces of different dimensionalities, layouts, etc. (ram)
 * 2013-02-27: 	Moved into gridworld package. (ram)
 * 2013-03-07: 	Made abstract, since the render() method will be client specific. (ram)
 * 	    		Added EntityManager parameter so that clients can render items if they like. (ram)
 * 2013-04-10: 	Gave CompassBearing the ability to map back and forth from degrees (ram)
 * 2017-01-20: 	Added comments to methods for better understanding (asel)
 * 2017-02-03:	Added a getter method for the angle since it's required by the GUI to show move buttons in their corresponding directions
 * 2017-02-17:	Removed condition that checks preconditions in getLocationByCoordinates method. It is already handled by the assertions (Asel)
 */

public abstract class Grid<T extends Location> extends LocationContainer<T> {
	
	/**
	 * Class that manages the direction in which an <code>Entity</code> is heading as shown by a compass. Supports an 8-way movement 
	 * (N, NE, E, SE, S, SW, W, NW).
	 * <p>
	 * All <code>CompassBearings</code> are measured relative to the North (at 0 degrees) in a 360-degree system.
	 * 
	 */
	public static enum CompassBearing implements Direction {
		NORTH(0),
		NORTHEAST(45),
		EAST(90),
		SOUTHEAST(135),
		SOUTH(180),
		SOUTHWEST(225),
		WEST(270),
		NORTHWEST(315);
		
		/**
		 * Angle of the <code>CompassBearing</code>. 
		 * <p>
		 * The <code>angle</code> is measured in a 360-degree system, relative to the North in a clockwise direction.
		 */
		private int angle;
		
		/**
		 * List of valid angles in a 8-way movement geometry that corresponds to each of the 8 <code>CompassBearings</code>
		 * (0 for N, 45 for NE, 90 for E, 135 for SE, 180 for S, 225 for SW, 270 for W, 315 for NW)
		 */
		private static final List<Integer> validAngles = Arrays.asList(0,45,90,135,180,225,270,315);

		/**
		 * Getter for the <code>angle</code> of this <code>CompassBearing</code>.
		 * 
		 * @author Asel
		 * @return {@link #angle}
		 */
		public int getAngle() {
			return angle;
		}

		
		/**
		 * Constructor of a <code>CompassBearing</code>.
		 * 
		 * @param 	angle the angle of the compass bearing 
		 * @pre		<code>angle</code> should be between 0 and 360 inclusive
		 * @pre		<code>angle</code> should correspond to one of the 8 directions. 
		 * @see 	{@link #angle}
		 * @see 	{@link #validAngles} 			 
		 */
		private CompassBearing(int angle) {
						
			//Precondition 1 - Ensure the angle is between 0 and 360
			assert (angle >=0 && angle <= 360)	:"angle should be between 0 and 360 inclusive";
			
			//Precondition 2 - Ensure angle corresponds to one of the 8 directions
			assert (validAngles.contains(angle)):"angle should correpsond to one of the 8 directions";
			
			this.angle = angle;
		}
		
		/**
		 * This method will return the antipode (the opposite bearing) of a given <code>CompassBearing</code>
		 * <p>
		 * Ex: Antipode of NORTH is SOUTH
		 * 
		 * @param 	cb the <code>CompassBearing</code> for which the antipode needs to be found
		 * @return 	the antipode of the given compass bearing <code>cb</code>
		 * @post	the returned <code>CompassBearing</code> corresponds to one of the 8 directions
		 */
		public static CompassBearing opposite(CompassBearing cb) {
			
			//turn 180 degrees and round to an angle between 0 and 360
			int newAngle = (cb.angle + 180) % 360;
			
			//return the compass bearing of the angle
			return compassBearingOfAngle(newAngle);
		}
		
		/**
		 * This method will return the closest <code>CompassBearing</code> after turning <code>angle</code> amount clockwise.
		 * <p>
		 * The new angle after the turn will be rounded up to fall in a 360-degree system that matches of one of the 8 directions. 
		 * 
		 * @param 	angle the size of the angle in degrees that needs to be turned clockwise
		 * @return 	the <code>CompassBearing</code> after turning <code>angle</code> degrees clockwise
		 * @post	the returned <code>CompassBearing</code> corresponds to one of the 8 directions
		 */
		public CompassBearing turn(int angle) {
			// round to closest multiple of 45
			int newAngle = (((angle + this.angle)/45) * 45) % 360;
			
			//return the compass bearing of the angle
			return compassBearingOfAngle(newAngle);
			
		}
		
		/**
		 * This method will return a random <code>CompassBearing</code>.
		 *  
		 * @return 	a random <code>CompassBearing</code>
		 * @post 	the returned <code>CompassBearing</code> corresponds to one of the 8 directions
		 */
		public static CompassBearing getRandomBearing() {
			return values()[(int)(Math.random() * values().length)];
		}
		
		/**
		 * Gets the corresponding <code>CompassBearing</code> for a given angle <code>angle</code>.
		 * 
		 * @author 	Asel
		 * @param 	angle for which the <code>CompassBearing</code> needs to be found
		 * @pre		<code>angle</code> should be between 0 and 360 inclusive
		 * @pre		<code>angle</code> should correspond to one of the 8 directions. 
		 * @return 	the corresponding <code>CompassBearing</code> of the <code>angle</code>
		 * @throws 	IllegalArgumentException if the preconditions fail
		 * @see 	{@link #validAngles} 
		 */
		private static CompassBearing compassBearingOfAngle(int angle){
			
			//Precondition 1 - Ensure the angle is between 0 and 360
			assert (angle >=0 && angle <= 360)	:"angle should be between 0 and 360 inclusive";
			
			//Precondition 2 - Ensure angle corresponds to one of the 8 directions
			assert (validAngles.contains(angle)):"angle should correpsond to one of the 8 directions";
			
			for (CompassBearing dir: values()) {
				if (dir.angle == angle) //go through all the bearings values
					return dir;			//return the bearing if angle is the same as the bearing angle
			}
			
			//this should be impossible since the angle should correspond to a bearing
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Height of this <code>Grid</code>. 
	 * <p>
	 * Grid height of 'n' means the grid would have 'n' rows
	 */
	protected int height;
	
	/**
	 * Width of the <code>Grid</code>. 
	 * <p>
	 * Grid width of 'n' means the grid would have 'n' columns
	 */
	protected int width;
	
	/**
	 * Maker of <code>Locations</code>
	 */
	private LocationMaker<T> factory;

	/**
	 * A 2D array of <code>Locations</code>. Each element is a list of <code>Locations</code> of a row.
	 */
	/*
	 * We can't instantiate a 2D array, but we can use a factory to populate a List.
	 * Declared as a List because UML Lab has trouble identifying implementations of the
	 * generic collections.
	 */
	protected List<List<T>> locations;
	
	/**
	 * Method that returns the <code>Location</code> at the given coordinates (<code>x</code>, <code>y</code>) of this <code>Grid</code>.
	 * <p>
	 * Note that the origin (0,0) is at the top left hand corner of the grid and <code>y</code> 
	 * coordinates are always zero or positive and increase moving from North to South. <code>x</code> 
	 * coordinates are always zero or positive and increase moving from West to East.
	 * 
	 * @author 	ram
	 * @param 	x West/East coordinate of desired Location
	 * @param 	y North/South coordinate of desired Location
	 * @pre		<code>x</code> coordinate is greater than or equal to 0 and less than the <code>Width</code> of this <code>Grid</code>
	 * @pre		<code>y</code> coordinate is greater than or equal to 0 and less than the <code>Height</code> of this <code>Grid</code>
	 * @return	the <code>Location</code> at the given coordinates
	 * @see		{@link #width}
	 * @see		{@link #height}
	 */
	public T getLocationByCoordinates(int x, int y) {
		
		//Precondition 1 : for x coordinates
		assert (x >=0 && x < width)	:"x coordinate should be greater than or equal to 0 and less than the "+width;
		
		//Precondition 2 : for y coordinates
		assert (y >=0 && y < height):"y coordinate should be greater than or equal to 0 and less than the "+height;
		
		return locations.get(y).get(x);
		
		
	}
	
	/**
	 * Returns for the height of this <code>Grid</code>.
	 * <p>
	 * Height of this <code>Grid</code> is required by the Views/User Interfaces 
	 * (Implementations of <code>GridRenderer</code>) to display this <code>Grid</code> as a map.
	 * 
	 * @return 	Height of this <code>Grid</code>.
	 * @see		{@link #height}
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns for the width of this <code>Grid</code>
	 * <p>
	 * Width of this <code>Grid</code> is required by the Views/User Interfaces 
	 * (Implementations of <code>GridRenderer</code>) to display this <code>Grid</code> as a map.
	 * 
	 * @return 	Width of this <code>Grid</code>.
	 * @see		{@link #width}
	 */
	@Override
	public int getWidth() {
		return width;
	}
	
	/**
	 * Creates a two-way path between loc1 and loc2 in direction <code>cb</code>.  
	 * <p>
	 * Sets up <code>loc2</code> as the neighbour of <code>loc1</code> in direction <code>cb</code>, and <code>loc1</code> as 
	 * the neighbour of <code>loc2</code> in opposite direction of <code>cb</code>.
	 * 
	 * @author 	ram
	 * 
	 * @param 	loc1 one end of the path
	 * @param 	loc2 other end of the path
	 * @param 	cb the direction of the path as a <code>CompassBearing</code>
	 * @see		{@link CompassBearing#opposite(CompassBearing)}
	 */
	protected void placePaths(Location loc1, Location loc2, CompassBearing cb) {
		loc1.addNeighbour(cb, loc2);
		loc2.addNeighbour(CompassBearing.opposite(cb), loc1);//setting the two way path by creating one in the opposite direction
	}
	
	/**
	 * Constructor that instantiates a <code>Grid</code> and sets up neighbour relationships
	 * between the <code>Locations</code> it contains.
	 * 
	 * @param 	x width of this <code>Grid</code>
	 * @param 	y height of this <code>Grid</code>
	 * @param 	maker factory for Location subclass
	 * 
	 * @pre		Height <code>x</code> should be greater than 0
	 * @pre		Width <code>y</code> should be greater than 0
	 * 
	 * <p>
	 * TODO: Post conditions? - Asel
	 */
	public Grid(int x, int y, LocationMaker<T> maker) {
		//Precondition 1 : for height
		assert (x > 0)	:"Height x of should be greater than zero";
				
		//Precondition 2 : for width
		assert (y > 0)	:"Width y of should be greater than zero";
				
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
