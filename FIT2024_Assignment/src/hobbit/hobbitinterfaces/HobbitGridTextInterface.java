package hobbit.hobbitinterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.gridworld.GridRenderer;
import edu.monash.fit2024.simulator.matter.ActionInterface;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.EntityManager;
import hobbit.HobbitActionInterface;
import hobbit.HobbitActor;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitGrid;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;

/**
 * This is the text based user interface for the simulation. Is responsible for outputting a text based map and messages on the console
 * and obtaining user selection of commands from the console
 * <p>
 * Its operations are controlled by the <code>HobbitGridController</code> hence tightly coupled
 * 
 * @author Asel
 */
public class HobbitGridTextInterface implements GridRenderer {
	
	/**The grid of the world*/
	private static HobbitGrid grid;
	
	/**
	 * Constructor for the <code>HobbitGridTextInterface</code>
	 * 
	 * @pre 	grid should not be null
	 * @param 	grid the grid of the world
	 */
	public HobbitGridTextInterface(HobbitGrid grid) {
		
		assert grid != null : "grid should not be null";
		HobbitGridTextInterface.grid = grid;
	}
	
	
	/**
	 * Returns a string consisting of the symbol of the <code>HobbitLocation loc</code>, a colon ':' followed by 
	 * any symbols of the contents of the <code>HobbitLocation loc</code> and/or empty spaces of the <code>HobbitLocation loc</code>.
	 * <p>
	 * All string returned by this method are of a fixed length and doesn't contain any line breaks.
	 * 
	 * @author 	Asel
	 * @param 	loc for which the string is required
	 * @pre 	<code>loc</code> should not be null
	 * @pre		all symbols and empty spaces should not be line break characters
	 * @return 	a string in the format location symbol of <code>loc</code> + : + symbols of contents of <code>loc</code> + any empty characters of <code>loc</code>
	 * @post	all strings returned are of a fixed size
	 */
	private String getLocationString(HobbitLocation loc) {
		
		final EntityManager<HobbitEntityInterface, HobbitLocation> em = MiddleEarth.getEntitymanager();
		
		//all string would be of locationWidth length
		final int locationWidth = 8;
		
		StringBuffer emptyBuffer = new StringBuffer();
		char es = loc.getEmptySymbol(); 
		
		for (int i = 0; i < locationWidth - 2; i++) { 	//add two less as one character is reserved for the location symbol and the other for the colon (":")
			emptyBuffer.append(es);						
		}									  			
			
		//new buffer buf with a symbol of the location + :
		StringBuffer buf = new StringBuffer(loc.getSymbol() + ":"); 
		
		//get the Contents of the location
		List<HobbitEntityInterface> contents = em.contents(loc);
		
		
		if (contents == null || contents.isEmpty())
			buf.append(emptyBuffer);//add empty buffer to buf to complete the string buffer
		else {
			for (HobbitEntityInterface e: contents) { //add the symbols of the contents
				buf.append(e.getSymbol());
			}
		}
		buf.append(emptyBuffer); //add the empty buffer again since the symbols of the contents that were added might not actually filled the location upto locationWidth
		
		//set a fixed length
		buf.setLength(locationWidth);
		
		return buf.toString();		
	}
	
	@Override
	public void displayMap() {
				
		String buffer = "\n";
		final int gridHeight = grid.getHeight();
		final int gridWidth  = grid.getWidth();
		
	
		for (int row = 0; row< gridHeight; row++){ //for each row
			for (int col = 0; col< gridWidth; col++){ //each column of a row
				
				HobbitLocation loc = (HobbitLocation) grid.getLocationByCoordinates(col, row);
				
				//construct the string of a location to be displayed on the text interface
				buffer = buffer + "|"+ getLocationString(loc)+"| ";
			}
			buffer += "\n"; //new row
		}
		
		System.out.println(buffer); //print the grid on the screen
		
	}

	@Override
	public void displayMessage(String message) {
		System.out.println(message);		
	}


	@Override
	public ActionInterface getSelection(ArrayList<ActionInterface> cmds) {
		
		//assertion for the precondition
		assert cmds.size()>0:"command list for the actor is empty";
		
		Scanner instream = new Scanner(System.in);
					
		Collections.sort(cmds);//sorting the actions for a prettier output

		//construct the commands to be displayed in the console
		for (int i = 0; i < cmds.size(); i++) {
			System.out.println(i + 1 + " " + cmds.get(i).getDescription());
		}
		
		int selection = 0; //set to zero to trigger the loop
		while (selection < 1 || selection > cmds.size()) {//loop until a valid command has been obtained
			System.out.println("Enter command:");
			
			try{
				selection = (instream.nextInt());
			}catch (InputMismatchException e) { //catching any non integer inputs
			    instream.next(); // this consumes the invalid input
			}
		}
	
		return cmds.get(selection-1);//return the action selected		
	}
 	

}
