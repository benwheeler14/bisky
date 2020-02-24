package TCP;

import java.net.*;
import java.awt.Point;
import java.io.*;

class ObjectServer {
	public static void main(String args[]) throws Exception {
		ServerSocket ss = new ServerSocket(3333);
		Socket s = ss.accept();
		ObjectOutputStream dout = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream din = new ObjectInputStream(s.getInputStream());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str = "", str2 = "";
		while (!str.equals("stop")) {
			str = (String) din.readObject();
			System.out.println("client says: " + str);
			Point point = (Point) din.readObject();
			System.out.println("Point recieved:  coordinates were " + point.x + ", " + point.y);
			
			
			str2 = br.readLine();
			dout.writeObject(str2);
			dout.flush();
		}
		din.close();
		s.close();
		ss.close();
	}
}