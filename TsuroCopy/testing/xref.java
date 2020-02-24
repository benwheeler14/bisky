package testing;
import view.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.*;
import players.Player;
import players.Second;


public class xref {

	public static void main(String[] args) {
		
		System.out.print("Please enter a valid JSON string");
		Scanner scanner = new Scanner(System.in);
		JSONParser parser = new JSONParser();
		JSONArray input = new JSONArray();
		try {
			input = (JSONArray) parser.parse(scanner.nextLine());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//throw new IllegalArgumentException("Invalid JSON input");
			e.printStackTrace();
		}
		ArrayList<Player> agents = new ArrayList<Player>();
		//ArrayList<String> playerNames = new ArrayList<String>();
		Iterator<String> iter = input.iterator();
		while (iter.hasNext()) {
			//playerNames.add(iter.next());
			agents.add(new Second(iter.next()));
		}
		
		Game game = new Game(agents);
		
		game.playFirstTurn();

		while (!game.isGameOver()) {
			game.playTurn();
		}
		
		Podium podium = game.getPodium();
		
		ArrayList<ArrayList<String>> placements = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < podium.getNumPlacements(); i++) {
			ArrayList<String> winners = podium.getPlacement(i);
			Collections.sort(winners);
			placements.add(winners);
		}
		
		JSONObject dict = new JSONObject();
		dict.put("winners", placements);
		ArrayList<String> disqualified = podium.getDisqualified();
		Collections.sort(disqualified);
		dict.put("losers", disqualified);
		
		System.out.print(dict.toJSONString());
		scanner.close();
		
	}
	
	

}
