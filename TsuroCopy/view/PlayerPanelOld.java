package view;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Player (hand) graphics
 */
public class PlayerPanelOld extends JPanel {

    ArrayList<Tile> hand;
    String player;

    public PlayerPanelOld() {
        super();
    }

    /**
     * Sets the hand being delt
     * @param hand
     */
    public void setHand(Hand hand) {
        this.hand = hand.getAllTiles();
    }

    /**
     * Sets the player whose turn it is
     * @param player
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Paints the graphics of the hand and the player whose turn it is
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawString("Current Player is " + this.player, 0,50);

        final int width_Height = BoardPanelOld.WIDTH_HEIGHT;
        final int PLAYER_SIZE = BoardPanelOld.PLAYER_SIZE;
        final Color colorOfRect = BoardPanelOld.RECTANGLE_COLOR;
        final Color colorOfLine = BoardPanelOld.LINE_COLOR;


        int ctrlXY = width_Height / 2; //30
        //Find where each ports are
        int port = width_Height / 3; //20

        int size = hand.size();
        for (int i = 0; i < size; i ++) {

                Tile t = hand.get(i);

                    List<Integer> edges = t.getListOfPorts();
                    int xPos = i * 2 * width_Height;
                    int yPos = width_Height;

                    //For the outline
                    g2.drawRect(xPos, yPos, width_Height, width_Height);
                    //Fill the color
                    g2.setColor(colorOfRect);
                    Rectangle rec = new Rectangle(xPos, yPos, width_Height, width_Height);
                    g2.fill(rec);

                    for (int p = 0; p < 7; p += 2) {

                        int edge_1 = edges.get(p);
                        int[] posn1 = BoardPanelOld.edgeToPosn(edge_1, port);
                        int x1 = posn1[0];
                        int y1 = posn1[1];

                        int edge_2 = edges.get(p + 1);
                        int[] posn2 = BoardPanelOld.edgeToPosn(edge_2, port);
                        int x2 = posn2[0];
                        int y2 = posn2[1];


                        g2.setColor(colorOfLine);
                        g2.setStroke(new BasicStroke(2));
                        QuadCurve2D q = new QuadCurve2D.Float();
                        q.setCurve(xPos + x1, yPos + y1, xPos + ctrlXY,
                                yPos + ctrlXY, xPos + x2, yPos + y2);
                        g2.draw(q);

                    }
                }


    }

}
