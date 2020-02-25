package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import game.Game.PlayerData;
import players.Player;
import view.*;

public class Game {
	
	protected ArrayList<PlayerData> players;
	private Board board;
	protected Podium podium;
	private boolean firstTurn;  //whether or not the game is on the first round of turns
	private int nextPlayer, numPlayers;  //nextPlayer: the player whose turn it is next, numPlayers: the number of players in the Game
	
	public Game(ArrayList<Player> agents) {
		assert agents.size() >= 3 && agents.size() <= 5;  //make sure the game has the right number of players
		players = new ArrayList<PlayerData>();
		for (int i = 0; i < agents.size(); i++) {  //load the PlayerData with values
			PlayerData data = new PlayerData();
			data.agent = agents.get(i);
			data.name = agents.get(i).getName();
			data.alive = true;
			data.hand = new Hand();
			this.players.add(data);
		}
		board = new Board(this.getPlayerNames());
		this.podium = new Podium();
		this.firstTurn = true;
		this.nextPlayer = 0;
		this.numPlayers = this.players.size();
	}
	
	/**
	 * performs the next turn in a game of Tsuro
	 * By choosing the PlayerData object whose turn it is to go next and calling playFirstTurnForPlayer() or playTurnForPlayer() from it's parent class
	 */
	public void playNextTurn() {
		if (this.isGameOver()) { //if the game is over, just return
			return;
		}
		if (this.onePlayerLeft()) {  //if there is one player left then kill them and add them to the podium
			PlayerData lastPlayer = this.getLivingPlayers().get(0);
			lastPlayer.alive = false;
			this.podium.addLosses(new ArrayList(Arrays.asList(lastPlayer.name)));
			return;
		}
		
		if (firstTurn) {  //if is the first turn, call playFirstTurnForPlayer()
			this.playFirstTurnForPlayer(this.players.get(nextPlayer));
			nextPlayer++;
			if (nextPlayer >= numPlayers) {
				nextPlayer = 0;
				firstTurn = false;
			}
		}
		else {  //otherwise, call playTurn
			while(!this.players.get(nextPlayer).alive) {  //find the index of the next alive player
				nextPlayer = (nextPlayer + 1) % numPlayers;
			}
			this.playTurnForPlayer(this.players.get(nextPlayer));
			nextPlayer = (nextPlayer + 1) % numPlayers;
		}
	}
	
	/**
	 * Runs the whole game
	 * @return a Podium object containg the result of the game (ie: who came in 1st, 2nd, was disqualified, etc)
	 */
	public Podium run() {
		while(!this.isGameOver()) {
			this.playNextTurn();
		}
		return this.getPodium();
	}
	
	/**
	 * Plays the first turn of the game for the specificed player
	 * @param player, the PlayerData object for which to play the first turn
	 */
	public void playFirstTurnForPlayer(PlayerData player) {
		Logging.print("Playing First Turn for player: " + player.name);
		//player.hand.refill();
		Logging.print("Player: " + player.name + " has Hand: \n" + player.hand.toString());
		FirstMove firstMove = player.agent.pickFirstMove(board.getTestableBoard(), player.hand.clone());
		Position startingPosition = firstMove.position;
		//Position startingPosition = player.agent.pickInitialPlacement(board.getTestableBoard());
		Logging.print("Player: " + player.name + " has chosen Starting Position: " + startingPosition.toString());
		Tile firstTile = firstMove.tile;
		//Tile firstTile = player.agent.pickInitialTile(board.getTestableBoard(), player.hand);
		Logging.print("Player: " + player.name + " picked Tile: " + firstTile.toString());
		//check if the move is valid
		if (RuleChecker.validFirstTilePlacement(board.getTestableBoard(), player.name, firstTile, player.hand, startingPosition)) {
			//play the chosen move
			board.placeFirstTileForPlayer(firstTile.clone(), player.name, startingPosition);
			player.hand.remove(firstTile);
			Logging.print("Player: " + player.name + " has Hand: \n" + player.hand.toString() + "\n");
		}
		//else, disqualify the player
		else {
			player.alive = false;
			this.podium.addDisqualified(player.name);
			Logging.print("Player: " + player.name + " has been disqualified");
		}
	}
	
	/**
	 * Plays a turn for the specified player, updating the board state based on the players turn, and potentially killing off this or other players
	 * @param player, the PlayerData object for which to play the turn
	 */
	public void playTurnForPlayer(PlayerData player) {
		if (this.onePlayerLeft()) {  //if there is only one player left, return
			return;
		}
		if (player.alive) {  //don't play turns for players who are dead
			Logging.print("Playing turn for player: " + player.name);
			player.hand.refill();
			Logging.print("Player: " + player.name + " has Hand: \n" + player.hand.toString());
			Tile tile = player.agent.pickTile(board.getTestableBoard(), player.hand);  
			Logging.print("Player: " + player.name + " has Hand: \n" + player.hand.toString());
			Logging.print("Player: " + player.name + " picked Tile: " + tile.toString());
			if (RuleChecker.validTilePlacement(board.getTestableBoard(), player.name, tile, player.hand)) {  //make sure the chosen tile is a legal choice
				board.placeTileForPlayer(tile.clone(), player.name);
				board.updatePlayerTokens();
				this.checkForNewDead();
				player.hand.remove(tile);
				Logging.print("Player: " + player.name + " has Hand: \n" + player.hand.toString() + "\n");
			}
			else {  //if the chosen tile isn't valid, disqualify the player
				player.alive = false;
				this.podium.addDisqualified(player.name);
				Logging.print("Player: " + player.name + " has been disqualified");
			}
			
		}
	}
	
	/**
	 * @return this game's Podium object
	 */
	public Podium getPodium() {
		return this.podium;
	}
	/**
	 * @return this game's Board object
	 */
	public Board getBoard() {
		return this.board.clone();
	}
	
	/**
	 * @return whether there is only one player still alive
	 */
	protected boolean onePlayerLeft() {
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
				Logging.print("Player: " + player.name + " has died");
			}
		}
		this.podium.addLosses(newDead);
	}
	/**
	 * @return a list of all PlayerData for players still alive
	 */
	protected ArrayList<PlayerData> getLivingPlayers(){
		ArrayList<PlayerData> result = new ArrayList<PlayerData>();
		for (PlayerData player : this.players) {
			if (player.alive) {
				result.add(player);
			}
		}
		return result;
	}
	/**
	 * @return a list of all player names
	 */
	public ArrayList<String> getPlayerNames(){
		ArrayList<String> names = new ArrayList<String>();
		for (PlayerData player : this.players) {
			names.add(player.name);
		}
		return names;
	}
	/**
	 * @return gets a list of player names for all players that are still alive
	 */
	public ArrayList<String> getLivingPlayerNames(){
		ArrayList<String> names = new ArrayList<String>();
		for (PlayerData player : this.getLivingPlayers()) {
			names.add(player.name);
		}
		return names;
	}
	/**
	 * @return whether the game is over
	 */
	public boolean isGameOver() {
		return this.getLivingPlayers().size() == 0;
	}
	/**
	 * Structure for keeping track of all the data necessary to play the game for a single player
	 */
	protected class PlayerData{
		public Player agent;
		String name;
		public Hand hand;
		public boolean alive;
	}
}