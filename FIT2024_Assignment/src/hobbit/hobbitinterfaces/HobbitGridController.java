package hobbit.hobbitinterfaces;

import java.util.ArrayList;

import edu.monash.fit2024.gridworld.GridController;
import edu.monash.fit2024.gridworld.GridRenderer;
import edu.monash.fit2024.simulator.matter.ActionInterface;
import hobbit.HobbitActionInterface;
import hobbit.HobbitActor;
import hobbit.HobbitGrid;
import hobbit.MiddleEarth;

/**
 * Concrete implementation of the <code>GridController</code>.
 * <p>
 * This controller class works independent of the GridRenderer implementation
 * 
 * @author 	Asel
 * @see 	{@link edu.monash.fit2024.gridworld.GridController}
 *
 */
public class HobbitGridController implements GridController {

	/**The user interface to be used by the controller. All user interfaces should be concrete 
	 * implementations of the <code>GridRenderer</code> interface
	 * 
	 * @see {@link edu.monash.fit2024.gridworld.GridRenderer}*/
	private static GridRenderer ui; 
	
	/**Hobbit grid of the world*/
	private HobbitGrid grid;
	
	/**
	 * Constructor of this <code>HobbitGridController</code>
	 * <p>
	 * The constructor will initialize the <code>grid</code> and the user interface to be used by the controller.
	 * 
	 * @param 	world the world to be considered by the controller
	 * @pre 	the world should not be null
	 */
	public HobbitGridController(MiddleEarth world) {
		this.grid = world.getGrid();
		
		//change the user interface to be used here in the constructor
		//this.ui = new HobbitGridTextInterface(this.grid); //use a Text Interface to interact
		this.ui = new HobbitGridGUI(this.grid); //Use a GUI to interact
	}

	@Override
	public void render() {
		//Call the UI to handle this
		ui.displayMap();		
	}

	@Override
	public void render(String message) {
		//call the UI to handle this too
		ui.displayMessage(message);
	}
	
	/**
	 * Will return a Action selected by the user.
	 * <p>
	 * This method will provide the user interface with a list of commands from which the user 
	 * needs to select one from and will return this selection.	
	 * 
	 * @param 	a the <code>HobbitActor</code> for whom an Action needs to be selected
	 * @return	the selected action for the <code>HobbitActor a</code>
	 */
	public static HobbitActionInterface getUserDecision(HobbitActor a) {
		
		//this list will store all the commands that HobbitActor a can perform
		ArrayList<ActionInterface> cmds = new ArrayList<ActionInterface>();

		//Get all the actions the HobbitActor a can perform
		for (HobbitActionInterface ac : MiddleEarth.getEntitymanager().getActionsFor(a)) {
			if (ac.canDo(a))
				cmds.add(ac);
		}
		
		//Get the UI to display the commands to the user and get a selection
		//TO DO: Ensure the cmd list is not empty to avoid an infinite wait
		ActionInterface selectedAction = ui.getSelection(cmds);
		
		//cast and return selection
		return (HobbitActionInterface)selectedAction;//return selected action
	}
	
}
