package testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import game.*;
import players.*;
import view.*;

public class AllCenterTest {

	
	
    public static void main(String[] args) throws IOException {
    	
    	ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("white", "blue", "black", "green", "red"));
		ArrayList<Player> agents = new ArrayList<Player>();
		agents.add(new CenterPlayer("white"));
		agents.add(new CenterPlayer("blue"));
		agents.add(new CenterPlayer("black"));
		agents.add(new CenterPlayer("green"));
		agents.add(new CenterPlayer("red"));
		Game game = new Game(agents);

        Observer view = new Observer(game);
        
        view.setVisible(true);

        view.refresh();
    }
}
