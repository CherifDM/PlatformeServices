package appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AppliClient {

	private static String ip = "localhost";
	private static int PORT;
	
	public static void main(String[] args) {
		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Entrer PORT");
			PORT = Integer.parseInt(sc.nextLine());
			
			try {
				Socket maSocket = new Socket(ip, PORT);
				
				PrintWriter socketOut = new PrintWriter(maSocket.getOutputStream(), true);
				BufferedReader socketIn = new BufferedReader (new InputStreamReader(maSocket.getInputStream()));
				
				System.out.println("Connection avec le serveur : "  + ip);
				
				String line;
				while (true) {
					
					line = socketIn.readLine();
					if(line == null || line == "\n") break;
					
					String[] str = line.split("##");
					line = "";
					for (int i = 0; i < str.length; i++) 
						line += str[i] + "\n";
					
					System.out.print(line);
					
					System.out.print("> ");
					
					socketOut.println(sc.nextLine());
				}
				
				maSocket.close();
				
			} catch (UnknownHostException e) {} catch (IOException e) {}
		}
	}

}
