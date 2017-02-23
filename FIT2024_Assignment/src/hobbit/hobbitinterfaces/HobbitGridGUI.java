package hobbit.hobbitinterfaces;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import edu.monash.fit2024.gridworld.Grid;
import edu.monash.fit2024.gridworld.GridRenderer;
import edu.monash.fit2024.gridworld.Grid.CompassBearing;
import edu.monash.fit2024.simulator.matter.ActionInterface;
import edu.monash.fit2024.simulator.matter.Actor;
import edu.monash.fit2024.simulator.matter.EntityManager;
import hobbit.HobbitActionInterface;
import hobbit.HobbitActor;
import hobbit.HobbitEntity;
import hobbit.HobbitEntityInterface;
import hobbit.HobbitGrid;
import hobbit.HobbitLocation;
import hobbit.MiddleEarth;
import hobbit.actions.Move;

/**
 * This is a graphical based user interface for the simulation which offers better graphics than
 * <code>HobbitGridBasicGUI</code>. Is responsible for outputting a graphical map and messages on a JFrame
 * window and also obtain user selection of commands from the same JFrame window.
 * <p>
 * Its operations are controlled by the <code>HobbitGridController</code>
 * 
 * @author Asel
 * @see    {@link HobbitGridBasicGUI}
 */
public class HobbitGridGUI extends JFrame implements GridRenderer {
	
	/**The grid of the world*/
	private static HobbitGrid grid;
	
	/**JPanel on which the <code>HobbitGrid</code> is painted on*/
	private JPanel mapPanel = new JPanel();
	
	/**JPanel that contains the <code>lblMessages</code> label and other panels for action buttons (<code>moveActionPanel</code> and <code>otherActionPanel</code>)*/
	private JPanel controlPanel = new JPanel();
	
	/**Label that displays messages. Is contained in {@link #actionPanel}*/
	private JLabel lblMessages = new JLabel();
	
	/**Panel that contains action buttons corresponding to move commands. Is contained in {@link #actionPanel}*/
	private static JPanel moveActionPanel = new JPanel();
	
	/**Panel that contains action buttons corresponding to non movement commands. Is contained in {@link #actionPanel}*/
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
	
	/**List to store Images of textures. Loaded only once for better performance*/
	private List<BufferedImage> textures;
	
