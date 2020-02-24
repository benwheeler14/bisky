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

public class Observer extends JFrame{
	
	public static final int FRAME_WIDTH = 1200;
	public static final int FRAME_HEIGHT = 1200;
	
	private final BoardPanel boardPanel;
	private final JSplitPane splitPane;
	private final JPanel rightPanel;
	private final JPanel leftPanel;
	
	private boolean buttonClicked;
	
	public Observer(Game game) {
		buttonClicked = false;
		
		//make panels
		//board panel
		boardPanel = new BoardPanel(game);
		this.add(boardPanel);
		boardPanel.repaint();
		
		JButton button = new JButton("CLICK HERE!");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//refresh();
				buttonClicked = true;
			}
		});
		
		//create split pane
		splitPane = new JSplitPane();
		rightPanel = new JPanel();
		leftPanel = new JPanel();
		this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.getContentPane().setLayout(new GridLayout());
		this.getContentPane().add(splitPane);
		
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(FRAME_WIDTH / 2);
		
		splitPane.setLeftComponent(boardPanel);
		splitPane.setRightComponent(button);
		
		
		//this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.pack();
	}
	
	public void refresh() {
		boardPanel.repaint();
		this.pack();
	}
	
	public void waitForButtonClick() {
		while (!buttonClicked) {
			 try {
     	        Thread.sleep(200);
     	    } catch (InterruptedException e) {
     	        // TODO Auto-generated catch block
     	        e.printStackTrace();
     	    }
		}
		buttonClicked = false;
	}
	
	public boolean checkButtonClicked() {
		if (buttonClicked == true) {
			buttonClicked = false;
			return true;
		}
		return false;
	}
}
