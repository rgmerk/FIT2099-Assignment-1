/**
 * A GUI for displaying messages and the simulation grid, and for obtaining user input.
 * 
 * @author Asel
 */
package hobbit;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.userInterface.MapRenderer;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import edu.monash.fit2024.simulator.userInterface.SimulationController;

public class GUInterface extends JFrame implements MessageRenderer, MapRenderer, SimulationController{

	/**Hobbit grid of the world*/
	private HobbitGrid grid;
	
	/**The number of items to be displayed per location including the location label and colon ':'*/
	private static int locationWidth = 8;
	
	/**Panel that contains the grid formed as a matrix of JTextfields*/
	private JPanel gridPanel = new JPanel();
	
	/**Panel that contains the label lblMessages and the actionButtonPanel*/
	private JPanel actionPanel = new JPanel();
	
	/**Label that displays messages from the message renderer. Is contained in actionPanel*/
	private JLabel lblMessages = new JLabel();
	
	/**Panel that contains action buttons*/
	private static JPanel actionButtonPanel = new JPanel();
	
	/**The index of the command selected*/
	private static volatile int selection = -1;
	
	/**Array list that stores all the action buttons*/
	private static List<JButton> actionButtons = new ArrayList<>();

	
	/**
	 * Constructor for the Graphical User Interface
	 * <p>
	 * This GU interface can be used to display messages, grid and obtain user input in a JFrame window
	 * 
	 * @author 	Asel
	 * @param 	world The world being considered by the Text Interface
	 * @pre 	world should not be null
	 */
	public GUInterface(MiddleEarth world) {
		grid = world.getGrid();
		drawLayout();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Hobbit World");
        this.setVisible(true);
	}
	
	
	
	@Override
	/**
	 * @author Asel
	 * @see {@link #drawGrid()}
	 */
	public void render() {
		drawGrid();
		
	}

