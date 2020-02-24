package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
 
public class Server1 {
 
	private static ServerSocket serverSocket;
	private static Socket client;
	private static ObjectInputStream in;
 
	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(5555);
			System.out.println("Waiting for clients...");
			client = serverSocket.accept();
			System.out.println("Client Connected!");
			in = new ObjectInputStream(client.getInputStream());
			
			String msg = (String)in.readObject();
			System.out.println("\nMessage received: "+msg+"\n");
			
			in.close();
			client.close();
			System.out.println("Client closed.");
			
			serverSocket.close();
			System.out.println("Server closed.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error: "+e.getMessage());
		}	
	}
 
}
