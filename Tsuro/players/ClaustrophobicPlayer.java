package players;

import java.awt.Point;
import java.util.ArrayList;

import game.*;

public class ClaustrophobicPlayer extends HeuristicPlayer {

	public ClaustrophobicPlayer(String name) {
		super(name);
	}

	/**
	 * Favors Positions that have few adjacent Tiles
	 */
	@Override
	protected int getValue(Board board) {
		Position position = board.getPlayerPosition(this.getName());
		ArrayList<Point> adjacentPoints = this.getAdjacentPoints(position.getPoint());
		int value = 8; //max number of adjacent tiles is 8;
		for (Point point : adjacentPoints) {
			if (board.hasTile(point)) {
				value = value - 1;
			}
		}
		return value;
	}
	/**
	 * Gets a List of Points adjacent to the given one (Diagonals count)
	 * @param point
	 * @return a list of adjacent Points
	 */
	private ArrayList<Point> getAdjacentPoints(Point point) {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				Point p = point.getLocation();
				p.translate(x, y);
				points.add(p);
			}
		}
		return points;
	}

}
