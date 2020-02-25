package players;

import java.awt.Point;

import game.*;

/**
 * Dummy Interface that chooses tile with deterministic strategy.
 */
public class Dumb implements Player {

    //Name of the Player.
    private String name;
    //Age is determined by connection. Faster the player is connected, older the player is.
    private int age;

    public Dumb(String name) {
        this.name = name;
        this.age = -1;

    }
    /**
     * Picks the next Tile to play based on a give Board and Hand
     * This Player picks the first tile in the hand without rotation
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
    @Override
    public Tile pickTile(TestableBoard board, Hand hand) {

        Tile t = hand.get(0);

        return t;
    }
    /**
     * Picks the initial Tile based on a give Board and Hand
     * This Player picks the last Tile in the hand without rotation
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
    public Tile pickInitialTile(TestableBoard board, Hand hand) {

        Tile t = hand.get(hand.getNumTiles() - 1);

        return t;
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
    		point.translate(2, 0);
    	}
    	return new Position(point, 0);
    }
    /**
     * @return the name of this player
     */
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
