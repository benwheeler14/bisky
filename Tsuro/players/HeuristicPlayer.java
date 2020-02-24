package players;

import java.util.ArrayList;
import java.util.Random;

import game.*;

public abstract class HeuristicPlayer extends LegalPlayer {

	public HeuristicPlayer(String name) {
		super(name);
	}
	/**
	 * Chooses an optimal Tile based on this objects implementation of the getValue() method
	 * returns the first legal Tile returned by getAllLegalTiles that has the highest value
	 */
	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		ArrayList<Tile> legalTiles = this.getAllLegalTiles(board, hand);
		Tile bestTile = hand.get(0);
		int bestValue = -1;
		for (Tile tile : legalTiles) {
			board.testTilePlacement(tile, this.getName());
			int value = this.getValue(board);
			board.reverseLastMove();
			if (value > bestValue) {
				bestTile = tile;
				bestValue = value;
			}
		}
		return bestTile;
	}
	
	/**
	 * Gets a FirstMove using this objects getInitialPosition method to choose the position parameter
	 * and this objects getValue method to determine the best Tile out of all possible Tiles
	 */
	@Override
	public FirstMove pickFirstMove(TestableBoard board, Hand hand) {
		Position initialPosition = this.getInitialPosition(board);
		ArrayList<Tile> legalInitialTiles = this.getAllLegalInitialTiles(board, hand, initialPosition);
		Tile bestTile = hand.get(0);
		int bestValue = -1;
		for (Tile tile : legalInitialTiles) {
			board.testFirstTilePlacement(tile, this.getName(), initialPosition);
			int value = this.getValue(board);
			board.reverseLastMove();
			if (value > bestValue) {
				bestTile = tile;
				bestValue = value;
			}
		}
		FirstMove result = new FirstMove();
		result.position = initialPosition;
		result.tile = bestTile;
		return result;
	}
	/**
	 * Gets a random legal InitialPosition (Overridable)
	 * @param board
	 * @returna random legal InitialPosition
	 */
	protected Position getInitialPosition(TestableBoard board) {
		ArrayList<Position> legalPositions = this.getAllLegalInitialPlacements(board);
		return legalPositions.get((new Random().nextInt(legalPositions.size())));
	}
	/**
	 * A Heuristic for a board state
	 * Child classes should implement this method with their desired heuristic
	 * Returned value must be positive
	 * @param board
	 * @return a positive value representing how highly this objects values the given board state
	 */
	protected abstract int getValue(Board board);
}
