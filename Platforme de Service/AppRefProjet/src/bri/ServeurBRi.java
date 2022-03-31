package bri;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private Class<? extends Runnable> classService;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(int port, Class<? extends Runnable> classService) {//class in constructor
		try {
			listen_socket = new ServerSocket(port);
			this.classService = classService;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		try {
			while(true) {				
				new Thread(classService.getDeclaredConstructor(Socket.class).newInstance(listen_socket.accept())).start();	
			}
		}
		catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	 // restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}

	// lancement du serveur
	public void lancer() {
		(new Thread(this)).start();		
	}
}
