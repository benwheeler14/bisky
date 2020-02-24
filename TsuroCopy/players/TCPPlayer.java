package players;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.Hand;
import game.Position;
import game.TestableBoard;
import game.Tile;

public class TCPPlayer implements Player {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	
	public TCPPlayer (Socket socket) {
		this.socket = socket;
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());  //order of these two operations matter!!!!
			this.in = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e){
			System.out.println(e);
		}
		
	}

	@Override
	public Tile pickTile(TestableBoard board, Hand hand) {
		Tile tile = null;
		try {
			System.out.println("Sending Pick Tile Request");
			out.writeObject("pickTile");
			out.writeObject(board);
			out.writeObject(hand);
			out.flush();
			tile = (Tile) in.readObject();
			System.out.println("Tile recieved, ports are : " + tile.getListOfPorts());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tile;
	}

	@Override
	public Tile pickInitialTile(TestableBoard board, Hand hand) {
		Tile tile = null;
		try {
			out.writeObject("pickInitialTile");
			out.writeObject(board);
			out.writeObject(hand);
			out.flush();
			tile = (Tile) in.readObject();
			System.out.println("Tile recieved, ports are : " + tile.getListOfPorts());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tile;
	}

	@Override
	public Position pickInitialPlacement(TestableBoard board) {
		Position position = null;
		try {
			out.writeObject("pickInitialPlacement");
			out.writeObject(board);
			out.flush();
			position = (Position) in.readObject();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return position;
	}

	@Override
	public String getName() {
		String name = null;
		try {
			out.writeObject("getName");
			out.flush();
			name = (String) in.readObject();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return name;
	}
	
	public void closeConnection() {
		try {
			out.writeObject("close");
			out.flush();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public int getAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAge(int age) {
		// TODO Auto-generated method stub

	}

}
