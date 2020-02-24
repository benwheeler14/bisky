package testing;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.*;
import players.*;

public class xserver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int port = 8000;
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Waiting for client to connect...");
		Socket socket = ss.accept();
		System.out.println("Client connected!");
		TCPPlayer tcpPlayer = new TCPPlayer(socket);
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(tcpPlayer);
		players.add(new Dumb("ben"));
		players.add(new Dumb("damon"));
		players.add(new Dumb("zach"));
		players.add(new Dumb("nicole"));

		Game game = new Game(players);
		
		System.out.println("Playing First Turn");
		game.playFirstTurn();

		while (!game.isGameOver()) {
			game.playTurn();
		}

		Podium podium = game.getPodium();
		
		tcpPlayer.closeConnection();
		socket.close();
		
		System.out.print(podium.toString());
	}

}
