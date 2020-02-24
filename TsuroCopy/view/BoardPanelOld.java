package view;

import game.Board;
import game.Position;
import game.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;


/**
 * Game board graphics
 */
public class BoardPanelOld extends JPanel {

	private Board board;
    private ArrayList<ArrayList<Tile>> tiles;
    private HashMap<String, Position> players;
    public static final int WIDTH_HEIGHT = 60;
    public static final int CTRL_XY = WIDTH_HEIGHT / 2;
    public static final int PORT = WIDTH_HEIGHT / 3;
    public static final int PLAYER_SIZE = 10;
    public static final Color RECTANGLE_COLOR = Color.ORANGE;
    public static final Color LINE_COLOR = Color.gray;

    public BoardPanelOld(Board board) {
        super();
        this.board = board;
        this.setPreferredSize(new Dimension(200,200));
        this.tiles = null;
        this.players = null;
    }

    /**
     * Gets the size of the board in pixels
     * @return int of board size
     */
    public int getBoardSize() {
        return this.WIDTH_HEIGHT * 12;
    }

    /**
     * Sets the tiles for the board.
     * @param tiles
     */
    public void setTiles(ArrayList<ArrayList<Tile>> tiles) {
        this.tiles = tiles;
    }

    /**
     * Sets the players for the board.
     * @param players
     */
    public void setPlayers(HashMap<String, Position> players) {
        this.players = players;
    }

    private void paintTile(Graphics2D g, Tile tile, int xPos, int yPos) {
    	 List<Integer> edges = tile.getListOfPorts();

         //For the outline
         g.drawRect(xPos, yPos, this.WIDTH_HEIGHT, this.WIDTH_HEIGHT);
         //Fill the color
         g.setColor(this.RECTANGLE_COLOR);
         Rectangle rec = new Rectangle(xPos, yPos, this.WIDTH_HEIGHT, this.WIDTH_HEIGHT);
         g.fill(rec);

         for (int p = 0; p < 7; p += 2) {

             int edge_1 = edges.get(p);
             int[] posn1 = edgeToPosn(edge_1, PORT);
             int x1 = posn1[0];
             int y1 = posn1[1];

             int edge_2 = edges.get(p + 1);
             int[] posn2 = edgeToPosn(edge_2, PORT);
             int x2 = posn2[0];
             int y2 = posn2[1];


             g.setColor(this.LINE_COLOR);
             g.setStroke(new BasicStroke(2));
             QuadCurve2D q = new QuadCurve2D.Float();
             q.setCurve(xPos + x1, yPos + y1, xPos + CTRL_XY,
                     yPos + CTRL_XY, xPos + x2, yPos + y2);
             g.draw(q);
         }
    }
    
    private void paintBoard(Graphics2D g) {
    	//draw grid
        for (int i = 0; i < 12; i++) {
            g.drawLine(WIDTH_HEIGHT, i * WIDTH_HEIGHT, 11 * WIDTH_HEIGHT, i * WIDTH_HEIGHT);
            g.drawLine(i * WIDTH_HEIGHT, WIDTH_HEIGHT, i * WIDTH_HEIGHT, 11 * WIDTH_HEIGHT);
        }
        ArrayList<ArrayList<Tile>> tiles = board.getTiles();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                Tile tile = tiles.get(x).get(y);
                if (tile != null) {
                	int xPos = (x + 1) * this.WIDTH_HEIGHT;
                    int yPos = (y + 1) * this.WIDTH_HEIGHT;
                    
                	this.paintTile(g, tile, xPos, yPos);
                }
            }
        }
    }
    
    /**
     * Paints the board with all the tiles
     */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		this.paintBoard(g2);

		// Draw PlayerToken

		HashMap<String, Position> player = this.players;

		int colorNum = 0;
		for (Map.Entry<String, Position> entry : board.getPlayerTokens().entrySet()) {
			String color = entry.getKey();
			Position pos = entry.getValue();

			int xPlayer = (int) (pos.getPoint().getX() + 1) * this.WIDTH_HEIGHT;
			int yPlayer = (int) (pos.getPoint().getY() + 1) * this.WIDTH_HEIGHT;
			int[] posn3 = edgeToPosn(pos.getPort(), PORT);
			int xPort = posn3[0];
			int yPort = posn3[1];

			g2.setColor(this.whatColor(colorNum));
			colorNum++;
			g2.fillOval(xPlayer + xPort - this.PLAYER_SIZE / 2, yPlayer + yPort - this.PLAYER_SIZE / 2,
					this.PLAYER_SIZE, this.PLAYER_SIZE);

		}
	}

    //posn[0] = x / posn[1] = y
    /**
     * Gets the x and y based on the edge and port
     * @param edge
     * @param port
     * @return posn for x and y
     */
    public static int[] edgeToPosn(int edge, int port) {
        int[] posn = new int[2];

        switch (edge) {
            case 0:
                posn[0] = port;
                posn[1] = 0;
                break;
            case 1:
                posn[0] = port * 2;
                posn[1] = 0;
                break;
            case 2:
                posn[0] = port * 3;
                posn[1] = port;
                break;
            case 3:
                posn[0] = port * 3;
                posn[1] = port * 2;
                break;
            case 4:
                posn[0] = port * 2;
                posn[1] = port * 3;
                break;
            case 5:
                posn[0] = port;
                posn[1] = port * 3;
                break;
            case 6:
                posn[0] = 0;
                posn[1] = port * 2;
                break;
            case 7:
                posn[0] = 0;
                posn[1] = port;
                break;
            default:
                break;
        }
        return posn;
    }
    //white", "black", "red", "green", "blue"
    /**
     * Returns a color based on the string given
     * @param color
     * @return Color
     */
    public Color whatColor(int i) {
        Color c;
        switch (i) {
            case 0:
                return Color.WHITE;
            case 1:
                return Color.BLACK;
            case 2:
                return Color.RED;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLUE;
            default:
                break;
        }
        throw new IllegalArgumentException("Wrong color");
    }


}
