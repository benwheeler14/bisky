package game;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Board implements Serializable {
	
	public static final int BOARD_SIZE = 10;
	/**
	 * Matrix of tiles representing the board.  
	 * Indices containing null represent coordinates without Tiles
	 */
	private ArrayList<ArrayList<Tile>> tiles;
	/**
	 * Mapping of names to Position representing the placements of named player tokens
	 */
	private HashMap<String, Position> playerTokens; //hashmap of player names with their location on the board
	
	
	public Board(ArrayList<String> playerNames) {
		playerTokens = new HashMap<String, Position>();
		tiles = new ArrayList<ArrayList<Tile>>();
		for (int i = 0; i < BOARD_SIZE; i++) {  //add null to all indices of our tile matrix
			ArrayList<Tile> column = new ArrayList<Tile>();
			for (int j = 0; j < BOARD_SIZE; j++) {
				column.add(null);
			}
			tiles.add(column);
		}
	}
	
	//Update Methods
	/**
	 * Places the first Tile for a playerName
	 * adds tile to the board and updates the Position to startingPosition
	 * @param tile
	 * @param playerName
	 * @param startingPosition
	 */
	public void placeFirstTileForPlayer(Tile tile, String playerName, Position startingPosition) {
		this.addTile(tile, startingPosition);
		this.setPlayerPosition(playerName, startingPosition);
		this.movePlayerAcrossTile(playerName);
	}
	
	/**
	 * Plays tile for playerName
	 * adds tile to the Position adjacent to playerNames current Position
	 * @param tile
	 * @param playerName
	 */
	public void placeTileForPlayer(Tile tile, String playerName) {
		Position position = this.getPlayerPosition(playerName).getAdjacentPosition();
		this.addTile(tile, position);
	}
	
	/**
	 * Moves all players on the board as far as possible
	 * stopping when they can no longer be moved
	 */
	public void updatePlayerTokens() {
		for (String playerName : playerTokens.keySet()) {
			while (this.playerAbleToMove(playerName)) {
				this.movePlayerToken(playerName);
			}
		}
	}
	
	//Question Answering Methods
	/**
	 * @param position
	 * @return whether a Tile exists at position
	 */
	public boolean hasTile(Position position) {
		return this.getTile(position) != null;
	}
	
	/**
	 * @param point
	 * @return whether a Tile exists at point
	 */
	public boolean hasTile(Point point) {
		return this.getTile(point) != null;
	}
	
	/**
	 * @param position
	 * @return the Tile at position
	 */
	public Tile getTile(Position position) {
		Point point = position.getPoint();
		return this.getTile(point);
	}
	
	/**
	 * @param point
	 * @return the Tile at point
	 */
	public Tile getTile(Point point) {
		if (!this.inBounds(point)) {
			return null;
		}
		return this.tiles.get(point.x).get(point.y);
	}
	
	/**
	 * @param name
	 * @return the Position of the player with the specified name
	 */
	public Position getPlayerPosition(String name) {
		return this.playerTokens.get(name);
	}
	
	/**
	 * Determines whether or not the specified player is dead
	 * cases for death are: being on the periphery of the board
	 * and being collided with another player
	 * @param playerName
	 * @return whether or not the player is dead
	 */
	public boolean playerDead(String playerName) {
		return this.playerCollided(playerName) || this.getPlayerPosition(playerName).onPeriphery();
	}
	
	/**
	 * @return whether this board has any living players left (false if game is over)
	 */
	public boolean hasLivingPlayers() {
		for (String key : this.playerTokens.keySet()) {
			if (!this.playerDead(key)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @return the matrix containing this Board's Tiles
	 */
	public ArrayList<ArrayList<Tile>> getTiles() {
		return this.tiles;
	}
	/**
	 * @return the HashMap of player Names to player Positions
	 */
	public HashMap<String, Position> getPlayerTokens() {
		return this.playerTokens;
	}
	
	public int getNumLivingPlayers() {
		int numLivingPlayers = 0;
		for (String name : this.playerTokens.keySet()) {
			if (!this.playerDead(name)) {
				numLivingPlayers += 1;
			}
		}
		return numLivingPlayers;
	}
	
	//Private Methods
	/**
	 * Adds tile on board at position
	 * @param tile
	 * @param position
	 */
	protected void addTile(Tile tile, Position position) {
		Point point = position.getPoint();
		this.tiles.get(point.x).set(point.y, tile);
	}
	
	/**
	 * Sets playerName's Position to position
	 * @param playerName
	 * @param position
	 */
	protected void setPlayerPosition(String playerName, Position position) {
		this.playerTokens.put(playerName, position);
	}
	
	/**
	 * Moves the specified player across the tile it is on (across the squiggly line)
	 * @param playerName
	 */
	private void movePlayerAcrossTile(String playerName) {
		Position position = this.getPlayerPosition(playerName);
		Tile tile = this.getTile(position);
		Position newPosition = new Position(position.getPoint(), tile.getConnectedPort(position.getPort()));
		this.setPlayerPosition(playerName, newPosition);
	}
	
	/**
	 * Moves the specified player from the Position it currently occupies to it's adjacent Position
	 * if that Position has a Tile
	 * On success returns true, else false
	 * @param playerName the name of the specified token
	 * @return whether or not the move took place
	 */
	private boolean movePlayerBetweenTiles(String playerName) {
		Position adjacentPosition = this.getPlayerPosition(playerName).getAdjacentPosition();
		if (!this.hasTile(adjacentPosition)) {
			return false;
		}
		this.setPlayerPosition(playerName, adjacentPosition);
		return true;
	}
	
	/**
	 * Moves the specified player between tiles, then across tiles
	 * if the moves between tiles fails, the move between tiles if skipped
	 * This rule enforces directionality on the journey of the player token across the board
	 * @param playerName
	 */
	private void movePlayerToken(String playerName) {
		if (this.movePlayerBetweenTiles(playerName)) {
			this.movePlayerAcrossTile(playerName);
		}
	}
	
	/**
	 * Determines if the specified player is collided with another player
	 * Two players are collided if their Positions are adjacent to each other
	 * @param playerName
	 * @return whether the specified player is collided with another
	 */
	private boolean playerCollided(String playerName) {
		Position adjacentPosition = this.getPlayerPosition(playerName).getAdjacentPosition();
		for (Map.Entry<String, Position> mapElement : playerTokens.entrySet()) { 
            if (mapElement.getValue().equals(adjacentPosition)) {
            	return true;
            }
		}
		return false;
	}	
	
	/**
	 * Determines whether the specified player is able to move
	 * Cases where a player can't move are:
	 * The player is dead
	 * The player's adjacent Position has no tile
	 * @param playerName
	 * @return whether the specified player is able to move
	 */
	private boolean playerAbleToMove(String playerName) {
		Position adjacentPosition = this.getPlayerPosition(playerName).getAdjacentPosition();
		return this.hasTile(adjacentPosition) && !this.playerDead(playerName);
	}
	
	/**
	 * Determines whether point is in the bounds of the Board
	 * @param point
	 * @return whether the point is in the bounds of the Board
	 */
	private boolean inBounds(Point point) {
		return (point.x >= 0 && point.x < BOARD_SIZE) && (point.y >= 0 && point.y < BOARD_SIZE);
	}
	
	/**
	 * creates a deep clone of this board
	 * @return a deep clone of this board
	 */
	@Override
	public Board clone() {
		Board copy = new Board(new ArrayList<String>(this.playerTokens.keySet()));
		for (String key : this.playerTokens.keySet()){  //clones all playerPositions
			Position pCopy = this.playerTokens.get(key).getCopy();
			copy.setPlayerPosition(key, pCopy);
		}
		for (int i = 0; i < BOARD_SIZE; i++) {  //clone's all Tile's
			for (int j = 0; j < BOARD_SIZE; j++) {
				Position position = new Position(new Point(i, j), 0);
				if (this.hasTile(position)) {
					copy.addTile(this.getTile(position).clone(), position);
				}
			}
		}
		return copy;
	}
	/**
	 * Creates a copy of this Board as a TestableBoard
	 * Member objects are deeply cloned
	 * @return a copy of this Board as a TestableBoard
	 */
	public TestableBoard getTestableBoard() {
		TestableBoard copy = new TestableBoard(new ArrayList<String>(this.playerTokens.keySet()));
		for (Entry<String, Position> entry : this.playerTokens.entrySet()){
			copy.setPlayerPosition(entry.getKey(), entry.getValue().getCopy());
		}
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Position position = new Position(new Point(i, j), 0);
				if (this.hasTile(position)) {
					copy.addTile(this.getTile(position).clone(), position);
				}
			}
		}
		return copy;
	}
}