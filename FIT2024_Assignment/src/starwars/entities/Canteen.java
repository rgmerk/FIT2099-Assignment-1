package starwars.entities;

import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import starwars.Capability;
import starwars.SWEntity;
import starwars.actions.Fill;

public class Canteen extends SWEntity implements Fillable {

	private int capacity;
	private int level;
	
	public Canteen(MessageRenderer m, int capacity, int initialLevel)  {
		super(m);
		this.shortDescription = "a canteen";
		this.longDescription = "A slightly battered aluminium canteen";
		
		this.capacity = capacity;
		this.level= initialLevel;
		capabilities.add(Capability.FILLABLE);
		this.addAffordance(new Fill(this, m));
	}

	public void fill() {
		System.out.println("Fill method called");
		level = capacity;
	}
	@Override 
	public String getShortDescription() {
		return shortDescription + " [" + level + "/" + capacity + "]";
	}
	
	@Override
	public String getLongDescription () {
		return longDescription + " [" + level + "/" + capacity + "]";
	}
}
