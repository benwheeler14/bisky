package players;

import game.*;

public interface Player {

	/**
	 * Player Chooses which tile to play for intermediate moves.
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	public Tile pickTile(TestableBoard board, Hand hand);
	/**
	 * Player chooses an initial placement and an initial tile
	 * @param board Current board state
	 * @param hand Tiles to choose from
	 * @return the initial Position and initial Tile constituting a first move
	 */
	public FirstMove pickFirstMove(TestableBoard board, Hand hand);
	/**
	 * Getter for the player's name if needed.
	 * @return name of the player.
	 */
	public String getName();
}


