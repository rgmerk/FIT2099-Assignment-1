/**
 * A GUI for displaying messages and the simulation grid, and for obtaining user input.
 * <p>
 * This GUI displays the move commands and the other commands separately
 * <p>
 * This GUI is only suitable for a world with 8 way movements (NW,N,NE,W,E,SW,S,SE)
 * 
 * @author Asel
 */
/*
 * Change log
 * 2017/02/03	Fixed the issue where layout didn't display all elements by moving the repaint and validate methods 
 * 				for the panels (gridPanel and actionButtonPanel) (asel)
 * 				Added a message buffer string that allows all say messages of within a tick to be displayed at once (asel)
 * 				Added a separate panel for move commands that corresponds to the compass bearing
 * 2017/02/04	Fixed the issue with the message renderer. It now prints all the messages from the message renderer (asel)
 * 2017/02/08	human controlled player's locations are highlighted in yellow. The text if the GUI is mono spaced (asel)
 */
package starwars.userinterfaces;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
public class GUInterface extends JFrame implements MessageRenderer, MapRenderer, SimulationController{

	/**Hobbit grid of the world*/
	private SWGrid grid;
	
	/**The number of items to be displayed per location including the location label and colon ':'*/
	private static int locationWidth = 8;
	
	/**Panel that contains the map formed as a matrix of JTextfields*/
	private JPanel gridPanel = new JPanel();
	
	/**Panel that contains the label {@link #lblMessages}, {@link #moveActionPanel}  and {@link #otherActionPanel}*/
	private JPanel actionPanel = new JPanel();
	
	/**Label that displays messages from the message renderer. Is contained in {@link #actionPanel}*/
	private JLabel lblMessages = new JLabel();
	
	/**Panel that contains action buttons corresponding to move commands. Is contained in {@link #actionPanel}*/
	private static JPanel moveActionPanel = new JPanel();
	
	/**Panel that contains action buttons corresponding to non movement commands. Is contained in {@link #actionPanel}*/
	private static JPanel otherActionPanel = new JPanel();
	
	/**The index of the command selected*/
	private static volatile int selection = -1;
	
	/**Array list that stores all the action buttons corresponding order of the commands*/
	private static List<JButton> allCommandButtons = new ArrayList<>();
	
	/**String that contains all the messages from the message renderer within a tick. Formatted using HTML tags*/
	private static String messageBuffer = "";
	
	/**defined order of angles required to sort the 8 directions
	 * <p>
	 * Defined order corresponds to NW, N, NE, W, CENTER, E, SW, S, SE
	 */
	/* This order also corresponds to the order in which move actionButtons should appear in the 3 X 3 grid
	 * The -1 entry corresponds to the empty space in the middle
	 */
	private static final List<Integer> definedOrder = Arrays.asList(315,0,45,270,-1,90,225,180,135);
	
