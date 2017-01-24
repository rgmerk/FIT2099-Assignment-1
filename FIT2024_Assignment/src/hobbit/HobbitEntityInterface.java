/**
 * All Entities and Actors in the hobbit client package should implement this interface.
 * It allows them to be managed by the EntityManager.
 * 
 * @author ram
 */
package hobbit;

import edu.monash.fit2024.simulator.matter.EntityInterface;

public interface HobbitEntityInterface extends EntityInterface {

	public abstract String getSymbol();
	public abstract void setSymbol(String string);
	public boolean hasCapability(Capability c);
	public int getHitpoints();
	public void takeDamage(int damage);

}