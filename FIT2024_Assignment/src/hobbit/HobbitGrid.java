/**
 * Grid of HobbitLocations.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: Extensions to the Javadoc and added comments (asel)
 * 			   Possible bug fix where the location of width 8 used to display a location of width 7
 */
package hobbit;

import java.util.List;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.space.LocationMaker;

public class HobbitGrid extends Grid<HobbitLocation> {
	
	private static int locationWidth = 8;
	//the location width of 8 will create a location with 7 spaces
	//Is this a bug?
	
	public HobbitGrid(LocationMaker<HobbitLocation> factory) {
		super(10,10,factory);//would create a 10 by 10 grid with 100 (10*10) locations
	}

	/**
	 * Display the grid on the console using System.out
	 * 
	 * @param em the entity manager (which is used to manager the locations and the entities)
	 * @author ram
	 * 
	 */
	public void render(EntityManager<HobbitEntityInterface, HobbitLocation> em) {
		//string buffer would be the string that would eventually be displayed using the System.out.println
		//Multiple lines are achieved using new line characters \n that would be concatenated to the string
		String buffer = "";

		for (List<HobbitLocation> row : locations) {//for each row of locations
			for (HobbitLocation loc : row) {//for each location in the row. i.e. for column in each row
				
				StringBuffer emptyBuffer = new StringBuffer();
				char es = loc.getEmptySymbol(); //the empty symbol
				
				for (int i = 0; i < locationWidth - 3; i++) { //add empty symbol character to the buffer
															  //adding 2 less here because one space is reserved for the location symbol
															  //and one more for the colon : used to separate the location symbol and the symbol(s) of the contents of that location
					emptyBuffer.append(es);
				}
				
				//new buffer buf with a vertical line seperator |
				//Concatenated with the symbol of the location
				//concatenated with an colon :
				StringBuffer buf = new StringBuffer("|" + loc.getSymbol() + ":"); 
				
				//get the Contents of the location (entities of the location)
				List<HobbitEntityInterface> contents = em.contents(loc);
				
				
				if (contents == null || contents.isEmpty())//no contents (or entities) in that location
					buf.append(emptyBuffer);//add empty buffer to buf to complete the string buffer
				else {
					for (HobbitEntityInterface e: contents) { //for each entity in the contents
						buf.append(e.getSymbol());//add the symbol to buf
					}
				}
				buf.append(emptyBuffer); //add the empty buffer again since the symbols of the contents that were added might not actually fill the location upto locationWidth
				
				buf.setLength(locationWidth+1);//set the length of buf to the required locationWidth
											//setLength will set the buf to contain only the first locationWidth+1 characters in buf
											//possible bug fixed here. Needs confirmation (asel)
				
				buf.append("| ");//add the vertical line seperator to mark the end of that location
				
				buffer += buf; //add the buffer (buf) created for the location to the buffer (the buffer that will eventually be printed)
			}
			buffer += "\n";//all locations of the row has been addressed hence add a new line character \n to start the new row on a new line

		}

		System.out.println(buffer); //print the grid on the screen
	}
}
