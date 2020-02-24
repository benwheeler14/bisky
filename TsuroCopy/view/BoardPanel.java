package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import game.*;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private Board board;
	private HashMap<String, Color> playerColors;
	
	public static final int TILE_SIZE = 60;
	public static final int NUM_TILES_PER_ROW = 10;
	public static final int BOARD_SIZE = TILE_SIZE * NUM_TILES_PER_ROW;
	public static final int PLAYER_SIZE = 10;
	
	public static final Color TILE_COLOR = Color.ORANGE;
    public static final Color LINE_COLOR = Color.gray;
    
    public BoardPanel(Game game) {
    	super();
    	this.board = board;
    	this.playerColors = new HashMap<String, Color>();
    	Iterator<Color> iter = Arrays.asList(Color.WHITE, Color.BLUE, Color.GREEN, Color.BLACK, Color.RED).iterator();
    	for (String name : board.getPlayerNames()) {
    		this.playerColors.put(name, iter.next());
    	}
    	this.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
    }
    
    private void paintTiles(Graphics2D g) {
    	for (int i = 0; i < Board.BOARD_SIZE; i++) {
    		for (int j = 0; j < Board.BOARD_SIZE; j++) {
    			Tile tile = this.board.getTile(new Point(i, j));
    			this.paintTile((Graphics2D) g, tile, i * TILE_SIZE, j * TILE_SIZE);
    		}
    	}
    }
    
    private void paintTile(Graphics2D g, Tile tile, int x, int y) {
    	g.setColor(LINE_COLOR);
    	g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
    	if (tile == null) {
    		return;
    	}
    	g.setColor(TILE_COLOR);
    	g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
    	g.setColor(LINE_COLOR);
    	ArrayList<Integer> ports = tile.getListOfPorts();
    	for (int i = 0; i < 8; i = i + 2) {
    		Point p1 = this.getOffsetFromPortNumber(ports.get(i));
    		Point p2 = this.getOffsetFromPortNumber(ports.get(i + 1));
    		Point center = new Point(TILE_SIZE / 2, TILE_SIZE / 2);
    		p1.translate(x, y);
    		p2.translate(x, y);
    		center.translate(x, y);
    		QuadCurve2D curve = new QuadCurve2D.Float();
    		curve.setCurve(p1, center, p2);
    		g.draw(curve);
    	}
    	//Rectangle rec = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
    	//g.fill(rec);
    }
    
    private void paintPlayerTokens(Graphics2D g) {
    	for (String playerName : board.getPlayerNames()) {
    		if (board.getPlayerPosition(playerName) != null && !board.playerDead(playerName)) {
    			Position playerPosition = board.getPlayerPosition(playerName);
        		Point boardPoint = playerPosition.getPoint();
        		int port = playerPosition.getPort();
        		Point drawPoint = new Point(boardPoint.x * TILE_SIZE, boardPoint.y * TILE_SIZE);
        		Point offset = this.getOffsetFromPortNumber(port);
        		drawPoint.translate(offset.x, offset.y);
        		drawPoint.translate(-(PLAYER_SIZE / 2), -(PLAYER_SIZE / 2));
        		

        		g.setColor(this.playerColors.get(playerName));
        		g.fillOval(drawPoint.x, drawPoint.y, PLAYER_SIZE, PLAYER_SIZE);
    		}
    	}
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	this.paintTiles((Graphics2D) g);
    	this.paintPlayerTokens((Graphics2D) g);
    	//this.paintTile((Graphics2D) g, Tile.getRandomTile(), 0, 0);
    }
    
    /**
     * Gets the offset for a given port on a tile as a Point. The offset is the x and y coordinate of
     * a port on a tile drawn at (0, 0)
     * eg: the offset for port 0 would be (TILE_SIZE / 4, 0)
     * @param port
     * @return the offset for that port
     */
    private Point getOffsetFromPortNumber(int port) {
    	int x = 0; //valid for ports 6, 7
    	int y = 0; //valid for ports 0, 1
    	
    	if (port == 0 || port == 5) {
    		x = TILE_SIZE / 4;
    	}
    	if (port == 1 || port == 4) {
    		x = (TILE_SIZE / 4) * 3;
    	}
    	if (port == 2 || port == 3) {
    		x = TILE_SIZE;
    	}
    	
    	if (port == 2 || port == 7) {
    		y = TILE_SIZE / 4;
    	}
    	if (port == 3 || port == 6) {
    		y = (TILE_SIZE / 4) * 3;
    	}
    	if (port == 4 || port == 5) {
    		y = TILE_SIZE;
    	}
    	return new Point(x, y);
    }
    
}
