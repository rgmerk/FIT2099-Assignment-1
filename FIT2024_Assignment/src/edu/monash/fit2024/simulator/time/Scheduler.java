package edu.monash.fit2024.simulator.time;
/** 
 * This class handles time in the simulation by maintaining a priority queue of events, prioritized by time.
 * 
 * Event is an inner class that contains a Action, an Actor, and an integer denoting the scheduled event time.
 * 
 * @author ram
 */

// TODO: I'd like Scheduler to be a singleton, but its constructor needs parameters. --ram

/*
 * Changelog
 * 
 * 2013-03-07: removed World parameter from tick() method and made sure the universe member variable was correctly set in the constructor instead (ram)
 * 2013-03-09: changed condition in event processing loop to process events occurring at or before the new now instead of only before (ram)
 * 2017-01-20: Comments for the tick method(asel)
 * 2017-02-08: Changes to the compareTo method of the Event class to handle the new ordering of Events based on the priority of the Events Actions(asel)
 */

import java.util.PriorityQueue;

import edu.monash.fit2024.simulator.matter.Action;
import edu.monash.fit2024.simulator.matter.ActionInterface;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.space.World;
import hobbit.HobbitActor;

public class Scheduler {
	
	/**
	 * An Event is the execution of a Action, by an Actor, at a point in time.  
	 * 
	 * The Actor may be null in the case of simulations that allow the world to change automatically. 
	 * 
	 * @author ram
	 *
	 */	
	private class Event implements Comparable<Event> {
		private ActionInterface what;
		private Actor<?> who;
		private int when;
		
		/**
		 * Constructor for an Event object
		 * @param what : what Action to be performed as the event
		 * @param who : the actor of the event. The Actor may be null in the case of simulations that allow the world to change automatically.
		 * @param when : when the event should occur
		 */
		public Event(ActionInterface what, Actor<?> who, int when) {
			this.what = what;
			this.who = who;
			this.when = when;
		}
		
		//getters for the class attributes
		public ActionInterface getAction() {
			return what;
		}
		
		public Actor<?> getActor() {
			return who;
		}
		
		public int getTime() {
			return when;
		}
		
		/**
		 * Compare this against another Event, e.
		 * <p>
		 * Events will be compared by their time (<code>when</code>) and also by 
		 * the <code>priority</code> of their Actions
		 * 
		 *  
		 * @author 	ram
		 * @author 	Asel
		 * @date 	19 February 2013
		 * @date  	8 January 2017 (Modified)
		 * @param 	e the event to compare this Event to
		 * @return 	<ul>
		 * 				<li>0 if the events are simultaneous and their <code>Actions</code> have the same <code>priority</code></li>
		 * 				<li>a positive integer if, 
		 * 					<ul>
		 * 						<li>the event <code>this</code> happens before the <code>e</code> OR</li>
		 * 						<li>the event <code>this</code> and <code>e</code> happens at the same time but
		 * 							the <code>Action</code> of event <code>this</code> has higher <code>priority</code> than the <code>Action</code> of event <code>e</code></li>
		 * 					</ul>
		 * 				</li>
		 *	 			<li>a negative integer if, 
		 * 					<ul>
		 * 						<li>the event <code>this</code> happens after the <code>e</code> OR</li>
		 * 						<li>the event <code>this</code> and <code>e</code> happens at the same time but
		 * 							the <code>Action</code> of event <code>this</code> has lower <code>priority</code> than the <code>Action</code> of event <code>e</code></li>
		 * 					</ul>
		 * 				</li>
		 * 			</ul>
		 */
		public int compareTo(Event e) {
			//First sort by the time of the event
	        int timeResult = this.when - e.when;
	        
	        //if the comparison gave us a result i.e the events aren't simultaneous,
	        //then it's safe to return the results as it is
	        if (timeResult!=0){
	        	return timeResult;
	        }
	        
	        //if we are here then the events are simultaneous, hence must be sorted according to priority of the event's action
	        int priorityResult = e.getAction().getPriority() - this.getAction().getPriority();
	        
	        //NOTE: if the priority result is still 0, then we let the events happen in an arbitrary order
	        
	        //return the result
	        return priorityResult;
		}
		
		
	}
	
	private PriorityQueue<Event> events = new PriorityQueue<Event>();
	private int now = 0;
	private int ticksize;
	private World universe;
	
	
	/**
	 * Schedules an action by adding an event to the queue of events
	 * @param c : the action 
	 * @param a : the actor of the action or event. The Actor may be null in the case of simulations that allow the world to change automatically.
	 * @param duration : of the event (how long it takes for the event to complete)
	 */
	public void schedule(ActionInterface c, Actor<?> a, int duration) {
			
		int delay = 0;
		int cooldown = 0;
		
		if (a instanceof Actor){
			if(c instanceof Action){
				delay = ((Action) c).getDelay();
				cooldown = ((Action) c).getCooldown();
				int waittime = delay + cooldown;
				a.setWaittime(waittime);//set actor's wait time
				
				//add event to queue of events. Note for the actor the event will be scheduled to happen after the delay from now
				events.offer(new Event(c, a, now + delay));
			}
		}
		else{//Non actors or null
			//add event to queue of events. The event will happen after the duration from now
			events.offer(new Event(c, a, now + duration));
		}
		
		
		
	}
	
	/** Allow time to pass.  Process any events that are scheduled to go off between now and the next time tick in the order of the priority.
	 * 
	 * @author ram
	 * @date 19 February 2013
	 */
	public void tick() {
		universe.tick();
		System.out.println("");
		while (!events.isEmpty() && events.peek().getTime() <= now + ticksize) {
			//the second condition ensures that an event that should happen in the future doesn't execute now
								
			//get the event at the head of the queue
			Event e = events.poll();
			//System.out.println(e.getAction().getDescription() + " BY "+e.getActor().getShortDescription()+ " AT "+e.getTime() +" PRIORITY "+e.getAction().getPriority());
					
			//execute that event
			e.getAction().execute(e.getActor());			
		}
		//update the present time after the tick has happened
		now = now + ticksize;
		
//		universe.tick();
	}
	
	/**
	 * Sets the size of the tick (amount of time that should elapse between polls of the scheduler) and
	 * instantiates the event queue.
	 * 
	 * @author ram
	 * @date 19 February 2013
	 * @param ticksize - the amount of time to be elapsed for each tick 
	 * @param w - the world to be ticked
	 */
	public Scheduler(int ticksize, World w) {
		universe = w;
		events = new PriorityQueue<Event>();
		this.ticksize = ticksize;
	}
		
}