	/**List to store Images of Entities. Loaded only once for better performance*/
	private List<BufferedImage>	entities;
	
	
	/**
	 * Constructor for the <code>HobbitGridGUI</code>. This will load the image assets into their lists
	 * (<code>textures</code> and <code>entities</code>),draw the basic layout on the window and open it as a maximized window.
	 *  
	 * @pre 	grid should not be null
	 * @param 	grid the grid of the world
	 * @see 	{@link #textures}
	 * @see 	{@link #entities}
	 */
	public HobbitGridGUI(HobbitGrid grid) {
		
		loadTextures();		
		loadEntities();
		
		HobbitGridGUI.grid = grid;
		drawLayout();
		
		//setting a title and opening the window in full screen
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Hobbit World");
        this.setVisible(true);
	}
	
	
	@Override
	public void displayMap() {
		final int gridHeight = grid.getHeight();
		final int gridWidth  = grid.getWidth();
		
		//the height and width of a location tile
		final int locHeight = 100;
		final int locWidth  = 100;
		

		JPanel map = new JPanel();
		map.setLayout(new GridLayout(gridHeight, gridWidth));
		map.setPreferredSize(new Dimension(locHeight*gridHeight, locWidth*gridWidth));
	
		
		for (int row = 0; row< gridHeight; row++){ //for each row
			for (int col = 0; col< gridWidth; col++){ //each column of a row
				
				HobbitLocation loc = (HobbitLocation) grid.getLocationByCoordinates(col, row);
								
				//paint the texture
				BufferedImage imgLoc = getLocationTexture(loc);
				ImagePanel locTile = new ImagePanel(resizeImage(imgLoc, locHeight, locWidth));
				locTile.setToolTipText(loc.getLongDescription());
				
				
				EntityManager<HobbitEntityInterface, HobbitLocation> em = MiddleEarth.getEntitymanager();
				
				//get the Contents of the location
				List<HobbitEntityInterface> contents = em.contents(loc);
				
				if (!(contents == null || contents.isEmpty())) {//there is content
					int numItems = contents.size(); //the number of items
					
					int gridSize = (int) Math.ceil(Math.sqrt(numItems)); //we need a gridSize X gridSize grid to show all the items
					
					locTile.setLayout(new GridLayout(gridSize, gridSize));
					
					//the height of the item image so that all the items could fit in the location tile locTile
					int itemHeight = (int) (locHeight / gridSize); 
					int itemWidth  = (int) (locWidth / gridSize); 
					
					//add the images of the contents to the location tile
					for (HobbitEntityInterface a : contents) {
						
						BufferedImage imgItem = getEntityPicture(a);
						
						ImageIcon iconItem = new ImageIcon(resizeImage(imgItem, itemHeight, itemWidth));
						JLabel lblItem = new JLabel();
						lblItem.setIcon(iconItem);
						
						locTile.add(lblItem);
					}
				}
							
				map.add(locTile);
				
			}
			
		}
		
		JScrollPane mapScrollPane = new JScrollPane(map);
		
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
		
		ArrayList<HobbitActionInterface> moveCmds = new ArrayList<HobbitActionInterface>(); //all move commands
		ArrayList<HobbitActionInterface> otherCmds = new ArrayList<HobbitActionInterface>(); //all other non move commands

		//Separate the move and other commands
		for (ActionInterface ac : cmds) {
			
			if (((HobbitActionInterface) ac).isMoveCommand()){ //move commands
				moveCmds.add((HobbitActionInterface)ac);
			}
			else{ //non move commands
				otherCmds.add((HobbitActionInterface)ac);
			}
			
		}
		
		//sort the move commands in a defined order
		sortMoveCommands(moveCmds);
		
		//sort other commands for prettier output
		Collections.sort(otherCmds);
		
		ArrayList<HobbitActionInterface> allCmds = new ArrayList<HobbitActionInterface>(); //all commands
		
		//add move commands to list of all commands first
		for (HobbitActionInterface ac:moveCmds){
			allCmds.add(ac);
		}
		
		//next add the other commands
		for (HobbitActionInterface ac:otherCmds){
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
	 * Sort the Move Commands in a predefined order @see {@link #definedOrder}
	 * 
	 * @author	Asel
	 * @param 	moveCmds move commands to be sorted
	 * @pre 	<code>moveCmds</code> should only contain <code>HobbitActionInterface</code> objects that are instances of <code>Move</code>
	 */
	private static void sortMoveCommands(ArrayList<HobbitActionInterface> moveCmds){
		
		Comparator<HobbitActionInterface> comparator = new Comparator<HobbitActionInterface>(){

		    @Override
		    public int compare(final HobbitActionInterface moveAction1, final HobbitActionInterface moveAction2){
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
	private static void addOtherActionButtons(ArrayList<HobbitActionInterface> otherCmds){
		
		otherActionPanel.removeAll(); //remove other action buttons from previous layout
		otherActionPanel.setLayout(new GridLayout(otherCmds.size(), 1));//Grid to contain non movement commands
		
		//add other buttons
		for (HobbitActionInterface cmd : otherCmds) {
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
	 * @post	<code>allCommandButtons</code> updated with the new JButtons added.
	 */
	private static void addMoveActionButtons(ArrayList<HobbitActionInterface> moveCmds){
		
		moveActionPanel.removeAll(); //remove move action buttons from previous layout
		moveActionPanel.setLayout(new GridLayout(3,3)); //3X3 grid for all 8 directions plus the empty space in the middle
		
		//the index of the move command in the list that is currently being processed
		int moveCmdIndex = 0;

		//this code is a bit tricky so I'll try my best to explain using the power of comments
		for (int angle : definedOrder){ //we looping through each and every angle in the defined order
			
			if (moveCmdIndex < moveCmds.size()){ //if there are move commands in moveCmd that have not been added yet
				
				HobbitActionInterface currMoveCommand = moveCmds.get(moveCmdIndex);
				
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
	 * Sets up the main layout to display grid and action panels
	 * 
	 * @author 	Asel
	 * @post 	<code>gridPanel</code> takes up half the screen and the <code>actionPanel</code> takes up the rest
	 */
	private void drawLayout(){		
 		this.setLayout(new GridLayout(1,2));//Layout with 2 columns. One for the gridPanel and the other for the actionPanel
		this.add(mapPanel);
		
		
		controlPanel.setLayout(new GridLayout(3, 1));//Layout with 3 rows. For lblMessages, moveActionPanel and otherActionPanel
		controlPanel.add(lblMessages);
		controlPanel.add(moveActionPanel);
		controlPanel.add(otherActionPanel);
				
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
	
	/**
	 * Method that resizes a given <code>BufferedImage</code> to another <code>BufferedImage</code> of given dimensions
	 * <p>
	 * This method protects the transparencies, i.e. the transparent background of PNG are not filled with black. This method doesn't attempt to scale images to maintain the aspect ratios.
	 * 
	 * @author Asel
	 *  
	 * @param 	image to be resize
	 * @param 	height of the resized image
	 * @param 	width of the resized image
	 * 
	 * @pre		<code>height</code> and width <code>width</code> are positive integers
	 * @return 	a resized <code>BufferedImage</code> of height <code>height</code> and width <code>width</code>
	 */
	private BufferedImage resizeImage(BufferedImage image,int height,int width) {
		
		//new buffered image. TYPE_INIT_ARGB used to protect the transparencies
	    BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D graphics2D = resizedImg.createGraphics();

	    //The graphic object graphic2D used to draw a new resized image to buffer
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, width, height, null);
	    graphics2D.dispose();
		
	   return resizedImg;
	}
	
	
	/**
	 * <code>ImagePanel</code> class that extends a <code>JPanel</code> with a background image
	 * 
	 * @author Asel
	 *
	 */
	private class ImagePanel extends JPanel{

		/**Background image of the <code>ImagePanel</code>*/
	    private BufferedImage image;

	    /**
	     * Constructor for an ImagePanel.
	     * 
	     * @param 	image the <code>BufferedImage</code> that should be set as the background of this <code>ImagePanel</code>
	     * @post 	<code>ImagePanel</code> has the <code>image</code> as its background
	     * @see 	{@link #image}
	     */
	    public ImagePanel(BufferedImage image) {
	       this.image = image;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);           
	    }

	}
	
	/**
	 * This method performs an Image IO read to load the texture bitmaps into the array of <code>BufferedImages</code> <code>textures</code>.
	 * The textures should loaded at the respective indices as follows,
	 * <ul>
	 * 	<li>Dirt texture at index 0</li>
	 * 	<li>Grass texture at index 1</li>
	 * 	<li>Dark Grass texture at index 2</li>
	 * 	<li>Water texture at index 3</li>
	 * </ul>
	 * 
	 * @author 	Asel
	 * @post	<code>entities</code> is of size 4 with the texture bitmaps loaded as <code>BufferedImages</code> in the stated order.
	 */
	private void loadTextures() {
		textures = new ArrayList<BufferedImage>();
		String folderPath = "hobbitGUIResources/textures/";
		
		BufferedImage imgDirt 		= null;
		BufferedImage imgGrass 		= null;
		BufferedImage imgDarkGrass 	= null;
		BufferedImage imgWater 		= null;
		
		try {
			imgDirt 		= ImageIO.read(getClass().getResource(folderPath+"dirt.png"));
			imgGrass 		= ImageIO.read(getClass().getResource(folderPath+"grass.png"));
			imgDarkGrass 	= ImageIO.read(getClass().getResource(folderPath+"darkgrass.png"));
			imgWater 		= ImageIO.read(getClass().getResource(folderPath+"water.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		textures.add(imgDirt);
		textures.add(imgGrass);
		textures.add(imgDarkGrass);
		textures.add(imgWater);
		
	}

	/**
	 * Method that return a texture buffered image for a given <code>HobbitLocation</code>.This method uses the 
	 * symbol of the <code>HobbitLocation</code> to query for the texture.
	 * <p>
	 * This method returns,
	 * <ul>
	 * 		<li>Dirt texture for The Shire</li>
	 * 		<li>Grass texture for Bag End and Middle Earth</li>
	 * 		<li>Dark Grass texture for Mirkwood Forest</li>
	 * 		<li>Water texture for The River Sherbourne</li>
	 * 		<li>Grass texture for all other locations</li>
	 * </ul>
	 * 
	 * @author 	Asel
	 * @param 	loc the <code>HobbitLocation</code> being queried
	 * @return	the texture <code>BufferedImage</code> for the corresponding to the <code>HobbitLocation</code>
	 * @see		{@link hobbit.HobbitLocation#getSymbol()}
	 */
	private BufferedImage getLocationTexture(HobbitLocation loc) {
		final List<Character> dirtLocations 		= Arrays.asList('S');
		final List<Character> grassLocations 		= Arrays.asList('b','.');
		final List<Character> darkGrassLocations 	= Arrays.asList('F');
		final List<Character> waterLocations		= Arrays.asList('R');
		
		
		char locSymbol = loc.getSymbol();
		
		if (dirtLocations.contains(locSymbol)) {
			return textures.get(0);
		}
		else if (grassLocations.contains(locSymbol)) {
			return textures.get(1);
		}
		else if (darkGrassLocations.contains(locSymbol)) {
			return textures.get(2);
		}
		else if (waterLocations.contains(locSymbol)) {
			return textures.get(3);
		}
		else {
			return textures.get(0);
		}
		
	}
 	
	/**
	 * Method that return a buffered image for a given <code>HobbitEntityInterface</code>.This method uses the 
	 * symbol of the <code>HobbitEntity</code> to query for the Image of the entity.
	 * <p>
	 * This method will return,
	 * <ul>
	 * 		<li>
	 * 		The respective images of <code>HobbitEntityInterface</code>s for
	 * 			<ul>
	 * 				<li>Axe</li>
	 *				<li>Sword</li>
	 * 				<li>Tree</li>
	 * 				<li>Wood</li>
	 * 				<li>Suit</li>
	 * 				<li>Ring</li>
	 * 				<li>Treasure</li>
	 * 				<li>Goblin</li>
	 * 				<li>Dorko</li>
	 * 				<li>Biblo</li>
	 * 			</ul>
	 * 		</li>
	 * 			<li>An unknown image (an image with a question mark in a yellow circle), otherwise</li>
	 * 	</ul>
	 * 
	 * @author 	Asel
	 * @param 	a the <code>HobbitEntityInterface</code> being queried
	 * @return	the <code>BufferedImage</code> for the corresponding to the Hobbit Entity
	 * @see		{@link hobbit.HobbitActor#getSymbol()}
	 */
	private BufferedImage getEntityPicture(HobbitEntityInterface a) {
				
				
		final String Axe 		= "Æ";
		final String Sword 		= "s";
		final String Tree	 	= "T";
		final String Wood 		= "w";
		final String Suit 		= "c";
		final String Ring 		= "o";
		final String Treasure	= "×";
		final String Goblin		= "g";
		final String Dorko		= "d";
		final String Biblo		= "@";
			
		
		String hobbitSymbol = a.getSymbol();
		
		if (hobbitSymbol.matches(Axe)) {
			return entities.get(0);
		}
		else if (hobbitSymbol.matches(Sword)) {
			return entities.get(1);
		}
		else if (hobbitSymbol.matches(Tree)) {
			return entities.get(2);
		}
		else if (hobbitSymbol.matches(Wood)) {
			return entities.get(3);
		}
		else if (hobbitSymbol.matches(Suit)) {
			return entities.get(4);
		}
		else if (hobbitSymbol.matches(Ring)) {
			return entities.get(5);
		}
		else if (hobbitSymbol.matches(Treasure)) {
			return entities.get(6);
		}
		else if (hobbitSymbol.matches(Goblin)) {
			return entities.get(7);
		}
		else if (hobbitSymbol.matches(Dorko)) {
			return entities.get(8);
		}
		else if (hobbitSymbol.matches(Biblo)) {
			return entities.get(9);
		}
		else {
			return entities.get(10);
		}
		
		
		
	}

	/**
	 * This method performs an Image IO read to load the bitmaps of the entities into the array of <code>BufferedImages</code> 
	 * <code>entities</code>. The images of the entities should loaded at the respective indices as follows,
	 * <ul>
	 * <li>Image of the Axe at index 0</li>
	 * <li>Image of the Sword at index 1</li>
	 * <li>Image of the Tree at index 2</li>
	 * <li>Image of the Wood at index 3</li>
	 * <li>Image of the Suit at index 4</li>
	 * <li>Image of the Ring at index 5</li>
	 * <li>Image of the Treasure at index 6</li>
	 * <li>Image of the Goblin at index 7</li>
	 * <li>Image of the Dorko at index 8</li>
	 * <li>Image of the Biblo at index 9</li>
	 * </ul>
	 * @author 	Asel
	 * @post	<code>entities</code> is of size 11 with the bitmaps loaded as <code>BufferedImages</code> in the stated order.
	 */
	private void loadEntities() {
		
		entities = new ArrayList<BufferedImage>();
		String folderPath = "hobbitGUIResources/entities/";
		
		BufferedImage imgAxe 		= null;
		BufferedImage imgSword 		= null;
		BufferedImage imgTree	 	= null;
		BufferedImage imgWood 		= null;
		BufferedImage imgSuit 		= null;
		BufferedImage imgRing 		= null;
		BufferedImage imgTreasure	= null;
		BufferedImage imgGoblin		= null;
		BufferedImage imgDorko		= null;
		BufferedImage imgBiblo		= null;
		BufferedImage imgUnknown	= null;
		
		try {
			imgAxe 		= ImageIO.read(getClass().getResource(folderPath+"axe.png"));
			imgSword 	= ImageIO.read(getClass().getResource(folderPath+"sword.png"));
			imgTree	 	= ImageIO.read(getClass().getResource(folderPath+"tree.png"));
			imgWood 	= ImageIO.read(getClass().getResource(folderPath+"logs.png"));
			imgSuit 	= ImageIO.read(getClass().getResource(folderPath+"suit.png"));
			imgRing 	= ImageIO.read(getClass().getResource(folderPath+"ring.png"));
			imgTreasure	= ImageIO.read(getClass().getResource(folderPath+"treasure.png"));
			imgGoblin	= ImageIO.read(getClass().getResource(folderPath+"goblin.png"));
			imgDorko	= ImageIO.read(getClass().getResource(folderPath+"dorko.png"));
			imgBiblo	= ImageIO.read(getClass().getResource(folderPath+"biblo.png"));
			imgUnknown	= ImageIO.read(getClass().getResource(folderPath+"unknown.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		entities.add(imgAxe);
		entities.add(imgSword);
		entities.add(imgTree);
		entities.add(imgWood);
		entities.add(imgSuit);
		entities.add(imgRing);
		entities.add(imgTreasure);
		entities.add(imgGoblin);
		entities.add(imgDorko);
		entities.add(imgBiblo);
		entities.add(imgUnknown);		
		
	}
}
