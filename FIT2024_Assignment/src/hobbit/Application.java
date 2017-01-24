/**
 * Driver class for the Hobbit package.  Contains nothing but a main().
 * 
 * @author ram
 */
package hobbit;

import edu.monash.fit2024.simulator.time.Scheduler;

public class Application {
	public static void main(String args[]) {
		
		MiddleEarth world = new MiddleEarth();
		TextInterface ui = new TextInterface(world);
		Scheduler theScheduler = new Scheduler(1, world);
		HobbitActor.setScheduler(theScheduler);
		
		// say hello
		TextInterface.showBanner();
		
		// set up the world
		world.initializeWorld(ui);
	
		
		// kick off the scheduler
		while(true) {
			world.displayGrid();
			theScheduler.tick();
		}
	}
	
	

}
