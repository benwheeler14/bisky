package testing;

import java.util.ArrayList;
import java.util.Arrays;

import game.Game;
import players.*;
import view.Observer;

public class MixedTest {

	public static void main(String[] args) {

		ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("white", "blue", "black", "green", "red"));
		ArrayList<Player> agents = new ArrayList<Player>();
		agents.add(new BestPlayer("BestPlayer"));
		agents.add(new LegalPlayer("LegalPlayer"));
		agents.add(new CutThroatPlayer("CutThroatPlayer"));
		agents.add(new CenterPlayer("CenterPlayer"));
		agents.add(new ClaustrophobicPlayer("ClaustrophobicPlayer"));
		Game game = new Game(agents);

        Observer view = new Observer(game);
        
        view.setVisible(true);

        view.refresh();
	}

}
