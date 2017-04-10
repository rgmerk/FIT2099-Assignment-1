
package edu.monash.fit2099.simulator.space;


/**
 * Interface defining a <code>Location</code> factory.
 * <p>
 * This is needed because generic <code>LocationContainers</code> such as Grid can't
 * instantiate <code>Location</code> subclasses directly due to type erasure --
 * information about the type parameter is erased at compile time. We can
 * work around this by passing in a factory object.</p>
 * <p>
 * We could pass in a Class object instead for slightly simpler code, but
 * this method allows better checked exception handling.  We can also call
 * nondefault constructors more easily.
 * 
 * @author ram
 * @param <T> <code>Location</code> subclass for factory
 */
public interface LocationMaker<T extends Location> {
	public T make();
}