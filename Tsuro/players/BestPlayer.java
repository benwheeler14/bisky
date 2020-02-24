package players;

import game.Board;

public class BestPlayer extends HeuristicPlayer {

	public BestPlayer(String name) {
		super(name);
	}

	@Override
	protected int getValue(Board board) {
		CutThroatPlayer cutThroat = new CutThroatPlayer(this.getName());
		CenterPlayer center = new CenterPlayer(this.getName());
		ClaustrophobicPlayer claustrophobic = new ClaustrophobicPlayer(this.getName());
		
		int value = cutThroat.getValue(board) * 100;
		value += center.getValue(board) * 10;
		value += claustrophobic.getValue(board);
		return value;
	}

}
