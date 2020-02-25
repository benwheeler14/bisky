package players;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.*;

/**
 * Class for Player that uses a remote client to make it's decisions
 */
public class TCPPlayer implements Player {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private BufferedWriter writer;
	private String playerName = null;

	public TCPPlayer (Socket socket, BufferedWriter writer) {
		this.socket = socket;
		this.writer = writer;
		try {  //initialize the input and output streams
			this.out = new ObjectOutputStream(socket.getOutputStream());  //order of these two operations matter!!!!
			this.in = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e){
			System.out.println(e);
		}
		this.playerName = this.getName();
		
	}
	/**
     * Picks the next Tile to play based on a give Board and Hand
     * This Player asks a remote client which Tile to play
     * Starts message to client wit "pickTile", followed by the board and the hand
     * Then receives a Tile from the client and returns it
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		Tile tile = null;
		try {
			writer.write("Sending Pick Tile Request for " + this.getName() + "\n");
			out.writeObject("pickTile");
			out.writeObject(board.clone().getTestableBoard());
			Logging.print("Sending Hand:\n" + hand.toString());
			out.writeObject(hand);
			out.flush();
			tile = (Tile) in.readObject();
			writer.write("Tile recieved, ports are : " + tile.getListOfPorts() + " for " + this.getName() + "\n");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tile;
	}
	/**
     * Picks the initial Tile to play based on a give Board and Hand
     * This Player asks a remote client which Tile to play
     * Starts message to client wit "pickInitialTile", followed by the board and the hand
     * Then receives a Tile from the client and returns it
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return Return the Tile to place.
	 */
	//@Override
	public Tile pickInitialTile(TestableBoard board, Hand hand) {
		Tile tile = null;
		try {
			writer.write("Sending Pick Tile Request for " + this.getName() + "\n");
			out.writeObject("pickInitialTile");
			out.writeObject(board);
			out.writeObject(hand);
			out.flush();
			tile = (Tile) in.readObject();
			writer.write("Tile recieved, ports are : " + tile.getListOfPorts() + " for " + this.getName() + "\n");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tile;
	}
	/**
     * Picks the initial Position based on a give Board and Hand
     * This Player asks a remote client for a Position
     * Starts message to client wit "pickInitialPlacement", followed by the board
     * Then receives a Position from the client and returns it
	 * @param board Current board State.
	 * @param hand Tiles to choose from.
	 * @return the Position to place this players Token (and first Tile).
	 */
	//@Override
	public Position pickInitialPlacement(TestableBoard board) {
		Position position = null;
		try {
			writer.write("Sending Pick Initial Placement Request for " + this.getName() + "\n");
			out.writeObject("pickInitialPlacement");
			out.writeObject(board);
			out.flush();
			position = (Position) in.readObject();
			writer.write("Received Initial Placement for " + this.getName() + "\n");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return position;
	}
	/**
	 * Asks the remote client for it's name 
	 * @return the name of the player
	 */
	@Override
	public String getName() {
		if (this.playerName != null) {
			return this.playerName;
		}
		String name = null;
		
		try {
			writer.write("Sending Get Name request" + "\n");
			out.writeObject("getName");
			out.flush();
			name = (String) in.readObject();
			writer.write("received get name requst for " + name + "\n");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return name;
	}
	/**
	 * closes this objects connection
	 */
	public void closeConnection() {
		try {
			out.writeObject("close");
			out.flush();
			this.socket.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public FirstMove pickFirstMove(TestableBoard board, Hand hand) {
		FirstMove firstMove = null;
		try {
			writer.write("Sending Pick firstMove Request for " + this.getName() + "\n");
			out.writeObject("pickFirstMove");
			out.writeObject(board);
			out.writeObject(hand);
			out.flush();
			firstMove = (FirstMove) in.readObject();
			writer.write("FirstMove recieve, Tile is: " + firstMove.tile.getListOfPorts() + " and Position is: " + firstMove.position.toString() + " for " + this.getName() + "\n");
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return firstMove;
	}
}
