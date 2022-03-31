package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;



public class ErrorHandler {
	private Socket client;
	BufferedReader in;
	PrintWriter out;
    
    public ErrorHandler(Socket client, BufferedReader in, PrintWriter out) {  
    	this.client = client;
    	this.in = in;
    	this.out = out;
    }
    


	public  void handle(String error) {
		out.println(error+"**Continuer? y/n");
		String response = null;
		try {
			response = in.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(response.equals("n"))
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
