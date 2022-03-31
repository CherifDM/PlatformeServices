package appli;

import java.net.MalformedURLException;
import java.net.URL;

import bri.ServeurBRi;
import bri.ServiceAma;
import bri.ServiceProg;
import bri.UserRegistry;

public class BRiLaunch {
	private static final int PORT_PROG = 3000;
	private static final int PORT_AMA = 5000;
	
	public static void main(String[] args) {
		
		//DONNEES
		try {
			UserRegistry.addProgrammer(new Programmer("cherif", "hhmmx", new URL("ftp://localhost:2121/classes"), "localhost", 2121, "admin", "admin"));
			UserRegistry.addProgrammer(new Programmer("matthieu", "zorbal", new URL("ftp://localhost:2121/classes"), "localhost", 2121, "admin", "admin"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		UserRegistry.addAmateur(new Amateur("iranja", "irk"));

		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		new Thread(new ServeurBRi(PORT_PROG, ServiceProg.class)).start();
		new Thread(new ServeurBRi(PORT_AMA, ServiceAma.class)).start();
		
	}
}
