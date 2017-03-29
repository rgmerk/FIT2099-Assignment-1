package hobbit;

import edu.monash.fit2024.simulator.time.Scheduler;
import hobbit.hobbitinterfaces.HobbitGridController;

/**
 * Driver class for the Hobbit package with <code>GridController</code>.  Contains nothing but a main().
 * 
 * @author ram
 */
/*
 * Change log
 * 2017-02-02	The TextInterface handles the responsibly of displaying the grid not the HobbitGrid or HobbitWorld classes (asel)
 * 2017-02-10	GridController controls the interactions with the user and will determine which UI it should use to do this. 
 * 			    Therefore there is tight coupling with the user interfaces and the driver. The application no longer has to worry about the
 * 				UI(asel)
 * 2017-02-19	Removed the show banner method. The text interface will deal with showing the banner. (asel)
 */

public class Application {
	public static void main(String args[]) {
		
		MiddleEarth world = new MiddleEarth();
		
		//Grid controller controls the data and commands between the UI and the model
		HobbitGridController uiController = new HobbitGridController(world);
		
		Scheduler theScheduler = new Scheduler(1, world);
		HobbitActor.setScheduler(theScheduler);
		
		// set up the world
		world.initializeWorld(uiController);
	
		// kick off the scheduler
		while(true) {
			uiController.render();
			theScheduler.tick();
		}
		
		
	}
	
	

}
