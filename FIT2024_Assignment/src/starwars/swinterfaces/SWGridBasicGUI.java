package starwars.swinterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.monash.fit2024.gridworld.GridRenderer;
import edu.monash.fit2024.gridworld.Grid.CompassBearing;
import edu.monash.fit2024.simulator.matter.ActionInterface;
import edu.monash.fit2024.simulator.matter.EntityManager;
import starwars.SWActionInterface;
import starwars.SWActor;
import starwars.SWEntityInterface;
import starwars.SWGrid;
import starwars.SWLocation;
import starwars.SWWorld;
import starwars.actions.Move;

/**
 * This is a basic graphical based user interface for the simulation. Is responsible for outputting a graphical map and messages on a JFrame
 * window and also obtain user selection of commands from the same JFrame window.
 * <p>
 * The graphics of this UI are pretty basic. Its operations are controlled by the <code>SWGridController</code>
 * 
 * @author Asel
 */
public class SWGridBasicGUI extends JFrame implements GridRenderer {

	/**The grid of the world*/
	private static SWGrid grid;
	
	/**Panel that contains the map formed as a matrix of JTextfields*/
	private JPanel mapPanel = new JPanel();
	
	/**Panel that contains the label {@link #lblMessages}, {@link #moveActionPanel}  and {@link #otherActionPanel}*/
	private JPanel controlPanel = new JPanel();
	
	/**Label that displays messages. Is contained in {@link #controlPanel}*/
	private JLabel lblMessages = new JLabel();
	
	/**Panel that contains action buttons corresponding to move commands. Is contained in {@link #controlPanel}*/
	private static JPanel moveActionPanel = new JPanel();
	
	/**Panel that contains action buttons corresponding to non movement commands. Is contained in {@link #controlPanel}*/
	private static JPanel otherActionPanel = new JPanel();
	
	/**The index of the command selected*/
	private static volatile int selection = -1;
	
	/**Array list that stores all the JButtons corresponding order of the commands. 
	 * <p>
	 * JButtons of move commands come before the JButtons of non movement commands
	 */
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
	
	/**the height of a location tile. Used to determine the total height of the grid on the display*/
	private final int locHeight = 100;
	
	/**the width of a location tile. Used to determine the total width of the grid on the display*/
	private final int locWidth  = 100;
	
	/**
	 * Constructor for the <code>SWGridGUI</code>. This will draw the basic layout on the
	 * window and open it as a maximized window.
	 * 
	 * @param 	grid the grid of the world
	 * @pre 	<code>grid</code> should not be null
	 * 
	 */
	public SWGridBasicGUI(SWGrid grid) {
		SWGridBasicGUI.grid = grid;
				
		//setting a title and opening the window in full screen
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Hobbit World");
        this.setVisible(true);
        
        drawLayout();
	}
	
	@Override
	public void displayMap() {
		final int gridHeight = grid.getHeight();
		final int gridWidth  = grid.getWidth();
				

		JPanel map = new JPanel();
		map.setLayout(new GridLayout(gridHeight, gridWidth));
			
		Font locFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		
		for (int row = 0; row< gridHeight; row++){ //for each row
			for (int col = 0; col< gridWidth; col++){ //each column of a row
				
				SWLocation loc = grid.getLocationByCoordinates(col, row);
				String locationText = getLocationString(loc); 
				
				//Each location has a non editable JTextField with location string of each text
				JTextField txtLoc = new JTextField(locationText);
				
				//for pretty looks
				txtLoc.setFont(locFont);		
				txtLoc.setHorizontalAlignment(JTextField.CENTER);
							
				
				//highlight the human controlled player's location with yellow
				if (locationHasHumanControlledActor(loc)){
					txtLoc.setBackground(Color.YELLOW);
				}
				else { //other locations are given a color based on their location symbol
					txtLoc.setBackground(getColorOfLoc(loc));
				}
				
				txtLoc.setEditable(false); //made non editable
				txtLoc.setToolTipText(loc.getShortDescription());//show the location description when mouse hovered
				
				//add text field to gridPanel
				map.add(txtLoc);
				
			}
			
			
		}
		
		JScrollPane mapScrollPane = new JScrollPane(map);
		mapScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mapScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		
		//refresh the panel with the changes
		mapPanel.removeAll();
		mapPanel.setLayout(new GridLayout(1, 1));
		mapPanel.add(mapScrollPane);
		
		mapPanel.revalidate();
		mapPanel.repaint();
	}

