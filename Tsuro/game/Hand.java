package game;
import java.io.Serializable;
import java.util.ArrayList;

public class Hand implements Serializable {

	/**
	 * List of tiles that constitute the hand
	 */
	ArrayList<Tile> tiles = new ArrayList<>();
	/**
	 * Maximum size of the hand
	 */
	public static int HAND_SIZE = 2;
	public static int INITIAL_HAND_SIZE = 3;
	
	public Hand() {
		tiles = new ArrayList<Tile>();
		for (int i = 0; i < INITIAL_HAND_SIZE; i++) { //loads the hand with random tiles
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
		if(index < 0 || index >= tiles.size()) {
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
		for (int i = 0; i < this.tiles.size(); i++) {
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
	/**
	 * @return the number of tiles in this hand
	 */
	public int getNumTiles() {
		return this.tiles.size();
	}
	/**
	 * Adds the specified Tile to the hand
	 * @param tile
	 */
	private void addTile(Tile tile) {
		this.tiles.add(tile);
	}

	@Override
	public Hand clone() {
		Hand copy = new Hand();
		while(copy.getNumTiles() != 0) {
			copy.pop(0);
		}
		for (Tile tile : this.getAllTiles()) {
			copy.addTile(tile.clone());
		}
		return copy;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (Tile tile : this.getAllTiles()) {
			s = s + tile.toString() + "\n";
		}
		return s.substring(0, s.length() - 1);
	}
}
