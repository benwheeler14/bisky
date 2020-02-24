package TCP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
 
 
public class Client1 {
	
	private static SocketAddress address;
	private static Socket socket;
	private static ObjectOutputStream out;
	
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 5555;
		
		address = new InetSocketAddress(ip, port);
		socket = new Socket();
		try {
			socket.connect(address);
			System.out.println("Connected!");
			out = new ObjectOutputStream(socket.getOutputStream());
 
			String msg = "Hello from client!";
			out.writeObject(msg);
			System.out.println("Message sent!");
 
			out.close();
			socket.close();
			System.out.println("Socket closed.");
		} catch (IOException e) {
			System.out.println("Error: "+e.getMessage());
		}
	}
 
}