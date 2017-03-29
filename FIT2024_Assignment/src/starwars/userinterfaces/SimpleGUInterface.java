/**
 * A GUI for displaying messages and the simulation grid, and for obtaining user input. This GUI display the 
 * all commands as buttons to be clicked by the user
 * 
 * @author Asel
 */
/*
 * Change log
 * 2017/02/03	Fixed the issue where layout didn't display all elements by moving the repaint and validate methods 
 * 				for the panels (gridPanel and actionButtonPanel) (asel)
 * 				Added a message buffer string that allows all say messages of within a tick to be displayed at once (asel)
 * 2017/02/04	Fixed the issue with the message renderer. It now prints all the messages from the message renderer (asel)
 */
package starwars.userinterfaces;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.javafx.scene.traversal.Direction;

import edu.monash.fit2024.gridworld.Grid.CompassBearing;
import edu.monash.fit2024.simulator.matter.EntityManager;
import edu.monash.fit2024.simulator.userInterface.MapRenderer;
import edu.monash.fit2024.simulator.userInterface.MessageRenderer;
import edu.monash.fit2024.simulator.userInterface.SimulationController;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWGrid;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.actions.Move;

/**
 * IMPORTANT
 * This UI is no longer required and is not compatible with the controller. Needs to be deleted! - Asel
 */
public class SimpleGUInterface extends JFrame implements MessageRenderer, MapRenderer, SimulationController{

	/**Hobbit grid of the world*/
	private SWGrid grid;
	
	/**The number of items to be displayed per location including the location symbol and colon ':'*/
	private static int locationWidth = 8;
	
	/**Panel that contains the grid formed as a matrix of JTextfields*/
	private JPanel mapPanel = new JPanel();
	
	/**Panel that contains the label for messages {@link #lblMessages} and the panel for all action buttons {@link #actionButtonPanel}*/
	private JPanel actionPanel = new JPanel();
	
	/**Label that displays messages from the message renderer. Is contained in {@link #actionPanel}*/
	private JLabel lblMessages = new JLabel();
	
	/**Panel that contains action buttons corresponding to all commands. It is contained in {@link #actionPanel}*/
	private static JPanel actionButtonPanel = new JPanel();
	
	/**The index of the command selected*/
	private static volatile int selection = -1;
	
	/**Array list that stores all the action buttons corresponding order of the commands*/
	private static List<JButton> actionButtons = new ArrayList<>();
	
