package testing;

import java.util.ArrayList;
import java.util.Arrays;

import game.Game;
import players.*;
import view.Observer;

public class AllClaustrophicTest {

	public static void main(String[] args) {
		
		ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("white", "blue", "black", "green", "red"));
		ArrayList<Player> agents = new ArrayList<Player>();
		agents.add(new ClaustrophobicPlayer("white"));
		agents.add(new ClaustrophobicPlayer("blue"));
		agents.add(new ClaustrophobicPlayer("black"));
		agents.add(new ClaustrophobicPlayer("green"));
		agents.add(new ClaustrophobicPlayer("red"));
		Game game = new Game(agents);

        Observer view = new Observer(game);
        
        view.setVisible(true);

        view.refresh();

	}

}
