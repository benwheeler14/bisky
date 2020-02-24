package players;

import java.awt.Point;

import game.*;

public class Second implements Player {
	
	private String name;
	private int age;
	
	public Second(String name) {
		this.name = name;
		this.age = -1;
	}

	/**
     * Picks the next Tile to play based on a give Board and Hand
     * This Player picks the first legal Tile in the hand starting with the last tile and counting backwards
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		for (int i = hand.getNumTiles() - 1; i > -1; i--) {
			Tile tile = hand.get(i);
			for (int j = 0; j < 4; j++) {
				tile.rotate(1);
				if (RuleChecker.validTilePlacement(board, this.getName(), tile, hand)) {
					return tile;
				}
			}
		}
		return hand.get(hand.getNumTiles() - 1);
	}
	/**
     * Picks the initial Tile to play based on a give Board and Hand
     * This Player picks the last tile in the hand
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	public Tile pickInitialTile(TestableBoard board, Hand hand) {
		return hand.get(hand.getNumTiles() - 1);
	}
	/**
     * Picks the initial Position based on a give Board and Hand
     * This Player picks the first available Position on the top edge of the Board starting it's search from 0,0 going clockwise
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return the Position to place this players Token (and first Tile).
	 */
	public Position pickInitialPlacement(TestableBoard board) {
		Point point = new Point(0, 0);
    	while(board.hasTile(point)) {
    		point.translate(0, 2);
    	}
    	return new Position(point, 7);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FirstMove pickFirstMove(TestableBoard board, Hand hand) {
		FirstMove firstMove = new FirstMove();
		firstMove.position = this.pickInitialPlacement(board);
		firstMove.tile = this.pickInitialTile(board, hand);
		return firstMove;
	}
}
