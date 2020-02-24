package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import game.*;

public class GameView extends JFrame {

    private BoardPanelOld boardPanel;
    private PlayerPanelOld playerPanel;
    private JPanel p;

    /**
     * Implementation of a View. Sets up the graphics of a game board.
     * @param vp view panel
     * @param vpp view player panel
     */
    public GameView(Board board) {
        super("Tsuro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new BoardPanelOld(board);
        playerPanel = new PlayerPanelOld();
        this.setPlayerData(new Hand(), "Test");

        p = new JPanel();

        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

        p.add(boardPanel);
        p.add(playerPanel);

        this.add(p);

      //  this.setPreferredSize(new Dimension(panel.getBoardSize(), panel.getBoardSize()));
        this.setPreferredSize(new Dimension(1200, 1200));
        this.pack();

    }
    
    public void setHand(Hand hand) {
    	playerPanel.setHand(hand);
    }
    
    public void setPlayer(String name) {
    	playerPanel.setPlayer(name);
    }
    
    public void setPlayerData(Hand hand, String name) {
    	playerPanel.setHand(hand);
    	playerPanel.setPlayer(name);
    }

    /**
     * Turns the output into a .png file
     */
    public void toPNG() throws IOException {
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        p.paint(graphics2D);
        ImageIO.write(image,"png", new File("tsuro.png"));
    }

    /**
     * Makes the game board visible
     */
    public void makeVisible() {
        this.setVisible(true);
    }
    
    /**
     * Repaints the game board
     */
    public void refresh() {
        boardPanel.repaint();
        playerPanel.repaint();
    }



}
