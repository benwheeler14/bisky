package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import game.*;

/**
 * A window for viewing a game of Tsuro
 * using JFrame
 * Divides Frame into two panels, a Board panel on the left and a Button on the right
 * The button makes advances the Game one turn, then refreshes the view
 */
public class Observer extends JFrame{
	
	public static final int FRAME_WIDTH = 1200;
	public static final int FRAME_HEIGHT = 1200;
	
	private final BoardPanel boardPanel;
	private final JSplitPane splitPane;
	
	public Observer(Game game) {
		
		boardPanel = new BoardPanel(game); //create BoardPanel
		this.add(boardPanel);
		boardPanel.repaint();
		
		JButton button = new JButton("Click for Next Turn");  //create Button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //method that gets executed when the button is clicked
				game.playNextTurn();   //play the next turn
				refresh();             //refresh the board
				if (game.isGameOver()) {  //if the game is over, print the result then exit
					Podium podium = game.getPodium();
					Logging.print(podium.toString());
					System.exit(0);
				}
			}
		});
		

		splitPane = new JSplitPane();	//create split pane
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.getContentPane().setLayout(new GridLayout());
		this.getContentPane().add(splitPane);
		
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(FRAME_WIDTH / 2);   //divide the pane down the middle
		
		splitPane.setLeftComponent(boardPanel);  //set board to left pane
		splitPane.setRightComponent(button);     //set button to right pane
		
		
		//this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.pack();
	}
	/**
	 * Refresh the graphical elements of this frame
	 */
	public void refresh() {
		boardPanel.repaint();
		this.pack();
	}
}