	@Override
	/**
	 * Display the messages of the message renderer in the lblMessages
	 * 
	 * @author 	Asel
	 * @param 	message the message string to be displayed on the GUI
	 * @post	displays the <code>message</code> string on the label.
	 * 
	 * TODO	 	This method is triggered by the say method of the message renderer
	 * 			Multiple says would overwrite the last message and the label would only display the last say message
	 * 			This label needs to show all the say messages of a tick at once
	 */
	public void render(String message) {
		//show the message in the label
		lblMessages.setText(message);		
		lblMessages.setHorizontalAlignment(JLabel.CENTER);
		lblMessages.setVerticalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Sets up the main layout to display grid and action panels
	 * 
	 * @author 	Asel
	 * @post 	gridPanel takes up half the screen
	 * @post	the actionPanel with the lblMessages and the actionButtonPanel takes up the rest
	 * 
	 */
	private void drawLayout(){
		this.setLayout(new GridLayout(1,2));//Layout with 2 columns. One for the gridPanel and the other for the actionPanel
		this.add(gridPanel);
		
		actionPanel.setLayout(new GridLayout(2, 1));//Layout with 2 rows. One for lblMessages and the other for action buttons
		actionPanel.add(lblMessages);
		actionPanel.add(actionButtonPanel);
		
		this.add(actionPanel);
		
	}
	
	/**
	 * Draws the grid as a matrix of none editable JTextFields on the gridPanel
	 * 
	 * @author 	Asel
	 * @post	a grid of JTextField created
	 * @post	each text field is none editable
	 * @post	each text field contains a location string @see	{@link #getLocationString(HobbitLocation)}
	 */
	private void drawGrid(){
		
		final int gridHeight = grid.getHeight();
		final int gridWidth  = grid.getWidth();
		
		Font locFont = new Font("SansSerif", Font.BOLD, 15);
		
		gridPanel.removeAll();//clear previous grid
		gridPanel.revalidate();;
		gridPanel.repaint();
		gridPanel.setLayout(new GridLayout(gridHeight, gridWidth));//grid layout for the locations	
		
		
		for (int row = 0; row <gridHeight; row++){//for each row
			
			for (int col = 0; col <gridWidth; col++){//each column of row
				
				HobbitLocation loc = grid.getLocationByCoordinates(col, row);
				String locationText = getLocationString(loc); 
				
				//Each location has a non editable JTextField with the Text for each Location
				JTextField txtLoc = new JTextField(locationText);
				txtLoc.setFont(locFont);
				txtLoc.setEditable(false); //made non editable
				
				//add text field to gridPanel
				gridPanel.add(txtLoc);
				
			}
		}
		
	}
	
	/**
	 * Returns text to be displayed in each JTextField of the grid
	 * 
	 * @author 	Asel
	 * @param 	loc for which the string is required
	 * @pre 	loc should not be a blank
	 * @return 	a string in the format Location Symbol + : + Contents of location + any empty characters
	 * @post	the string contains the symbols of the locations contents
	 * @post	string length is equal to location width
	 */
	private String getLocationString(HobbitLocation loc){
		EntityManager<HobbitEntityInterface, HobbitLocation> em = MiddleEarth.getEntitymanager();
		
		StringBuffer emptyBuffer = new StringBuffer();
		char es = loc.getEmptySymbol(); 
		
		for (int i = 0; i < locationWidth - 3; i++) { 	//add empty symbol character to the buffer
			emptyBuffer.append(es);						//adding 2 less here because one space is reserved for the location symbol
		}									  			//and one more for the colon : used to separate the location symbol and the symbol(s) of the contents of that location
			
		//new buffer buf with symbol of the location + :
		StringBuffer buf = new StringBuffer(loc.getSymbol() + ":"); 
		
		//get the Contents of the location
		List<HobbitEntityInterface> contents = em.contents(loc);
		
		if (contents == null || contents.isEmpty())
			buf.append(emptyBuffer);//add empty buffer to buf to complete the string buffer
		else {
			for (HobbitEntityInterface e: contents) { //add the symbols of the contents
				buf.append(e.getSymbol());
			}
		}
		buf.append(emptyBuffer); //add the empty buffer again since the symbols of the contents that were added might not actually fill the location upto locationWidth
		buf.setLength(locationWidth);//set the length of buf to the required locationWidth
		
		return buf.toString();
				
	}

	/**
	 * Display a the action buttons and receive user input.
	 * 
	 * @param a the HobbitActor to display options for
	 * @return the HobbitActionInterface that the player has chosen to perform.
	 */
	public static HobbitActionInterface getUserDecision(HobbitActor a) {

		selection = -1;
		
		ArrayList<HobbitActionInterface> cmds = new ArrayList<HobbitActionInterface>();

		//for all the actions of the Hobbit actor
		for (HobbitActionInterface ac : MiddleEarth.getEntitymanager().getActionsFor(a)) {
			if (ac.canDo(a))
				cmds.add(ac);//add the ones the Hobbit Actor can do
		}
		
		Collections.sort(cmds);//sorting the actions for a prettier output

		
		//construct the buttons to be displayed
		actionButtonPanel.removeAll();
		actionButtonPanel.revalidate();;
		actionButtonPanel.repaint();
		actionButtonPanel.setLayout(new GridLayout(cmds.size(), 1));//Grid to contain all commands
		actionButtons.clear();
		
		for (int i = 0; i < cmds.size(); i++) {
			JButton actionButton = new JButton();
			actionButton.setText(cmds.get(i).getDescription());
			actionButtonPanel.add(actionButton);
			actionButtons.add(actionButton);
			
			actionButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					selection = actionButtons.indexOf(actionButton);				
				}
			});
		}

		
		while(selection<0){
		    try {
		    Thread.sleep(200);
		       System.out.println("SELECTION : "+ selection);
		    } 
		    catch(InterruptedException e) {}
		}
		 
		
		//String userInput = JOptionPane.showInputDialog("Please enter integer");
		//selection = Integer.parseInt(userInput);
		
		//return the action selected
		return cmds.get(selection);
	}
	

	
}
