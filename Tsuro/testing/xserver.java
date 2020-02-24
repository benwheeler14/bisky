package testing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.*;

import game.*;
import players.*;



public class xserver {
	
	private static final int SERVER_TIMEOUT = 30000;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedWriter writer = new BufferedWriter(new FileWriter("xserver.log", false));
		
		int port = 8000;
		if (args.length >= 2) {
			port = Integer.parseInt(args[1]);
		}
		ServerSocket ss = new ServerSocket(port);
		ss.setSoTimeout(SERVER_TIMEOUT);
		
		ArrayList<TCPPlayer> tcpPlayers = new ArrayList<TCPPlayer>();
		
		try {
			for (int i = 0; i < 5; i++) {
				Socket socket = ss.accept();
				writer.write("Client " + i + " Connected\n");
				TCPPlayer tcpPlayer = new TCPPlayer(socket, writer);
				tcpPlayers.add(tcpPlayer);
			}
		}
		catch (Exception e){
			//dont print
		}
		finally {
			ArrayList<Player> players = new ArrayList<Player>();
			players.addAll(tcpPlayers);
			for (int i = 0; i < 5 - tcpPlayers.size(); i++) {
				players.add(new Dumb("localPlayer" + i));
			}
			
			Game game = new Game(players);

			Podium podium = game.run();
			
			for (TCPPlayer tcpPlayer : tcpPlayers) {
				tcpPlayer.closeConnection();
			}
			
			writer.write(podium.toString());
			writer.close();
		}
	}

}
