package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * A class representing the placements and disqualifications of players in a game of Tsuro
 */
public class Podium {
	
	/**
	 * Represents placecments of players during a game of Tsuro
	 * Each int in the map is associated with a list of players that lost at that placement
	 * 0 represents last place, numPlacements - 1 represents first place
	 */
	private HashMap<Integer, ArrayList<String>> placements;
	/**
	 * The number of placements (tiers of the podium)
	 */
	private int numPlacements;
	/**
	 * The players who lost due to qualification (not counted in the placements)
	 */
	ArrayList<String> disqualified;
	
	public Podium() {
		placements = new HashMap<Integer, ArrayList<String>>();
		numPlacements = 0;
		disqualified = new ArrayList<String>();
	};
	/**
	 * Adds the names of players who have lost to the datastructuce
	 * The players in playerNames represent players who have lost simultaneously
	 * should be called on each playername only once
	 * @param playerNames
	 */
	public void addLosses(ArrayList<String> playerNames) {
		if (playerNames.size() == 0) {
			return;
		}
		placements.put(numPlacements, playerNames);
		numPlacements++;
	}
	
	/**
	 * Adds a disqualified player to the datastructure
	 * @param playerName
	 */
	public void addDisqualified(String playerName) {
		this.disqualified.add(playerName);
	}
	
	/**
	 * Gets the list of playernames(could be a single player) that placed at that specific placement
	 * first place is denoted by 0, second by 1, etc (the reverse of how the data is stored)
	 * @param placement
	 * @return list of playernames at that placement
	 */
	public ArrayList<String> getPlacement(int placement){
		return placements.get((numPlacements - 1) - placement);
	}
	
	/**
	 * Gets the list of players who were disqualified
	 * @return the list of players that were disqualified
	 */
	public ArrayList<String> getDisqualified() {
		return disqualified;
	}
	/**
	 * @return a list of disqualified players as a String
	 */
	public String disqualifiedAsString() {
		String result = "";
		for (String dis : this.getDisqualified()) {
			result = result + dis + ", ";
		}
		return result;
	}
	/**
	 * @return a list of placements as a String
	 */
	public String placementsAsString() {
		String result = "";
		for (int i = 0; i < this.getNumPlacements(); i++) {
			result = result + (i + 1) + ", ";
			for (String name : this.getPlacement(i)) {
				result = result + name + ", ";
			}
			result = result + "\n";
		}
		return result;
	}
	/**
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		return "Winners:\n" + this.placementsAsString() + "\nDisqualified: " + this.disqualifiedAsString();
	}
	/**
	 * @return number of total placements (tiers on the podium)
	 */
	public int getNumPlacements() {
		return numPlacements;
	}
	
	public int getScore(String playerName) {
		if (this.getDisqualified().contains(playerName)) { //no points for being disqualified
			return 0;
		}
		int score = 1;
		for (int i = this.getNumPlacements() - 1; i >= 0; i--) {  //one point for last, increasing by 1 point towards 1st
			if (this.getPlacement(i).contains(playerName)) {
				return score;
			}
			score++;
		}
		return 0; //playerName was not found in the podium
	}

}