	@Override
	public void displayMessage(String message) {
		//HTML formatting used instead of \n, to achieve a multi line label
		messageBuffer = messageBuffer + message+"<br>";
		lblMessages.setText("<html>"+messageBuffer+"</html>");	
		
		//for pretty looks
		Font messageFont = new Font(Font.MONOSPACED, Font.BOLD, 18);
		lblMessages.setFont(messageFont);
		lblMessages.setHorizontalAlignment(JLabel.CENTER);
		lblMessages.setVerticalAlignment(JLabel.CENTER);

	}

	@Override
	public ActionInterface getSelection(ArrayList<ActionInterface> cmds) {
		clearMessageBuffer();//this method is called in each tick so the message buffer can be cleared
		
		
		//assertion for the precondition
		assert cmds.size()>0:"command list for the actor is empty";
				
				
		//selection set to -1 to trigger the wait for user input
		selection = -1;
		
		ArrayList<SWActionInterface> moveCmds = new ArrayList<SWActionInterface>(); //all move commands
		ArrayList<SWActionInterface> otherCmds = new ArrayList<SWActionInterface>(); //all other non move commands

		//Separate the move and other commands
		for (ActionInterface ac : cmds) {
			
			if (((SWActionInterface) ac).isMoveCommand()){ //move commands
				moveCmds.add((SWActionInterface)ac);
			}
			else{ //non move commands
				otherCmds.add((SWActionInterface)ac);
			}
			
		}
		
		//sort the move commands in a defined order
		sortMoveCommands(moveCmds);
		
		//sort other commands for prettier output
		Collections.sort(otherCmds);
		
		ArrayList<SWActionInterface> allCmds = new ArrayList<SWActionInterface>(); //all commands
		
		//add move commands to list of all commands first
		for (SWActionInterface ac:moveCmds){
			allCmds.add(ac);
		}
		
		//next add the other commands
		for (SWActionInterface ac:otherCmds){
			allCmds.add(ac);
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
		 
		return allCmds.get(selection);
	}
	
	/**
	 * Returns a string consisting of the symbol of the <code>SWLocation loc</code>, a colon ':' followed by 
	 * any symbols of the contents of the <code>SWLocation loc</code> and/or empty spaces of the <code>SWLocation loc</code>.
	 * <p>
	 * All string returned by this method are of a fixed length and doesn't contain any line breaks.
	 * 
	 * @author 	Asel (most code adopted from previous version of TextInterface)
	 * @param 	loc for which the string is required
	 * @pre		all symbols and empty spaces should not be line break characters
	 * @return 	a string in the format location symbol of <code>loc</code> + : + symbols of contents of <code>loc</code> + any empty characters of <code>loc</code>
	 * @post	all strings returned are of a fixed size
	 */
	private String getLocationString(SWLocation loc) {
		
		final EntityManager<SWEntityInterface, SWLocation> em = SWWorld.getEntitymanager();
		
		//all string would be of locationWidth length
		final int locationWidth = 8;
		
		StringBuffer emptyBuffer = new StringBuffer();
		char es = loc.getEmptySymbol(); 
		
		for (int i = 0; i < locationWidth - 2; i++) { 	//add two less as one character is reserved for the location symbol and the other for the colon (":")
			emptyBuffer.append(es);						
		}									  			
			
		//new buffer buf with a symbol of the location + :
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
		buf.append(emptyBuffer); //add the empty buffer again since the symbols of the contents that were added might not actually filled the location upto locationWidth
		
		//set a fixed length
		buf.setLength(locationWidth);
		
		return buf.toString();		
	}
	
	
	/**
	 * Returns is a given <code>SWLocation loc</code> has a human controlled <SWActor>
	 * <p>
	 * Useful to highlight the human controlled player's locations.
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
	 * @pre		<code>otherCmds</code> sorted using Collections.sort()
	 * @post	all commands in <code>otherCmds</code> added to <code>otherActionPanel</code> as JButtons
	 * @post	<code>allCommandButtons</code> updated with the new JButtons added.
	 */
	private static void addOtherActionButtons(ArrayList<SWActionInterface> otherCmds){
		
		//heights and widths for the action buttons
		final int BUTTON_HEIGHT = 50;
		final int BUTTON_WIDTH  = otherActionPanel.getWidth();
	
		
		otherActionPanel.removeAll(); //remove other action buttons from previous layout
		otherActionPanel.setLayout(new BoxLayout(otherActionPanel, BoxLayout.Y_AXIS));
		
		
		//add other buttons
		for (SWActionInterface cmd : otherCmds) {
			
			
			JButton actionButton = new JButton();
			actionButton.setText(cmd.getDescription());
			actionButton.setMaximumSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));;
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
	 * @post	<code>allCommandButtons</code> updated with the new JButtons added.
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
	
	/**
	 * Sets up the main layout to display grid and action panels. This method attempts to optimize the 
	 * screen real estate for the map and controls for different screen sizes as follows,
	 * <ul>
	 * 	<li>If the height of the screen is larger than the total map height, the window height is reduced to match the map height</li>
	 *  <li>If the height of the screen is smaller than the total map height, the window is left maximized</li>
	 * 	<li>If the screen is too wide, the map takes it's required width and the rest is taken up by the controls</li>
	 * 	<li>If the screen is too narrow, the map and controls each take 50% of the total screen width</li>
	 * </ul> 
	 * 
	 * TODO: - Dealing with scroll bar widths so that the map could be displayed without horizontal scrolls
	 * 		 - Dealing with scroll bar widths and title bar height to avoid vertical scrolls if possible.
	 * 		 - Changing the scroll policies didn't seem to work :( 
	 * 		 - Asel
	 * 
	 * @author 	Asel
	 */
	private void drawLayout(){	
		
		//window open on full screen
		Rectangle windowFrame = this.getBounds();
		int windowHeight = windowFrame.height;
		int windowWidth  = windowFrame.width;
				
		//get the max width of the map
		int recMapWidth = locWidth * grid.getWidth(); //recommended width for the map
		int recMapHeight = locHeight * grid.getHeight(); //recommended height for the map. This is also the maximum height for the window
		
		//set maximum height of the frame to the map height if the screen is too high
		//this is required to avoid blank spaces in the map (spaces without a texture)
		if (windowHeight>recMapHeight) {
			this.setSize(windowWidth, recMapHeight);
		}
		
		int recControlsWidth = 600; //minimum recommended width for the controls
		int totalReqWidth = recMapWidth + recControlsWidth;
		
		int mapPanelWidth;
		int controlPanelWidth;
		
		//here we are trying to determine the width for the control and map panels based on screen size
		
		if (windowWidth == totalReqWidth) {//perfect display width
			mapPanelWidth = recMapWidth;
			controlPanelWidth = recControlsWidth;
		}
		else if (windowWidth>totalReqWidth) {//we have a ultra wide display
			mapPanelWidth = recMapWidth; //map can take it's recommended width
			controlPanelWidth = windowWidth -mapPanelWidth; //the controls can take up the rest
		}
		else if (windowWidth>recControlsWidth && windowWidth<totalReqWidth) {//we have room for the controls but not entirely for the map
			controlPanelWidth = recControlsWidth;//controls take their recommended width
			mapPanelWidth = windowWidth - controlPanelWidth; //the map takes the little space left
		}
		else {//we have a display whos width doesn't even have enough room for the controls
			controlPanelWidth = windowWidth/2; //we'll give 50% for each
			mapPanelWidth = windowWidth/2;
		}
					
		
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		mapPanel.setMaximumSize(new Dimension(mapPanelWidth, windowHeight));
		this.add(mapPanel);
 		//this.add(new JScrollPane(mapPanel));//map panel added
		
		
		controlPanel.setLayout(new GridLayout(3, 1));//Layout with 3 rows. For lblMessages, moveActionPanel and otherActionPanel
				
		controlPanel.add(new JScrollPane(lblMessages));
		controlPanel.add(new JScrollPane(moveActionPanel));
		controlPanel.add(new JScrollPane(otherActionPanel));
				
		controlPanel.setMaximumSize(new Dimension(controlPanelWidth, windowHeight));
		
		this.add(controlPanel);//add control panel to main layout		
	}
	

	/**
	 * Clear the message buffer after tick
	 * @author Asel
	 * @see ({@link #messageBuffer}
	 */
	private static void clearMessageBuffer(){
		messageBuffer="";
	}
}
