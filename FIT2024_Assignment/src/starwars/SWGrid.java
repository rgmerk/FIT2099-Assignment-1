package starwars;

import edu.monash.fit2099.gridworld.Grid;
import edu.monash.fit2099.simulator.space.LocationMaker;

/**
 * Grid of <code>SWLocation</code>s.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: 	Bug fix where the location of width 8 used to display a location of width 7
 * 2017-02-02: 	Removed the render method and the location width attributes. The rendering of the map
 * 				and displaying it is now the job of the UI. The dependency with EntityManager package was hence removed
 * 				and this resulted in a simpler SWGrid class (asel) 
 */
public class SWGrid extends Grid<SWLocation> {

	/**
	 * The constructor of the <code>SWGrid</code>. 
	 * Will create a 10 by 10 grid with 100 <code>SWLocation</code>s
	 * 
	 * @param factory the maker of the <code>SWLocation</code>s
	 */
	public SWGrid(LocationMaker<SWLocation> factory) {
		super(10,10,factory);
	}
	

}
