package game;

import java.awt.Point;

public class RuleChecker {
	
	/**
	 * Determines whether the data specifying a first move on board is a valid
	 * The data is valid if:
	 * the chosen position is on the periphery
	 * the chosen position has no adjacent tiles
	 * the chosen tile matches one in the hand
	 * 
	 * @param board
	 * @param position
	 * @return whether the position on board is a valid one for the intial placement of a playertoken
	 */
	public static boolean validFirstTilePlacement(TestableBoard board, String playerName, Tile tile, Hand hand, Position position) {
		//check if the chosen position is on the periphery
		if (!position.onPeriphery()) {
			return false;
		}
		//check if the chosen point has any tiles adjacent to it
		for (Point p : position.getAdjacentPoints()) {
			if (board.hasTile(p)) {
				return false;
			}
		}
		//check if the tile exists the hand( prevents player agents from making up tiles)
		if (!hand.hasMatchingTile(tile)) {
			return false;
		}
		//check if the first move kills the player
		if (!doesFirstMoveKillPlayer(board, playerName, tile, position)) {
			return true;
		}
		//else:  check if there are any other moves that would let the player survive
		else {
			for (Tile t : hand.getAllTiles()) {
				for (int i = 0; i < 4; i++) {
					t.rotate(1);
					if (!doesFirstMoveKillPlayer(board, playerName, t, position)) {
						return false;
					}
				}
			}
			return true;
		}
	}
	/**
	 * Determines whether the specified tile selection is valid for a player
	 * A Tile selection is valid if it matches one in the Hand
	 * and it does not kill the player
	 * unless the hand has no tile selections that wouldn't kill the player
	 * @param board
	 * @param playerName
	 * @param tile
	 * @param hand
	 * @return
	 */
	public static boolean validTilePlacement(TestableBoard board, String playerName, Tile tile, Hand hand) {
		//check if the tile exists the hand( prevents player agents from making up tiles)
		if (!hand.hasMatchingTile(tile)) {
			return false;
		}
		//check if the move kills the player
		if (!doesMoveKillPlayer(board, playerName, tile)) {
			return true;
		}
		//else:  check if there are any other moves that would let the player survive
		else {
			for (Tile t : hand.getAllTiles()) {
				for (int i = 0; i < 4; i++) {
					t.rotate(1);
					if (!doesMoveKillPlayer(board, playerName, t)) {
						return false;
					}
				}
			}
			return true;
		}
		
	}
	
	/**
	 * Determines if the specified Tile selection kills the player on the Board
	 * @param board
	 * @param playerName
	 * @param tile
	 * @return
	 */
	private static boolean doesMoveKillPlayer(TestableBoard board, String playerName, Tile tile) {
		board.testTilePlacement(tile, playerName);
		boolean dead = board.playerDead(playerName);
		board.reverseLastMove();
		return dead;
	}
	/**
	 * Determines if the specified first Tile selection kills the player on the Board
	 * @param board
	 * @param playerName
	 * @param tile
	 * @param position
	 * @return
	 */
	private static boolean doesFirstMoveKillPlayer(TestableBoard board, String playerName, Tile tile, Position position) {
		board.testFirstTilePlacement(tile, playerName, position);
		boolean dead = board.playerDead(playerName);
		board.reverseLastMove();
		return dead;
	}
}