	private static final int CONTROL_PANE_WIDTH = 600;

	
	/**
	 * Constructor for the Graphical User Interface
	 * <p>
	 * This GU interface can be used to display messages, grid and obtain user input in a JFrame window
	 * <p>
	 * This GUI displays action buttons that corresponding to Move actions and other actions separately 
	 * 
	 * @author 	Asel
	 * @param 	world The world being considered by the GUI
	 * @pre 	<code>world</code> should not be null
	 * @post	opens a full screen JFrame window with the map, messages and action buttons (if any)
	 * @post	the action buttons for movement are separate from the action buttons for non movement commands
	 */
	public GUInterface(SWWorld world) {
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
		Font messageFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
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
	 * @post 	<code>gridPanel</code> takes up half the screen and the <code>actionPanel</code> takes up the rest
	 */
 	private void drawLayout(){		
 		this.setLayout(new GridLayout(1,2));//Layout with 2 columns. One for the gridPanel and the other for the actionPanel
		this.add(gridPanel); //add the grid	to main layout

		actionPanel.setLayout(new GridLayout(3, 1));//Layout with 3 rows. For lblMessages, moveActionPanel and otherActionPanel
		actionPanel.add(lblMessages);
		actionPanel.add(moveActionPanel);
		actionPanel.add(otherActionPanel);
		actionPanel.setSize(Integer.MAX_VALUE,CONTROL_PANE_WIDTH);
		
		this.add(actionPanel);//add action panel to main layout		
	}
 	
	
	/**
	 * Draws the map as a matrix of non editable JTextFields on the gridPanel.
	 * <p>
	 * <code>SWLocation</code>s of human controlled actors will be highlighted with yellow.
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
		
		//font for pretty output
		Font locFont = new Font(Font.MONOSPACED, Font.PLAIN, 15);
		Font playerFont = new Font(Font.MONOSPACED, Font.BOLD, 15);
		
		gridPanel.removeAll();//clear previous grid
		gridPanel.setLayout(new GridLayout(gridHeight, gridWidth));//grid layout for the locations	
		
		for (int row = 0; row <gridHeight; row++){//for each row
			
			for (int col = 0; col <gridWidth; col++){//each column of row
				
				SWLocation loc = grid.getLocationByCoordinates(col, row);
				String locationText = getLocationString(loc); 
				
				//Each location has a non editable JTextField with the Text for each Location
				JTextField txtLoc = new JTextField(locationText);
				
				//for pretty looks
				txtLoc.setFont(locFont);		
				txtLoc.setHorizontalAlignment(JTextField.CENTER);
				
				//highlight the human controlled player's location
				if (locationHasHumanControlledActor(loc)){
					txtLoc.setFont(playerFont);
				}
				
				txtLoc.setBackground(getColorOfLoc(loc));
				
				
				txtLoc.setEditable(false); //made non editable
				txtLoc.setToolTipText(loc.getShortDescription());//show the location description when mouse hovered
				
				//add text field to gridPanel
				gridPanel.add(txtLoc);
				
			}
		}
		
		//refresh the panel with the changes
		gridPanel.revalidate();;
		gridPanel.repaint();
		
	}
	
	/**
	 * Returns text to be displayed in each JTextField of the grid
	 * 
	 * @author 	Asel
	 * @param 	loc for which the string is required
	 * @pre 	<code>loc</code> should not be a blank
	 * @return 	a string in the format Location Symbol + : + Contents of location + any empty characters
	 * @post	the string contains the symbols of the locations contents
	 * @post	string length is equal to location width
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
	 * Returns is a given <code>SWLocation loc</code> has a human controlled <SWActor>
	 * 
	 * @param 	loc the <code>SWLocation</code> being queried
	 * @return	true if <code>loc</code> contains a human controlled actor, false otherwise
	 */
	private boolean locationHasHumanControlledActor(SWLocation loc){
		
		EntityManager<SWEntityInterface, SWLocation> em = SWWorld.getEntitymanager();
		
		//get the contents of the location
		List<SWEntityInterface> contents = em.contents(loc);
		
		if (contents!=null && !contents.isEmpty()){
			for (SWEntityInterface e: contents) { //find if human controlled
				if (e instanceof SWActor){
					if (((SWActor)e).isHumanControlled()){;
						return true;
					}
				}
			}
		}
		//couldn't find any human controlled actors
		return false;		
	}

	/**
	 * Display the action buttons and receive user input.
	 * 
	 * @param a the SWActor to display options for
	 * @return the SWActionInterface that the player has chosen to perform.
	 */
	public static SWActionInterface getUserDecision(SWActor a) {
		clearMessageBuffer();//this method is called in each tick so the message buffer can be cleared
		
		//selection set to -1 to trigger wait for user input
		selection = -1;
		
		ArrayList<SWActionInterface> cmds = new ArrayList<SWActionInterface>(); //all commands (move and non move)
		ArrayList<SWActionInterface> moveCmds = new ArrayList<SWActionInterface>(); //all move commands
		ArrayList<SWActionInterface> otherCmds = new ArrayList<SWActionInterface>(); //all other non move commands

		//get the Actions the SWActor can do
		for (SWActionInterface ac : SWWorld.getEntitymanager().getActionsFor(a)) {
			if (ac.canDo(a)){
				if (ac.isMoveCommand()){
					moveCmds.add(ac);
				}
				else{
					otherCmds.add(ac);
				}
			}
		}
		
		//sort the move commands in a defined order
		sortMoveCommands(moveCmds);
		
		//sort other commands for prettier output
		Collections.sort(otherCmds);
		
		//add move commands to list of all commands first
		for (SWActionInterface ac:moveCmds){
			cmds.add(ac);
		}
		
		//next add the other commands
		for (SWActionInterface ac:otherCmds){
			cmds.add(ac);
		}
		
		
		allCommandButtons.clear();
		
		addMoveActionButtons(moveCmds);
		addOtherActionButtons(otherCmds);
		
		//wait for user input before returning
		while(selection<0){
		    try {
		    Thread.sleep(200);
		    } 
		    catch(InterruptedException e) {}
		}
		 
		return cmds.get(selection);
	}
	
	/**
	 * Sort the Move Commands in a predefined order @see {@link #definedOrder}
	 * 
	 * @author	Asel
	 * @param 	moveCmds move commands to be sorted
	 * @pre 	<code>moveCmds</code> should only contain <code>SWActionInterface</code> objects that are instances of <code>Move</code>
	 */
	private static void sortMoveCommands(ArrayList<SWActionInterface> moveCmds){
		
		Comparator<SWActionInterface> comparator = new Comparator<SWActionInterface>(){

		    @Override
		    public int compare(final SWActionInterface moveAction1, final SWActionInterface moveAction2){
		    	int move1Index = 0;
		    	int move2Index = 0;
		    	
		    	if (moveAction1 instanceof Move && moveAction2 instanceof Move){
		    		
		    		//casting to Move.
		    		Move move1 = (Move)moveAction1;
		    		Move move2 = (Move)moveAction2;
		    		
			    	if (move1.getWhichDirection() instanceof CompassBearing && move2.getWhichDirection() instanceof CompassBearing){
			    		//cast to get the angle of the compass bearing
			    		int move1BearingAngle = ((CompassBearing)move1.getWhichDirection()).getAngle();
			    		int move2BearingAngle = ((CompassBearing)move2.getWhichDirection()).getAngle();
			    		
			    		//indexes in the predefined order
			    		move1Index = definedOrder.indexOf(move1BearingAngle);
			    		move2Index = definedOrder.indexOf(move2BearingAngle);
			    	}
		    	}

		    	//sorting based on the index in the defined order
		        return move1Index - move2Index;
		    }
		};

		moveCmds.sort(comparator);
		
	}
	
	/**
	 * Add the non movement action buttons to the <code>otherActionPanel</code>
	 * 
	 * @author 	Asel
	 * @param 	otherCmds a list of non movement commands
	 * @post	all commands in <code>otherCmds</code> added to <code>otherActionPanel</code> as JButtons
	 */
	private static void addOtherActionButtons(ArrayList<SWActionInterface> otherCmds){
	
		otherActionPanel.removeAll(); //remove other action buttons from previous layout
		otherActionPanel.setLayout(new GridLayout(otherCmds.size(), 1));//Grid to contain non movement commands
		
		//add other buttons
		for (SWActionInterface cmd : otherCmds) {
			JButton actionButton = new JButton();
			actionButton.setText(cmd.getDescription());
			otherActionPanel.add(actionButton);
			allCommandButtons.add(actionButton);
			
			actionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selection = allCommandButtons.indexOf(actionButton);				
				}
			});
		}

		//refresh panel with the changes
		otherActionPanel.revalidate();;
		otherActionPanel.repaint();
	}

	/**
	 * Add the movement action buttons to the <code>moveActionPanel</code> in a 3 X 3 Grid
	 * 
	 * @author 	Asel
	 * @param 	moveCmds a list of move commands
	 * @pre 	<code>moveCmds</code> must be sorted in the defined order. @see {@link #sortMoveCommands(ArrayList)}
	 * @post	All commands in <code>moveCmds</code> are displayed with action buttons with their positions corresponding to the bearings
	 */
	private static void addMoveActionButtons(ArrayList<SWActionInterface> moveCmds){
		
		moveActionPanel.removeAll(); //remove move action buttons from previous layout
		moveActionPanel.setLayout(new GridLayout(3,3)); //3X3 grid for all 8 directions plus the empty space in the middle
		
		//the index of the move command in the list that is currently being processed
		int moveCmdIndex = 0;

		//this code is a bit tricky so I'll try my best to explain using the power of comments
		for (int angle : definedOrder){ //we looping through each and every angle in the defined order
			
			if (moveCmdIndex < moveCmds.size()){ //if there are move commands in moveCmd that have not been added yet
				
				SWActionInterface currMoveCommand = moveCmds.get(moveCmdIndex);
				
				/* In a higher level the program is trying to add the currMoveCommand at the right 
				 * position in a 3X3 grid so that it would corresponds to the bearing
				 */
				
				if (currMoveCommand instanceof Move){ //this should always be true
					Move move = (Move)currMoveCommand;
					int moveAngle = ((CompassBearing)move.getWhichDirection()).getAngle(); //get the angle of the move command that we are trying to add
										
					if (angle == moveAngle){ //if the angles match then we need to place a button
						JButton actionButton = new JButton();
						actionButton.setText(currMoveCommand.getDescription());
			
						moveActionPanel.add(actionButton);
						allCommandButtons.add(actionButton);
						
						actionButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								selection = allCommandButtons.indexOf(actionButton);				
							}
						});
						
						//increment moveCmdIndex since the move command at moveCmdIndex has already being added as an action button
						moveCmdIndex++;
					}
					else{//couldnt find a move command with the same angle. So just add a empty label
						JLabel emptyLabel = new JLabel("");
						moveActionPanel.add(emptyLabel);
					}
				}
			}
			else{ //all move commands have been added to the panel
				//so we'll add empty labels to complete the 3 X 3 grid layout
				JLabel emptyLabel = new JLabel("");
				moveActionPanel.add(emptyLabel);
			}
		}
	
		

		//refresh panel with the changes
		moveActionPanel.revalidate();;
		moveActionPanel.repaint();
	}
	
	/**
	 * This method returns a color for a <code>SWLocation loc</code>
	 * <p>
	 * This method always returns
	 * <p>
	 * TODO: 	Change the characters to match the new location symbols of the world
	 * 			Support for color blindness
	 * 
	 * @param 	loc the location being queried
	 * @return 	<ul>
	 * 				<li>Green for Mirkwood Forest</li>
	 * 				<li>Light green for The Shire</li>
	 * 				<li>Dark green for the Bag End</li>
	 * 				<li>Light blue for the River Sherbourne</li>
	 * 				<li>Light brown for Middle Earth</li>
	 * 				<li>Gray for locations not specified above</li>
	 * 			</ul>
	 */
	private Color getColorOfLoc(SWLocation loc) {
		
		final char FOREST 	= 'F';
		final char RIVER 	= 'R';
		final char SHIRE 	= 'S';
		final char EARTH 	= '.';
		final char BAGEND 	= 'b';
		
		switch (loc.getSymbol()) {
		case FOREST:
			return Color.decode("#44aa00");

		case RIVER:
			return Color.decode("#2ad4ff");
			
		case SHIRE:
			return Color.decode("#8dd35f");
			
		case EARTH:
			return Color.decode("#fff6d5");
			
		case BAGEND:
			return Color.decode("#338000");
			
		default:
			return Color.decode("#ececec");
		}
	}
			
}
