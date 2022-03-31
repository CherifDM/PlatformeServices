package cherif;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


import bri.Service;

public class ServiceXML implements Service{
	    
	    
private final Socket client;
	
	public ServiceXML(Socket socket) {
		client = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			FTPClient client = new FTPClient();
			
			out.println("Entrer l'adresse de votre serveur (ex:'localhost')");
			String server = in.readLine();
			out.println("Entrer le numero de port de votre serveur (ex:'2121')");
			int port = Integer.parseInt(in.readLine());
			client.connect(server, port);
			
			out.println("Entrer le login de votre serveur (ex:'admin')");
			String login = in.readLine();
			out.println("Entrer le mot de passe de votre serveur (ex:'admin')");
			String mdp = in.readLine();
			boolean isSuccess = client.login(login, mdp);
			
			if(isSuccess) {
				out.println("Entrer le chemin vers votre fichier (ex:'/classes/text.xml')");
				String remoteFile = in.readLine();
				String DownloadFilePath = "C:\\Users\\alhas\\Desktop\\DUT Classes\\file.xml";
				File xmlDownload = new File(DownloadFilePath);
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(xmlDownload));
	            boolean success = client.retrieveFile(remoteFile, outputStream);
	            outputStream.close();
	            
	            out.println("Entrer votre adresse mail");
				String to = in.readLine();
				
	            String send = "We have analysed your xml file";

	            String from = "web@gmail.com";
	            String host = "localhost";
	            Properties properties = System.getProperties();
	            
	            
	            properties.setProperty("mail.smtp.host", host);
	            Session session = Session.getDefaultInstance(properties);
	            MimeMessage message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	            message.setSubject("XML File Analysis");
	            message.setText(send);
	            Transport.send(message);
	            System.out.println("Sent message successfully....");
	            
			}
			
		} catch (IOException e) {
			// TODO: handle exception
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}

}
