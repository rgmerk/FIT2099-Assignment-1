/**
 * Grid of HobbitLocations.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: 	Bug fix where the location of width 8 used to display a location of width 7
 * 2017-02-02: 	Removed the render method and the location width attributes. The rendering of the map
 * 				and displaying it is now the job of the TextInterface class. The dependency with EntityManager package was hence removed
 * 				and his resulted in a simpler HobbitGrid class (asel) 
 */
package hobbit;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.space.LocationMaker;

public class HobbitGrid extends Grid<HobbitLocation> {

	
	public HobbitGrid(LocationMaker<HobbitLocation> factory) {
		super(10,10,factory);//would create a 10 by 10 grid with 100 (10*10) locations
	}
	

}
