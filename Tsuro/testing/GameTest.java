package testing;
import java.util.ArrayList;
import java.util.Arrays;

import game.*;
import players.*;

public class GameTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("white", "blue", "black", "green", "orange"));
		ArrayList<Player> agents = new ArrayList<Player>();
		for (int i = 0; i < 5; i++) {
			agents.add(new CenterPlayer(playerNames.get(i)));
		}
		System.out.print(playerNames + "\n");
		Game game = new Game(agents);
		Podium podium = game.run();
		System.out.print(podium.toString());

	}

}
