package TCP;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import game.*;

public class Client {
	
	// constructor to put ip address and port 
	public Client(String address, int port) {
		try {
			Socket socket = new Socket(address, port);
			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			String str = "", str2 = "";
			
			while(!str.equals("stop")) {
				str = br.readLine();
				dout.writeUTF(str);
				dout.flush();
				str2 = din.readUTF();
				System.out.println("Server says: " + str2);
			}
			
			dout.close();
		}
		catch (Exception e) {
			System.out.print(e);
		}
		
		
	}

	public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 8000); 
	}
}
