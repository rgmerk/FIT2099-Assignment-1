package edu.monash.fit2024.simulator.space;


/**
 * <p>Interface defining a Location factory.</p>
 * 
 * <p>This is needed because generic Location containers such as Grid can't
 * instantiate Location subclasses directly due to type erasure --
 * information about the type parameter is erased at compile time. We can
 * work around this by passing in a factory object.</p>
 * 
 * <p>We could pass in a Class object instead for slightly simpler code, but
 * this method allows better checked exception handling.  We can also call
 * nondefault constructors more easily.</p>
 * 
 * @author ram
 * @param <T> Location subclass for factory
 */
public interface LocationMaker<T extends Location> {
	public T make();
}