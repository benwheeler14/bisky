package players;

import java.util.ArrayList;

import game.Board;
import game.Position;
import game.TestableBoard;

public class CutThroatPlayer extends HeuristicPlayer {

	public CutThroatPlayer(String name) {
		super(name);
	}

	/**
	 * Returns a higher value for for Boards with fewer players
	 * In other words, favors moves that kill other players
	 */
	@Override
	protected int getValue(Board board) {
		return 5 - board.getNumLivingPlayers();
	}
}