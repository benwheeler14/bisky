package players;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import game.*;

public class TCPClient {
	
	private String playerName;
	private Player agent;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public TCPClient(String ipAddress, int port, String playerName, String strategy) {
		try {
			System.out.println("Connecting to Server...");
			this.socket = new Socket(ipAddress, port);
			System.out.println("Connected to Server!");
			out = new ObjectOutputStream(this.socket.getOutputStream());
			in = new ObjectInputStream(this.socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		switch (strategy) {
		case "Dumb":
			this.agent = new Dumb(playerName);
			break;
		case "Second":
			this.agent = new Second(playerName);
			break;
		default:
			this.agent = new Dumb(playerName);
			break;
		}
		
		this.playerName = playerName;
	}

	public void run() {
		try {
			while (true) {
				String message = (String) this.in.readObject();
				System.out.println("Message received from Server: " + message);
				switch (message) {
				case "pickTile":
					this.pickTile();
					break;
				case "pickInitialTile":
					this.pickInitialTile();
					break;
				case "pickInitialPlacement":
					this.pickInitialPlacement();
					break;
				case "getName":
					this.getName();
					break;
				case "close":
					this.socket.close();
					return;
				}
			}
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void pickTile() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			Hand hand = (Hand) in.readObject();
			Tile tile = this.agent.pickTile(board, hand);
			out.writeObject(tile);
			out.flush();
			System.out.println("Sent tile");
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void pickInitialTile() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			Hand hand = (Hand) in.readObject();
			Tile tile = this.agent.pickInitialTile(board, hand);
			out.writeObject(tile);
			out.flush();
			System.out.println("Sent initial tile");
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void pickInitialPlacement() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			Position position = this.agent.pickInitialPlacement(board);
			out.writeObject(position);
			out.flush();
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}

	private void getName() {
		try {
			out.writeObject(this.playerName);
			out.flush();
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		String ipAddress = args[0];
		int port = Integer.parseInt(args[1]);
		String playerName = args[2];
		String strategy = args[3]; //strategy can be either 'Dumb' or 'Second'
		
		TCPClient client = new TCPClient(args[0], Integer.parseInt(args[1]), args[2], args[3]);
		client.run();

	}
	

}
