package game;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * A class representing the Position (where a player token can be) in a game of Tsuro
 * the data structure has two elements: an xy coordinate specifying the Tile in the Board's matrix,
 * and an int specifying the port on that Tile that this Position represents
 * @author bhend
 *
 */
public class Position implements Serializable {  //a class representing a valid position of a player's avatar on a game board
	
	private Point point; //x, y coordinate of the position
	private int port; //index of the port of the position

	/**
	 * Constructor
	 * @param point
	 * @param port
	 */
	public Position(Point point, int port) {
		this.point = point;
		this.port = port;
	}
	
	/**
	 * @return the x, y component of this Position
	 */
	public Point getPoint() {
		return point;
	}
	
	/**
	 * @return the port index of this Position
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @return the Position adjacent to this Position
	 * the adjacent Position the Position bordering this one, with the port that lines up with this one's
	 */
	public Position getAdjacentPosition() {
		int port = this.getPort();
		int adjacentPort = this.getAdjacentPort();
		Point adjacentPoint = this.getPoint().getLocation();
		if (port == 0 || port == 1) {
			adjacentPoint.translate(0, -1);
		}
		if (port == 2 || port == 3) {
			adjacentPoint.translate(1, 0);
		}
		if (port == 4 || port == 5) {
			adjacentPoint.translate(0, 1);
		}
		if (port == 6 || port == 7) {
			adjacentPoint.translate(-1, 0);
		}
		return new Position(adjacentPoint, adjacentPort);
	}
	
	/**
	 * @return the port index that will always line up with this ones on a different tile
	 */
	private int getAdjacentPort() {
		switch (this.port) {
			case 0:
				return 5;
			case 1:
				return 4;
			case 2:
				return 7;
			case 3:
				return 6;
			case 4:
				return 1;
			case 5:
				return 0;
			case 6:
				return 3;
			case 7:
				return 2;
			default:
				return -1; //invalid port
		}
	}
	
	/**
	 * Determines whether the Position is on the periphery
	 * A Position is on the periphery if it's point is on one of the four edges of the board
	 * and it's port is facing out of the Board
	 * @return
	 */
	public boolean onPeriphery() {
		if (point.y == 0 && (port == 0 || port == 1)) {
			return true;
		}
		if (point.y == Board.BOARD_SIZE - 1 && (port == 4 || port == 5)) {
			return true;
		}
		if (point.x == 0 && (port == 6 || port == 7)) {
			return true;
		}
		if (point.x == Board.BOARD_SIZE - 1 && (port == 2 || port == 3)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return a list of all Points adjacent to this ones
	 */
	public ArrayList<Point> getAdjacentPoints() {
		Point north = point.getLocation();
		Point east = point.getLocation();
		Point south = point.getLocation();
		Point west = point.getLocation();
		north.translate(0, -1);
		east.translate(1, 0);
		south.translate(0, 1);
		west.translate(-1, 0);
		
		return new ArrayList<Point>(Arrays.asList(north, east, south, west));
	}
	/**
	 * @return whether this object is equals to that.
	 * the two objects are equal if they represent that same Position on the Board
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Position)) {
			return false;
		}
		Position l = (Position) o;
		return this.getPoint().equals(l.getPoint()) && this.getPort() == l.getPort();
	}
	/**
	 * @return a deep clone of this Position
	 */
	public Position getCopy() {
		Position copy = new Position(this.getPoint().getLocation(), this.getPort());
		return copy;
	}
	
	@Override 
	public String toString() {
		String s = "Point: (" + this.point.x + ", " + this.point.y + ") Port: " + this.port;
		return s;
	}
}