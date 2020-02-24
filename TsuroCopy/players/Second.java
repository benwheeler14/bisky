package players;

import java.awt.Point;

import game.Hand;
import game.Position;
import game.RuleChecker;
import game.TestableBoard;
import game.Tile;

public class Second implements Player {
	
	private String name;
	private int age;
	
	public Second(String name) {
		this.name = name;
		this.age = -1;
	}

	/**
	 * Starts with the last tile in the hand, and returns the first legal rotation
	 * of that tile
	 * If the last tile has no valid rotations, checks the next tile in the hand counting back from the end of the hand
	 * If there are no valid placements, returns the last tile in the hand with a rotation of zero.
	 */
	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		for (int i = Hand.HAND_SIZE - 1; i >= 0; i++) {
			Tile tile = hand.get(i);
			for (int j = 0; j < 4; j++) {
				tile.rotate(1);
				if (RuleChecker.validTilePlacement(board, this.getName(), tile, hand)) {
					return tile;
				}
			}
		}
		return hand.get(Hand.HAND_SIZE - 1);
	}

	@Override
	public Tile pickInitialTile(TestableBoard board, Hand hand) {
		return hand.get(2);
	}

	@Override
	public Position pickInitialPlacement(TestableBoard board) {
		Point point = new Point(0, 0);
    	while(board.hasTile(point)) {
    		point.translate(0, 2);
    	}
    	return new Position(point, 0);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getAge() {
		return this.age;
	}

	@Override
	public void setAge(int age) {
		this.age = age;

	}

}
