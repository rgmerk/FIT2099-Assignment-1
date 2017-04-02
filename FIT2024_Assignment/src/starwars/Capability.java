package starwars;
/**
 * Capabilities that various entities may have.  This is useful in <code>canDo()</code> methods of 
 * <code>SWActionInterface</code> implementations.
 *  
 * @author 	ram
 * @see 	{@link SWActionInterface}
 */

public enum Capability {
	CHOPPER,//CHOPPER capability allows an entity to Chop another entity which has the Chop Affordance
	WEAPON,//WEAPON capability allows an entity to Attack another entity which has the Attack Affordance
	FILLABLE,//FILLABLE capability allows an entity to be refilled by another entity that 
	            // has the Dip affordance
	DRINKABLE,//DRINKABLE capability allows an entity to be consumed by another entity 
	
}
