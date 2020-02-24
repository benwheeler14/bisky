package players;

import game.Hand;
import game.Position;
import game.TestableBoard;
import game.Tile;

public interface Player {

	/**
	 * Player Chooses which tile to play for intermediate moves.
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	public Tile pickTile(TestableBoard board, Hand hand);

	/**
	 * Player Chooses which tile to play for Initial moves.
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	public Tile pickInitialTile(TestableBoard board, Hand hand);

	/**
	 * Player Chooses which where to place the initial tile and token.
	 * @param board Current board State.
	 * @return Return the Port (Location and Port) of the tile and player token.
	 */
	public Position pickInitialPlacement(TestableBoard board);

	/**
	 * Getter for the player's name if needed.
	 * @return name of the player.
	 */
	public String getName();

	/**
	 * Getter for the Age of the Player. Age is determined by order of connection.
	 * @return the age of the player.
	 */
	public int getAge();

	/**
	 * Setter for the Age of the Player.
	 * @param age age of the player.
	 */
	public void setAge(int age);

}
