package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A sub class of Board for testing moves without permanently altering the state of the Board
 * @author bhend
 *
 */
public class TestableBoard extends Board implements Serializable {
	
	private HashMap<String, Position> previousPlayerTokens; //the Positions of the player tokens before the last specified move
	private Position lastPlacedTile; //the Position of the last placed Tile
	private boolean reversible; //whether this Board is reversible(ie: do was have the save data for it's previous state)
	
	public TestableBoard(ArrayList<String> playerNames) {
		super(playerNames);
		this.previousPlayerTokens = new HashMap<String, Position>();
		for (String name : playerNames) {
			this.previousPlayerTokens.put(name, null);
		}
		lastPlacedTile = null;
		reversible = false;
	}
	/**
	 * Plays a Tile on the Board for playerName, while storing the previous gamestate
	 * can be reversed by calling reverseLastMove()
	 * @param tile
	 * @param playerName
	 */
	public void testTilePlacement(Tile tile, String playerName) {
		this.storeCurrentState(this.getPlayerPosition(playerName).getAdjacentPosition());
		this.placeTileForPlayer(tile, playerName);
		this.updatePlayerTokens();
	}
	/**
	 * Plays a first Tile for playerName, while storing the previous gamestate
	 * can be reversed by calling reverseLastMove()
	 * @param tile
	 * @param playerName
	 * @param startingPosition
	 */
	public void testFirstTilePlacement(Tile tile, String playerName, Position startingPosition) {
		this.storeCurrentState(startingPosition);
		this.placeFirstTileForPlayer(tile, playerName, startingPosition);
	}
	/**
	 * Reverses the last move executed by either testTilePlacement or testFirstTilePlacement
	 * reverting all player positions and removing the last tile placed
	 * Cannot be called twice in a row without a call to testTilePlacement or testFirstTilePlacement in between
	 * Repeat calls do not alter the state of the Board
	 */
	public void reverseLastMove() {
		if (!reversible) {
			return;
		}
		reversible = false;
		for (String name : this.previousPlayerTokens.keySet()) {
			this.setPlayerPosition(name, this.previousPlayerTokens.get(name));
		}
		this.addTile(null, this.lastPlacedTile);
	}
	/**
	 * Stores the current state of the game:
	 * All current player positions
	 * The position of the new tile being placed
	 * @param newTilePosition
	 */
	private void storeCurrentState(Position newTilePosition) {
		for (String name : previousPlayerTokens.keySet()) {
			previousPlayerTokens.put(name, this.getPlayerPosition(name));
		}
		lastPlacedTile = newTilePosition;
		reversible = true;
	}
}
