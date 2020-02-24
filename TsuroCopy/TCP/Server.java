package TCP;

import java.net.*;
import java.io.*;

public class Server {
	
	//constructor with port
	public Server(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket1 = server.accept();
			System.out.println("Client1 connected");
			Socket socket2 = server.accept();
			System.out.println("Client2 connected");
			DataInputStream din1 = new DataInputStream(socket1.getInputStream());
			DataOutputStream dout1 = new DataOutputStream(socket1.getOutputStream());
			
			DataInputStream din2 = new DataInputStream(socket2.getInputStream());
			DataOutputStream dout2 = new DataOutputStream(socket2.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			String str = "", str2 = "";
			while(!str.contentEquals("stop")) {
				str = din1.readUTF();
				dout1.writeUTF("Recieved: " + str);
				System.out.println("Client1 says: " + str);
				str2 = din2.readUTF();
				dout2.writeUTF("Recieved: " + str2);
				System.out.println("Client2 says: " + str2);
				dout1.flush();
				dout2.flush();
			}
			din1.close();
			din2.close();
			socket1.close();
			socket2.close();
			server.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	public static void main(String[] args) {
		Server server = new Server(8000);
	}
}
