package players;

import java.awt.Point;

import game.Board;

public class CenterPlayer extends HeuristicPlayer {

	public CenterPlayer(String name) {
		super(name);
	}

	/**  
	 * Assigns high values to board states that have the Position of this player close to the center of the Board
	 */
	@Override
	protected int getValue(Board board) {
		Point point = board.getPlayerPosition(this.getName()).getPoint();
		int maxDistance = 10;
		return maxDistance - this.getDistanceFromCenter(point);
	}
	
	/**
	 * Gets the distance of a point to the center of the Board as the sum of the horizontal and vertical distances
	 * (only works on Boards with even widths)
	 * @param point
	 * @return the distance to the center of the board
	 */
	private int getDistanceFromCenter(Point point) {
		int centerHigh = Board.BOARD_SIZE / 2;
		int centerLow = Board.BOARD_SIZE / 2 - 1 ;
		int x = point.x;
		int y = point.y;
		int xDistance, yDistance;
		if (x < centerLow) {
			xDistance = centerLow - x;
		}
		else if (x > centerHigh) {
			xDistance = x - centerHigh;
		}
		else {
			xDistance = 0;
		}
		if (y < centerLow) {
			yDistance = centerLow - y;
		}
		else if (y > centerHigh) {
			yDistance = y - centerHigh;
		}
		else {
			yDistance = 0;
		}
		return xDistance + yDistance;
	}

}
