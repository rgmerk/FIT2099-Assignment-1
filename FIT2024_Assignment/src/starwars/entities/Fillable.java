package starwars.entities;

/**
 * Interface for SWEntities fillable with water
 * 
 * All fillable objects must have capability FILLABLE.
 * @author Robert Merkel
 * @see {@link starwars.Capability}
 */
public interface Fillable {
	/**
	 * Fill this SWEntity with water
	 */
	void fill();
}
