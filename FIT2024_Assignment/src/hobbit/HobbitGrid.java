package hobbit;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.space.LocationMaker;

/**
 * Grid of <code>HobbitLocations</code>.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: 	Bug fix where the location of width 8 used to display a location of width 7
 * 2017-02-02: 	Removed the render method and the location width attributes. The rendering of the map
 * 				and displaying it is now the job of the UI. The dependency with EntityManager package was hence removed
 * 				and this resulted in a simpler HobbitGrid class (asel) 
 */
public class HobbitGrid extends Grid<HobbitLocation> {

	/**
	 * The constructor of the <code>HobbitGrid</code>. 
	 * Will create a 10 by 10 grid with 100 <code>HobbitLocations</code>
	 * 
	 * @param factory the maker of the <code>HobbitLocations</code>
	 */
	public HobbitGrid(LocationMaker<HobbitLocation> factory) {
		super(10,10,factory);
	}
	

}
