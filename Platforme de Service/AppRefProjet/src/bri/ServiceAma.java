package bri;


import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

import appli.Amateur;
import appli.IUser;


public class ServiceAma implements Runnable {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private IUser user;
	
	ServiceAma(Socket socket) {
		client = socket;
		try {
			in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			out = new PrintWriter (client.getOutputStream ( ), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			StringBuilder sb = new StringBuilder();
			String answer ;
			int choix ;
			do {
				sb.append("1 - Login"+" ** ");
				sb.append("2 - Register"+" ** ");
				out.println(sb.toString());
				answer = in.readLine();
				choix = Integer.parseInt(answer);
			} while(choix!=1 && choix!=2);

			if(choix==1) {
				user = login();
			} else {
				user = register();
			}

			Class<? extends Service> c = null;
			do {
				out.println(UserRegistry.toStringue().replace("\n", "**")+"**Entrer la service désiré :");
				String response = in.readLine();
				out.println("Entre l'autheur de la service desire :");
				String author = in.readLine();
				c = (Class<? extends Service>) UserRegistry.getAvailableServiceClass(author, response);
				if(c==null) {
					out.println("Invalid entry **Enter when ready");
					in.read();
				}
			} while(c==null);

			Service service = null;
			try {
				Constructor<? extends Service> constr = c.getConstructor(Socket.class);
				service = constr.newInstance(client);
				service.run();
				
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println("Class cannot be instantiated");
			} catch (NoSuchMethodException e) {
				System.out.println("Class does not have a valid constructor");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
			
		catch (IOException e) {
			//Fin du service
		}

		try {client.close();} catch (IOException e2) {}
	}
	private IUser login() throws IOException {
		String username;
		String password;
		do {
			 out.println("What is your username?"+" ** ");
			 username = in.readLine();
			 out.println("What is your password?"+" ** ");
			 password = in.readLine();	
		} while (!UserRegistry.VerifyLogin(username, password));
		return  UserRegistry.getUser(username);
	}
	
	private IUser register() throws IOException {
		String username = null;
		Boolean valid = true;
		do {
			try {
				out.println("What is your username?"+" ** ");
				 username = in.readLine();
				 if(UserRegistry.getUser(username)!=null){
					 throw new Exception();
				 }
				 out.println("What is your password?"+" ** ");
				 String password = in.readLine();	
				 UserRegistry.addAmateur(new Amateur(username, password));
				 valid = true;
			}catch (Exception e) {
				valid = false;
			}
		} while(!valid);
		 return UserRegistry.getUser(username);
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();		
	}

}
