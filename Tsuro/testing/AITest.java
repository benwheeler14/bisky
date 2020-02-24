package testing;

import java.util.ArrayList;
import java.util.Arrays;

import game.Game;
import game.Logging;
import game.Podium;
import players.*;

public class AITest {

	public static void main(String[] args) {
		ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList("white", "blue", "black", "green", "red"));
		ArrayList<Player> agents = new ArrayList<Player>();
		agents.add(new BestPlayer("BestPlayer"));
		//agents.add(new Second("Second"));
		agents.add(new LegalPlayer("LegalPlayer"));
		agents.add(new CutThroatPlayer("CutThroatPlayer"));
		agents.add(new CenterPlayer("CenterPlayer"));
		agents.add(new ClaustrophobicPlayer("ClaustrophobicPlayer"));
		
		ArrayList<Integer> scores = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
		ArrayList<Integer> firstPlaces = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
		for (int i = 0; i < 1000; i++) {
			Game game = new Game(agents);
			Podium podium = game.run();
			for (int j = 0; j < agents.size(); j++) {
				int score = scores.get(j) + podium.getScore(agents.get(j).getName()); //add score of current game to overall score
				scores.set(j, score);
				if (podium.getPlacement(0).contains(agents.get(j).getName())) {
					int numFirstPlaces = firstPlaces.get(j) + 1;
					firstPlaces.set(j, numFirstPlaces);
				}
			}
		}
		
		for (int i = 0; i < agents.size(); i++) {
			System.out.println("Agent: " + agents.get(i).getName() + " Scored: " + scores.get(i) + " points");
		}
		System.out.println("");
		for (int i = 0; i < agents.size(); i++) {
			System.out.println("Agent: " + agents.get(i).getName() + " Had: " + firstPlaces.get(i) + " first placements");
		}
	}
}
