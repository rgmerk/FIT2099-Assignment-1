package hobbit;
/**
 * Capabilities that various entities may have.  This is useful in <code>canDo()</code> methods of 
 * <code>HobbitActionInterface</code> implementations.
 *  
 * @author 	ram
 * @see 	{@link HobbitActionInterface}
 */

public enum Capability {
	CHOPPER,//CHOPPER capability allows an entity to Chop another entity which has the Chop Affordance
	WEAPON//WEAPON capability allows an entity to Attack another entity which has the Attack Affordance
	
}
