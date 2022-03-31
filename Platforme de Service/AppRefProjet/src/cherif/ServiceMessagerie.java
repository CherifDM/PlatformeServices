package cherif;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bri.Service;





public class ServiceMessagerie implements Service{
	
	private static String ip = "localhost";
	private static int PORT;
	private final Socket client;
	BufferedReader in;
	PrintWriter out;
	
	public ServiceMessagerie(Socket socket) {
		client = socket;
		try {
			in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			out = new PrintWriter (client.getOutputStream ( ), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			//Socket maSocket = new Socket(ip, PORT);
			StringBuilder sb = new StringBuilder();
			String username;
			out.println("What is your username?"+" ** ");
			username = in.readLine();
	
			while(true) {
				sb.append("What would you like to do today?"+" ** ");
				sb.append("1 - See Your Messages"+" ** ");
				sb.append("2 - Write a Message"+" ** ");
				out.println(sb.toString());
				String answer = in.readLine();
				int choix = Integer.parseInt(answer);
				switch (choix) {
					case 1: {
						seeMessages(username);
						break;
					}
					case 2: {
						writeMessage(username);
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void writeMessage(String username) {
		
		try {
			out.println("Who are you sending this message to?");
			String receiver = in.readLine();
			out.println("Enter your message");
			String message = in.readLine();
			 MessageRegistry.send(username,receiver, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void seeMessages(String username) {
		String response = null;
		do {
			out.println(MessageRegistry.seeMessages(username)+ "** Enter Quit if you would like to quit **");
			try {
				response = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(!response.equals("Quit"));
	}
	
	public static String toStringue() {
		return "Messagerie";
	}
	
	
}
