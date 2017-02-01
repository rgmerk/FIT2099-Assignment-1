package hobbit;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

public class TestGUI extends JFrame {
	public TestGUI() {
		
		this.setTitle("Marvel World");
		//2 Columns. One for the map and the other for the controls
		setLayout(new GridLayout(1, 2));
		
		//panel for the map
		JPanel mapPanel = new JPanel();
		this.add(mapPanel);
		
		//panel for the controls
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(3,1));//grid to accommodate the status, move actions and other actions
		
		//label to display messages from the message renderer
		JLabel lblMessages = new JLabel();
		lblMessages.setText("MESSAGE HERE!");
		controlsPanel.add(lblMessages);
		
		//panel for move actions
		JPanel moveControls = new JPanel();
		moveControls.setLayout(new GridLayout(3,3));
		
		JButton btnNorthWest = new JButton();
		btnNorthWest.setText("NW");
		moveControls.add(btnNorthWest);
		
		JButton btnNorth = new JButton();
		btnNorth.setText("N");
		moveControls.add(btnNorth);
		
		JButton btnNorthEast = new JButton();
		btnNorthEast.setText("NE");
		moveControls.add(btnNorthEast);
		
		JButton btnWest = new JButton();
		btnWest.setText("W");
		moveControls.add(btnWest);
		
		//blank in the middle just add a label
		JLabel lblBlank = new JLabel();
		lblBlank.setText("");
		moveControls.add(lblBlank);		
		
		JButton btnEast = new JButton();
		btnEast.setText("E");
		moveControls.add(btnEast);
		
		JButton btnSouthWest = new JButton();
		btnSouthWest.setText("SW");
		moveControls.add(btnSouthWest);
		
		JButton btnSouth = new JButton();
		btnSouth.setText("S");
		moveControls.add(btnSouth);
		
		JButton btnSouthEast = new JButton();
		btnSouthEast.setText("SE");
		moveControls.add(btnSouthEast);
		
		//add the move controls
		controlsPanel.add(moveControls);
		
		//other actions
		JPanel otherControls = new JPanel();
		otherControls.setLayout(new GridLayout(10,1));
		
		//testing
		for (int i = 0; i<10; i++){
			JButton btnOtherAction = new JButton();
			btnOtherAction.setText("Other Action "+i);
			otherControls.add(btnOtherAction);
		}
		
		//add the other controls
		controlsPanel.add(otherControls);
		
		
		
		add(controlsPanel);
		
		createGrid(mapPanel, 10, 10);
		
		
	}
	
	private void createGrid(JPanel panel, int numberOfRows, int numberOfColumns)
	{
	    panel.setLayout(new GridLayout(numberOfRows, numberOfColumns));
	    for (int c = 0; c < numberOfColumns; c++)
	    {
	        for (int r = 0; r < numberOfRows; r++)
	        {
	        JButton button = new JButton();
	        button.addActionListener(new java.awt.event.ActionListener()
	        {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt)
	            {
	            //Add your code here for action event
	            }
	        });
	        panel.add(button);
	        }
	    }
	}

}
