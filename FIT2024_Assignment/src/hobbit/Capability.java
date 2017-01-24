/**
 * Capabilities that various entities may have.  This is useful in canDo() methods.
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-01-20: Added comments (asel)
 */
package hobbit;

public enum Capability {
	CHOPPER,//CHOPPER capability allows an entity to Chop another entity which has the Chop Affordance
	WEAPON//WEAPON capability allows an entity to Attack another entity which has the Attack Affordance
	
}
