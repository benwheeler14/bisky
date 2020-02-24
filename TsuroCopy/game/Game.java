package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import players.Player;
import view.*;

public class Game {
	
	public static final int NUM_PLAYERS = 5;
	//public static final ArrayList<String> PLAYER_NAMES = new ArrayList<String>(Arrays.asList("white", "black", "red", "green", "blue"));
	
	private ArrayList<PlayerData> players;
	private int numPlayers;
	private Board board;
	private Podium podium;
	private boolean firstTurn;
	private int activePlayerIndex;
	
	public Game(ArrayList<Player> agents) {
		assert agents.size() >= 3;
		numPlayers = agents.size();
		players = new ArrayList<PlayerData>();
		firstTurn = true;
		for (int i = 0; i < agents.size(); i++) {
			PlayerData data = new PlayerData();
			data.agent = agents.get(i);
			data.name = agents.get(i).getName();
			data.alive = true;
			data.hand = new Hand();
			this.players.add(data);
		}
		board = new Board(this.getPlayerNames());
		this.podium = new Podium();
	}
	
	/**
	 * 
	 * @return false if game is over, true otherwise
	 */
	public boolean playNextTurn() {
		if (this.isGameOver()) {
			return false;
		}
		PlayerData nextPlayer;
		while (true) {
			nextPlayer = this.getNextActivePlayer();
			if (nextPlayer.alive) {
				break;
			}
		}
		if (this.firstTurn) {
			this.playFirstTurnForPlayer(nextPlayer);
		}
		else {
			this.playTurnForPlayer(nextPlayer);
		}
		if (this.isGameOver()) {
			return false;
		}
		return true;
	}
	
	/**
	 * returns a pointer to the PlayerData of the player whose turn it is next, providing that player is alive
	 * @return
	 */
	public PlayerData getNextActivePlayer() {
		PlayerData nextPlayer = this.getLivingPlayers().get(this.activePlayerIndex);
		this.activePlayerIndex++;
		if (this.activePlayerIndex == this.getLivingPlayers().size()) {
			this.activePlayerIndex = 0;
			this.firstTurn = false;
		}
		return nextPlayer;
		
	}
	
	public Podium run() {
		playFirstTurn();
		while(playTurn()) {}
		return podium;
	}
	
	public void playFirstTurn() {
		for (PlayerData player : this.getLivingPlayers()) {
			this.playFirstTurnForPlayer(player);
		}
	}
	
	public void playFirstTurnForPlayer(PlayerData player) {
		player.hand.refill();
		Position startingPosition = player.agent.pickInitialPlacement(board.getTestableBoard());
		Tile firstTile = player.agent.pickInitialTile(board.getTestableBoard(), player.hand);
		//check if the move is valid
		if (RuleChecker.validFirstTilePlacement(board.getTestableBoard(), player.name, firstTile, player.hand, startingPosition)) {
			//play the chosen move
			board.placeFirstTileForPlayer(firstTile.clone(), player.name, startingPosition);
		}
		//else, disqualify the player
		else {
			player.alive = false;
			this.podium.addDisqualified(player.name);
		}
	}

	/**return true if there are still turns left to play
	 * 
	 * @return whether or not there are turns left to play
	 */
	public boolean playTurn() {
		//if one person left, game is over, add them to the podium and return
		if (this.onePlayerLeft()) {
			PlayerData lastPlayer = this.getLivingPlayers().get(0);
			lastPlayer.alive = false;
			podium.addLosses(new ArrayList(Arrays.asList(lastPlayer.name)));
			return false;
		}
		for (PlayerData player : this.players) {
			this.playTurnForPlayer(player);
		}
		return true;
	}
	
	public void playTurnForPlayer(PlayerData player) {
		if (this.onePlayerLeft()) {
			return;
		}
		if (player.alive) {
			Tile tile = player.agent.pickTile(board.getTestableBoard(), player.hand);
			// check if the move data is valid
			if (RuleChecker.validTilePlacement(board.getTestableBoard(), player.name, tile, player.hand)) {
				board.placeTileForPlayer(tile.clone(), player.name);
				board.updatePlayerTokens();
				this.checkForNewDead();
			}
			//else, disqualify the player
			else {
				player.alive = false;
				this.podium.addDisqualified(player.name);
			}
		}
	}
	
	public boolean isGameOver() {
		return this.getLivingPlayers().size() == 0;
	}
	
	public Podium getPodium() {
		return this.podium;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	private boolean onePlayerLeft() {
		return this.getLivingPlayers().size() == 1;
	}
	/**
	 * checks if any players who are not dead in this objects state are dead in the boards state
	 * any newly dead players are killed (in this objects state) and added to the podium
	 */
	private void checkForNewDead() {
		ArrayList<String> newDead = new ArrayList<String>();
		for (PlayerData player : this.getLivingPlayers()) {
			if (board.playerDead(player.name)) {
				newDead.add(player.name);
				player.alive = false;
				this.activePlayerIndex--;
			}
		}
		this.podium.addLosses(newDead);
	}
	
	private ArrayList<PlayerData> getLivingPlayers(){
		ArrayList<PlayerData> result = new ArrayList<PlayerData>();
		for (PlayerData player : this.players) {
			if (player.alive) {
				result.add(player);
			}
		}
		return result;
	}
	
	public ArrayList<String> getPlayerNames(){
		ArrayList<String> names = new ArrayList<String>();
		for (PlayerData player : this.players) {
			names.add(player.name);
		}
		return names;
	}
	
	private class PlayerData{
		public Player agent;
		String name;
		public Hand hand;
		public boolean alive;
	}
}