	/**String that contains all the messages from the message renderer within a tick. Formatted using HTML tags*/
	private static String messageBuffer = "";

	
	/**
	 * Constructor for the Graphical User Interface
	 * <p>
	 * This GU interface can be used to display messages, grid and obtain user input in a JFrame window
	 * 
	 * @author 	Asel
	 * @param 	world The world being considered by the GUI
	 * @pre 	<code>world</code> should not be null
	 * @post	opens a full screen JFrame window with the map, messages and action buttons (if any)
	 */
	public SimpleGUInterface(SWWorld world) {
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
	 */
	public void render(String message) {
		//HTML formatting used instead of \n, to achieve a multi line label
		messageBuffer = messageBuffer + message+"<br>";
		lblMessages.setText("<html>"+messageBuffer+"</html>");	
		
		//for pretty looks
		Font messageFont = new Font("SansSerif", Font.BOLD, 15);
		lblMessages.setFont(messageFont);
		lblMessages.setHorizontalAlignment(JLabel.CENTER);
		lblMessages.setVerticalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Clear the message buffer after tick
	 * @author Asel
	 * @see ({@link #messageBuffer}
	 */
	private static void clearMessageBuffer(){
		messageBuffer="";
	}
	
	/**
	 * Sets up the main layout to display grid and action panels
	 * 
	 * @author 	Asel
	 * @post 	gridPanel takes up half the screen, the actionPanel with the lblMessages and the actionButtonPanel takes up the rest
	 * 
	 */
 	private void drawLayout(){		
 		this.setLayout(new GridLayout(1,2));//Layout with 2 columns. One for the gridPanel and the other for the actionPanel
		this.add(mapPanel); //add the grid	

		actionPanel.setLayout(new GridLayout(2, 1));//Layout with 2 rows. For lblMessages and the actionButtonPanel
		actionPanel.add(lblMessages);
		actionPanel.add(actionButtonPanel);
		
		this.add(actionPanel);//add action panel to main layout		
	}
	
	/**
	 * Draws the map as a matrix of non editable JTextFields on the gridPanel
	 * 
	 * @author 	Asel
	 * @post	a grid of JTextField created
	 * @post	each text field is non editable
	 * @post	each text field contains a location string @see	{@link #getLocationString(SWLocation)}
	 */
	private void drawGrid(){
		
		assert (grid!=null)	:"grid to be draw cannot be null";
		
		final int gridHeight = grid.getHeight();
		final int gridWidth  = grid.getWidth();
		
		Font locFont = new Font("SansSerif", Font.BOLD, 15);
		
		mapPanel.removeAll();//clear previous grid
		mapPanel.setLayout(new GridLayout(gridHeight, gridWidth));//grid layout for the locations	
		
		for (int row = 0; row <gridHeight; row++){//for each row
			
			for (int col = 0; col <gridWidth; col++){//each column of row
				
				SWLocation loc = grid.getLocationByCoordinates(col, row);
				String locationText = getLocationString(loc); 
				
				//Each location has a non editable JTextField with the Text for each Location
				JTextField txtLoc = new JTextField(locationText);
				
				//pretty looks again
				txtLoc.setFont(locFont);		
				txtLoc.setHorizontalAlignment(JTextField.CENTER);
				
				txtLoc.setEditable(false); //made non editable
				
				txtLoc.setToolTipText(loc.getShortDescription());//show the location description when mouse hovered
				
				//add text field to gridPanel
				mapPanel.add(txtLoc);
				
			}
		}
		
		//refresh the panel with the changes
		mapPanel.revalidate();;
		mapPanel.repaint();
		
	}
	
	/**
	 * Returns text to be displayed in each JTextField of the grid
	 * 
	 * @author 	Asel
	 * @param 	loc for which the string is required
	 * @pre 	<code>loc</code> should not be null
	 * @return 	a string in the format Location Symbol + : + Contents of location + any empty characters
	 * @post	the <code>string</code> contains the symbols of the locations contents plus any empty characters of the location
	 * @post	<code>string</code> length is equal to location width
	 */
	private String getLocationString(SWLocation loc){
		EntityManager<SWEntityInterface, SWLocation> em = SWWorld.getEntitymanager();
		
		StringBuffer emptyBuffer = new StringBuffer();
		char es = loc.getEmptySymbol(); 
		
		for (int i = 0; i < locationWidth - 3; i++) { 	//add empty symbol character to the buffer
			emptyBuffer.append(es);						//adding 2 less here because one space is reserved for the location symbol
		}									  			//and one more for the colon : used to separate the location symbol and the symbol(s) of the contents of that location
			
		//new buffer buf with symbol of the location + :
		StringBuffer buf = new StringBuffer(loc.getSymbol() + ":"); 
		
		//get the Contents of the location
		List<SWEntityInterface> contents = em.contents(loc);
		
		if (contents == null || contents.isEmpty())
			buf.append(emptyBuffer);//add empty buffer to buf to complete the string buffer
		else {
			for (SWEntityInterface e: contents) { //add the symbols of the contents
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
	 * @param a the SWActor to display options for
	 * @return the SWActionInterface that the player has chosen to perform.
	 */
	 public static SWActionInterface getUserDecision(SWActor a) {
		 
		 clearMessageBuffer();//this method is called in each tick so the message buffer can be cleared

		//selection value set to -1 in order to trigger the wait
		selection = -1;
		
		ArrayList<SWActionInterface> cmds = new ArrayList<SWActionInterface>(); //all commands
		
		//get the Actions the SWActor can do
		for (SWActionInterface ac : SWWorld.getEntitymanager().getActionsFor(a)) {
			if (ac.canDo(a))
				cmds.add(ac);
		}
				
		//sort for prettier output
		Collections.sort(cmds);
		
		//construct the buttons to be displayed
		actionButtonPanel.removeAll(); //remove action buttons from previous tick
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
					//a click would change the selection to the index of the corresponding command
					selection = actionButtons.indexOf(actionButton);				
				}
			});
		}

		//refresh panel with the changes
		actionButtonPanel.revalidate();;
		actionButtonPanel.repaint();
		
		//wait for user input before returning
		//wait until the selection is not -1
		while(selection<0){
		    try {
		    Thread.sleep(200);
		    } 
		    catch(InterruptedException e) {}
		}
		 
		return cmds.get(selection);
	}
	 
		
		
}
