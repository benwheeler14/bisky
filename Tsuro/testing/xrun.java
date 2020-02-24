package testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import players.Player;
import players.Second;

public class xrun {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

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
		ArrayList<PlayerData> players = new ArrayList<PlayerData>();
		Iterator<JSONObject> iter = input.iterator();
		while (iter.hasNext()) {
			//playerNames.add(iter.next());
			JSONObject dict = iter.next();
			PlayerData data = new PlayerData();
			data.name = (String) dict.get("name");
			data.strategy = getStrategyString((String) dict.get("strategy"));
			players.add(data);
		}
		
		if (players.size() < 3 || players.size() > 5) {
			throw new IllegalArgumentException("Too many or too few players");
		}
		
		String[] servercmdarr = new String[1];
		servercmdarr[0] = "./xserver";
		Process process = Runtime.getRuntime().exec(servercmdarr);
		
		
		for (int i = 0; i < players.size(); i++) {
			String[] cmdarr = new String[5];
			cmdarr[0] = "./xclient";
			cmdarr[1] = "localhost";
			cmdarr[2] = "8000";
			cmdarr[3] = players.get(i).name;
			System.out.println(players.get(i).name);
			System.out.println(players.get(i).strategy);
			cmdarr[4] = players.get(i).strategy;
			Process process1 = Runtime.getRuntime().exec(cmdarr);
		}
	}

	private static String getStrategyString(String input) {
		if (input.equalsIgnoreCase("Dumb")) {
			return "Dumb";
		}
		if (input.equalsIgnoreCase("Second")) {
			return "Second";
		}
		throw new IllegalArgumentException("Invalid Strategy String: " + input);
	}
	
	private static class PlayerData {
		public String name;
		public String strategy;
	}
}
