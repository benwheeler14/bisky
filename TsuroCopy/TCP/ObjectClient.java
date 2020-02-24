package TCP;

import java.net.*;
import java.awt.Point;
import java.io.*;

class ObjectClient {
	public static void main(String args[]) throws Exception {
		Socket s = new Socket("localhost", 3333);
		ObjectOutputStream dout = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream din = new ObjectInputStream(s.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String str = "", str2 = "";
		while (!str.equals("stop")) {
			str = br.readLine();
			System.out.println("Message sending " + str);
			dout.writeObject(str);
			System.out.println("Message sent " + str);
			Point point = new Point(420, 420);
			dout.writeObject(point);
			System.out.println("Point sent");
			dout.flush();
			
			
			str2 = (String) din.readObject();
			System.out.println("Server says: " + str2);
		}

		dout.close();
		s.close();
	}
}