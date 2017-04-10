package edu.monash.fit2099.simulator.time;

import java.util.PriorityQueue;

import edu.monash.fit2099.simulator.matter.Action;
import edu.monash.fit2099.simulator.matter.ActionInterface;
import edu.monash.fit2099.simulator.matter.Actor;
import edu.monash.fit2099.simulator.space.World;

/** 
 * This class handles time in the simulation by maintaining a priority queue of <code>Events</code>, prioritized by time and then
 * by priority for <code>Events</code> scheduled to happen in the same tick.
 * <p>
 * <code>Event</code> is an inner class that contains  
 * <ul>
 * 	<li>an <code>Action</code></li>
 * 	<li>an <code>Actor</code></li>
 * 	<li>an <code>Integer</code> that denotes the scheduled event time.</li>
 * </ul>
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
 * 2017-02-19: Added the duration to Actors events before adding them to the queue.
 */

public class Scheduler {
	
	/**
	 * An <code>Event</code> is the execution of a <code>Action</code>, by an <code>Actor</code>, at a point in time.  
	 * 
	 * The <code>Actor</code> may be null in the case of simulations that allow the world to change automatically. 
	 * 
	 * @author ram
	 *
	 */	
	private class Event implements Comparable<Event> {
		
		/**The <code>Action</code> to be performed for this <code>Event</code>*/
		private ActionInterface what;
		
		/**The <code>Actor</code> of the <code>Action</code> for this <code>Event</code>.
		 * <p>
		 * The <code>Actor</code> may be null in the case of simulations that allow the world to change automatically. 
		 */
		private Actor<?> who;
		
		/**
		 * When this <code>Event</code> should occur
		 */
		private int when;
		
		/**
		 * Constructor for an <code>Event</code> object.
		 * 
		 * @param what what Action to be performed as the event
		 * @param who the actor of the event. The Actor may be null in the case of simulations that allow the world to change automatically.
		 * @param when when the event should occur
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
		 * the <code>priority</code> of their <code>Actions</code> (<code>what</code>)
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
		 * 						<li>the <code>Event this</code> happens before <code>e</code> OR</li>
		 * 						<li>the <code>Event this</code> and <code>e</code> happens at the same time but
		 * 							the <code>Action</code> of <code>Event this</code> has higher <code>priority</code> than the <code>Action</code> of <code>Event e</code></li>
		 * 					</ul>
		 * 				</li>
		 *	 			<li>a negative integer if, 
		 * 					<ul>
		 * 						<li>the <code>Event this</code> happens after the <code>e</code> OR</li>
		 * 						<li>the <code>Event this</code> and <code>e</code> happens at the same time but
		 * 							the <code>Action</code> of event <code>this</code> has lower <code>priority</code> than the <code>Action</code> of <code>Event e</code></li>
		 * 					</ul>
		 * 				</li>
		 * 			</ul>
		 * @see 	{@link #what}
		 * @see 	{@link #when}
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
	
	/**
	 * Priority Queue of <code>Events</code> sorted by time and priority of <code>Actions</code>.
	 * <p>
	 * All <code>Events</code> are sorted by their scheduled time (<code>when</code>) with <code>Events</code>
	 * scheduled earlier (smaller <code>when</code>) happening before <code>Events</code> scheduled later (larger <code>when</code>).
	 * <p>
	 * <code>Events</code> scheduled to happen at the same time are sorted by the <code>priority</code> of their <code>Actions</code>
	 * hence <code>Events</code> whose <code>Actions (what)</code> have higher <code>priority</code> are completed before others.
	 * <p>
	 * <code>Events</code> scheduled to happen at the same time with same <code>priority</code> in their <code>Actions</code> complete in
	 * arbitrary order.
	 *   
	 * @see {@link Event#compareTo(Event)}
	 */
	private PriorityQueue<Event> events = new PriorityQueue<Event>();
	
	/**Stores the current time of the <code>World</code>. Zero(0) to start with*/
	private int now = 0;
	
	/**The amount of time that should elapse between polls of this <code>Scheduler</code>.
	 * Smallest should be 1.
	 */
	private int ticksize;
	
	/**The <code>World</code> for which this <code>Scheduler</code> passes time, i.e. the <code>World</code> to be ticked*/
	private World universe;
	
	
	/**
	 * Schedules an <code>Action</code> by adding an <code>Event</code> to the queue of events (<code>events</code>).
	 * 
	 * @param 	c the <code>Action</code> to be scheduled
	 * @param 	a the actor of the <code>Action</code> or <code>Event</code>. The <code>Actor</code> may be null in the case of simulations that allow the world to change automatically.
	 * @param 	duration of the <code>Event</code> (how long it takes for the event to complete)
	 * 
	 * @see 	{@link #events}
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
				events.offer(new Event(c, a, now + duration + delay));
			}
		}
		else{//Non actors or null
			//add event to queue of events. The event will happen after the duration from now
			events.offer(new Event(c, a, now + duration));
		}
		
		
		
	}
	
	/** 
	 * Allow time to pass.  
	 * Process any <code>Events</code> that are scheduled to go off between <code>now</code> and the 
	 * next time tick (<code>now + ticksize</code>) in the order of the priority.
	 * 
	 * @author 	ram 
	 * @date 	19 February 2013
	 */
	public void tick() {
		
		//calls the tick in other Entities so that they could schedule actions and so on
		universe.tick();
		
		while (!events.isEmpty() && events.peek().getTime() <= now + ticksize) {
			//the second condition ensures that an event that should happen in the future doesn't execute now
								
			//get the event at the head of the queue
			Event e = events.poll();
			
			//execute that event
			e.getAction().execute(e.getActor());			
		}
		//update the present time after the tick has happened
		now = now + ticksize;
		
	}
	
	/**
	 * Sets the <code>tickSize</code> and instantiates the <code>events</code> queue.
	 * 
	 * @author 	ram
	 * @date 	19 February 2013
	 * @param 	ticksize the amount of time to be elapsed for each tick 
	 * @param 	w the <code>World</code> to be ticked
	 * 
	 * @see {@link #ticksize}
	 * @see {@link #events}
	 * @see {@link #world}
	 */
	public Scheduler(int ticksize, World w) {
		universe = w;
		events = new PriorityQueue<Event>();
		this.ticksize = ticksize;
	}
		
}
