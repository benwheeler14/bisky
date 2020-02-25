package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class representing a Tile
 * Tile's store their own rotation
 * @author bhend
 *
 */
public class Tile implements Serializable {
	
	/**
	 * Two way mapping of connected ports on a tile
	 * Ports start at left topside with index 0, 
	 * then increase going clockwise up to 7 at top leftside
	 */
	HashMap<Integer, Integer> paths;  
	/**
	 * rotation is an integer between 0 and 3
	 */
	int rotation;  
	/**
	 * Creates a new instance of a tile
	 * @param edges each pair of ints in list (indexes 0 and 1, 2 and 3, etc) represents connected ports
	 * each int in the list must be within [0, 8), and each int must be unique
	 * list must also have size() 8
	 */
	public Tile(List<Integer> edges) {
		assert !validConstructorInput(edges);  //check input for validity

		paths = new HashMap<Integer, Integer>();
		for (int i = 0; i < 7; i = i + 2) {
			paths.put(edges.get(i), edges.get(i + 1));
			paths.put(edges.get(i + 1), edges.get(i));
		}
		rotation = 0;
	}
	/**
	 * Rotates the tile by 90 degrees * numTurns
	 * @param numTurns the number of times to clockwise turn the tile (negative specifies counterclockwise
	 */
	public void rotate(int numTurns) {
		rotation = Math.floorMod((rotation + numTurns), 4);
	}
	/**
	 * Gets the index of the port connected to the input port
	 * @param port index of the specified port within [0, 8)
	 * @return the port connected to the input port
	 */
	public int getConnectedPort(int port) {
		int givenPort = Math.floorMod((port - (rotation * 2)), 8);
		int resultPort = Math.floorMod((paths.get(givenPort) + (rotation * 2)), 8);
		return resultPort;
	}
	/**
	 * Gets a list of port indices in the same format that is used in the constructor
	 * each pair of indices in the list (0 and 1, 2 and 3, etc) represents two connected ports
	 * @return
	 */
	public ArrayList<Integer> getListOfPorts() {
		ArrayList<Integer> remainingPorts = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < 8; i++) {
			if (remainingPorts.contains(i)) {
				int j = getConnectedPort(i);
				remainingPorts.remove(Integer.valueOf(i));
				remainingPorts.remove(Integer.valueOf(j));
				result.add(i);
				result.add(j);
			}
		}
		return result;
	}
	/**
	 * Checks the constructor input for validity
	 * @param edges
	 * @return whether edges has length 8, and has no repeat integers, and contain integers only within [0, 8)
	 */
	private boolean validConstructorInput(List<Integer> edges) {
		if (edges.size() != 8) {
			return false;
		}
		Set<Integer> set = new HashSet<Integer>(edges);
		if (set.size() < edges.size()) {
			return false;
		}
		for (Integer e : edges) {
			if (e < 0 || e >= 8) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Determines if the input object is a tile that matches this one exactly (including rotation)
	 * @param o the object to check for equalness
	 * @return whether or not the object is equal to this one
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Tile)) {
			return false;
		}
		Tile tile = (Tile) o;
		for (int i = 0; i < 4; i++) {
			if (this.getConnectedPort(i) != tile.getConnectedPort(i)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Determines if the given tile matches this one (rotation doesn't matter)
	 * @param tile
	 * @return whether the given tile matches this one
	 */
	public boolean matches(Tile tile) {
		tile = tile.clone();
		for (int i = 0; i < 4; i++) {
			if (this.equals(tile)) {
				return true;
			}
			tile.rotate(1);
		}
		return false;
	}
	/**
	 * @return a deep clone of this Tile
	 */
	@Override
	public Tile clone() {
		Tile copy = new Tile(this.getListOfPorts());
		return copy;
	}
	
	@Override
	public String toString() {
		return "Tile: " + this.getListOfPorts();
	}
	
	/**
	 * @return a tile with randomly permuted port connections
	 */
	public static Tile getRandomTile() {
		ArrayList<Integer> ports = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		Collections.shuffle(ports);
		return new Tile(ports);
	}
}
