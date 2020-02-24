package game;
import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {

	/**
	 * List of tiles that constitute the hand
	 */
	ArrayList<Tile> tiles = new ArrayList<>();;
	/**
	 * Maximum size of the hand
	 */
	public static int HAND_SIZE = 3;
	
	public Hand() {
		tiles = new ArrayList<Tile>();
		for (int i = 0; i < HAND_SIZE; i++) {
			tiles.add(Tile.getRandomTile());
		}
	}
	
	/**
	 * Gets a tile by it's index in the Hand
	 * @param index
	 * @return the Tile at index
	 */
	public Tile get(int index) {
		if(index < 0 || index >= tiles.size()) {
			throw new IllegalArgumentException("Index out of range");
		}
		return tiles.get(index);
	}
	
	/**
	 * Gets and removes the Tile at index
	 * @param index
	 * @return the Tile at index
	 */
	public Tile pop(int index) {
		if(index < 0 || index >= tiles.size() - 1) {
			throw new IllegalArgumentException("Index out of range");
		}
		Tile tile = tiles.get(index);
		tiles.remove(index);
		return tile;
	}
	
	/**
	 * Gets all the Tiles in this Hand
	 * @return all the Tiles in this Hand
	 */
	public ArrayList<Tile> getAllTiles() {
		return this.tiles;
	}
	
	/**
	 * Removes the first Tile in this Hand that matches tile
	 * @param tile
	 */
	public void remove(Tile tile) {
		for (int i = 0; i < HAND_SIZE; i++) {
			if (tiles.get(i).matches(tile)) {
				pop(i);
				return;
			}
		}
	}
	
	/**
	 * Adds random Tiles to this Hand until the Hand size limit is reached
	 */
	public void refill() {
		while(tiles.size() < HAND_SIZE) {
			tiles.add(Tile.getRandomTile());
		}
	}
	
	/**
	 * Determines whether this Hand has a Tile that matches other
	 * @param other
	 * @return whether this Hand has a Tile that matches other
	 */
	public boolean hasMatchingTile(Tile other) {
		for (Tile tile : tiles) {
			if (tile.matches(other)) {
				return true;
			}
		}
		return false;
	}
	
	public int getHandSize() {
		return this.HAND_SIZE;
	}
}
