/**
 * Driver class for the Hobbit package with Text Interface.  Contains nothing but a main().
 * 
 * @author ram
 */
/*
 * Change log
 * 2017/02/02	The TextInterface handles the responsibly of displaying the grid not the HobbitGrid or HobbitWorld classes (asel)
 */
package hobbit;

import edu.monash.fit2024.simulator.time.Scheduler;
import userinterfaces.GUInterface;
import userinterfaces.SimpleGUInterface;
import userinterfaces.TextInterface;

public class Application {
	public static void main(String args[]) {
		
		MiddleEarth world = new MiddleEarth();
		//TextInterface ui = new TextInterface(world);
		//SimpleGUInterface ui= new SimpleGUInterface(world);
		GUInterface ui= new GUInterface(world);
		Scheduler theScheduler = new Scheduler(1, world);
		HobbitActor.setScheduler(theScheduler);
		
		// say hello
		//TextInterface.showBanner();
		
		// set up the world
		world.initializeWorld(ui);
	
		// kick off the scheduler
		while(true) {
			ui.render();
			theScheduler.tick();
		}
		
		
	}
	
	

}
