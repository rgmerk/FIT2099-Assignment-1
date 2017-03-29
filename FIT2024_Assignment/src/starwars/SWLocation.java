package hobbit;

import java.util.HashMap;
import edu.monash.fit2024.simulator.space.Direction;
import edu.monash.fit2024.simulator.space.Location;
import edu.monash.fit2024.simulator.space.LocationMaker;

/**
 * Class that models locations in Middle-Earth.
 * <p>
 * <code>HobbitLocations</code> are not restricted to 8-way movements. <I added this.Is this right? Asel>
 * 
 * @author ram
 */

public class HobbitLocation extends Location {
	
	/**A character that represents the <code>HobbitLocation</code>, suitable for display*/
	private char symbol;
	
	/**A character that represents an empty space in this <code>HobbitLocation</code>, suitable for display.
	 * By default empty spaces in all <code>HobbitLocations</code> are represented by a '.'
	 */
	private char emptySymbol = '.';
	
	/**A longer string that describes this <code>HobbitLocation</code>*/
	private String longDescription;
	
	/**A shorter string that describes this <code>HobbitLocation</code>*/
	private String shortDescription;
	
	
	/**
	 * Factory class used by <code>Grids</code> to instantiate <code>HobbitLocations</code>
	 * 
	 * @author ram
	 *
	 */
	public static class HobbitLocationMaker implements LocationMaker<HobbitLocation> {

		
		/**
		 * Factory method.
		 * 
		 * @author ram
		 * @return a new <code>HobbitLocation</code>
		 */
		@Override
		public HobbitLocation make() {
			return new HobbitLocation();
		}
		
	}

	/**
	 * Constructor for <code>HobbiLcoation</code>. Will initialize the list of neighboring references
	 */
	public HobbitLocation() {
		neighbours = new HashMap<Direction, Location>();
	}
	
	/** 
	 * A factory of a factory.
	 * Makes the <code>HobbitLocationMaker</code> which has a <code>make()</code> method that makes the <code>HobbitLocation</code>
	 * 
	 * @author ram
	 * @return an object with a <code>make()</code> method that can create <code>HobbitLocations</code>
	 */
	public static HobbitLocationMaker getMaker() {
		return new HobbitLocationMaker();
	}
	
	
	/**
	 * Returns a character that represents this <code>HobbitLocation</code>. 
	 * <p>
	 * The Views use this method to obtain the symbols that are used to query for resources(images of texture) and for display.
	 * 
	 * @author 	ram
	 * @return 	a char representing this <code>HobbitLocation</code>.
	 * @see 	{@link #symbol}
	 */
	public char getSymbol() {
		return symbol;
	}
	
	/**
	 * Sets the character symbol of this <code>HobbitLocation</code> to a new character <code>c</code>.
	 * <p>
	 * The Views use this symbols to query for resources(images of texture) and for display, hence although not a must
	 * symbols of different types of <code>HobbitLocations</code> are preferably unique.
	 * 
	 * @author 	ram
	 * @param 	c the new character symbol of this <code>HobbitLocation</code>
	 * @see 	{@link #symbol}
	 */
	public void setSymbol(char c) {
		symbol = c;
	}

	/**
	 * Returns a character that represents an empty space in this <code>HobbitLocation</code>.
	 * <p>
	 * <code>emptySymbols</code> are particularly used by text based Views (user interfaces)
	 * 
	 * @author 	dsquire
	 * @return 	a char representing empty space at this <code>HobbitLocation</code>
	 * @see 	{@link #emptySymbol}
	 */
	public char getEmptySymbol() {
		return emptySymbol;
	}
	
	/**
	 * Sets the <code>emptySymbol</code> of this <code>HobbitLocation</code> to a new
	 * character <code>c</code>
	 * <p>
	 * <code>emptySymbols</code> are particularly used by text based Views (user interfaces)
	 * 
	 * @author 	dsquire
	 * @param 	c the character to set the empty symbol to
	 * @see 	{@link #emptySymbol}
	 */
	public void setEmptySymbol(char c) {
		emptySymbol = c;
	}
	

	/**
	 * Sets the long description of this <code>HobbitLocation</code> to a new string <code>s</code>
	 * <p>
	 * Long description <code>s</code> should describe this <code>HobbitLocation</code> in general and 
	 * should not contain any information regarding what this location contains.
	 * 
	 * @param 	s the new long description string of this <code>HobbitLocation</code>
	 * @see 	{@link #longDescription}
	 */
	public void setLongDescription(String s) {
		longDescription = s;
	}
	
	/**
	 * Sets the short description of this <code>HobbitLocation</code> to a new string <code>s</code>
	 * <p>
	 * Short description <code>s</code> should describe this <code>HobbitLocation</code> in general and 
	 * should not contain any information regarding what this location contains.
	 * 
	 * @param 	s the new short description string of this <code>HobbitLocation</code>
	 * @see 	{@link #longDescription}
	 */
	public void setShortDescription(String s) {
		shortDescription = s;
	}
	
	/**
	 * Returns the long description of this <code>HobbitLocation</code>.
	 * 
	 * @return a string that describes this <code>HobbitLocation</code>
	 * @see {@link #longDescription}
	 */
	public String getLongDescription() {
		return longDescription;
	}
	
	/**
	 * Returns the long description of this <code>HobbitLocation</code>.
	 *  
	 * @return a string that describes this <code>HobbitLocation</code>
	 * @see {@link #longDescription}
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	


}
