package players;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import game.*;

/**
 * Class for a remote client to play the Tsuro game
 * This object wraps a Player object, and uses that object to answer requests from the server
 */
public class TCPClient {
	
	private String playerName;
	private Player agent;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public TCPClient(String ipAddress, int port, String playerName, String strategy) {
		try {  //connect to server and initialize in and out streams
			System.out.println("Connecting to Server...");
			this.socket = new Socket(ipAddress, port);
			System.out.println("Connected to Server!");
			out = new ObjectOutputStream(this.socket.getOutputStream());
			in = new ObjectInputStream(this.socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		switch (strategy) {  //Initializes it's strategy based on the input string
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
	/**
	 * Runs this client
	 * Waits for four messages:
	 * "pickTile"
	 * "pickInitialTile"
	 * "pickInitialPlacement"
	 * "getName"
	 * "close"
	 * uses this objects player object to answer all requests except for close
	 */
	public void run() {
		try {
			while (true) {
				String message = (String) this.in.readObject();
				System.out.println("Message received from Server: " + message);
				switch (message) {
				case "pickTile":
					this.pickTile();
					break;
				case "pickFirstMove":
					System.out.print("received first move request \n");
					this.pickFirstMove();
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
	/**
	 * Reads a Board and Hand from this objects connection,
	 * Then generates a Tile using this.agent and sends it to the server
	 */
	private void pickTile() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			Hand hand = (Hand) in.readObject();
			//hand.pop(2);   //work around for the hand corruption bug
			System.out.print("Received Hand:\n" + hand.toString());
			Tile tile = this.agent.pickTile(board, hand);
			out.writeObject(tile);
			out.flush();
			System.out.println("Sent tile:\n" + tile.toString() + "\n");
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Reads a Board and Hand from this objects connection
	 * Then generates the appropriate Tile using this.agent and sends it the server
	 */
	/**private void pickInitialTile() {
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
	}*/
	/**
	 * Reads a Board from this objects connection
	 * Then uses this.agent to generate the appropriate Position and sends it to the server
	 */
	/**private void pickInitialPlacement() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			this.firstMove = this.agent.pickFirstMove(board, hand)
			Position position = this.agent.pickInitialPlacement(board);
			out.writeObject(position);
			out.flush();
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}*/
	/**
	 * Reads a Board and Hand from this objects connection
	 * Then generates the appropriate firstMove using this.agent and sends it the server
	 */
	private void pickFirstMove() {
		try {
			TestableBoard board = (TestableBoard) in.readObject();
			Hand hand = (Hand) in.readObject();
			FirstMove firstMove = this.agent.pickFirstMove(board, hand);
			out.writeObject(firstMove);
			out.flush();
			System.out.println("Sent firstMove\n");
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * Sends this objects name String to the server
	 */
	private void getName() {
		try {
			out.writeObject(this.playerName);
			out.flush();
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	/**
	 * launches an instance of this object
	 * @param args [String ipAddress, int Port, String playerName, String strategy]
	 */
	public static void main(String[] args) {
		String ipAddress = args[0];
		int port = Integer.parseInt(args[1]);
		String playerName = args[2];
		String strategy = args[3]; //strategy can be either 'Dumb' or 'Second'
		
		TCPClient client = new TCPClient(args[0], Integer.parseInt(args[1]), args[2], args[3]);
		client.run();

	}
	

}
