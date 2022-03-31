package bri;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import appli.Programmer;
import exceptions.*;

public class ServiceProg implements Runnable {
	
	private Socket client;
	private Programmer user;
	private BufferedReader in;
	private PrintWriter out;
	private ErrorHandler errHandler;
	
	ServiceProg(Socket socket) {
		client = socket;
		try {
			in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			out = new PrintWriter (client.getOutputStream ( ), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		errHandler = new ErrorHandler(client, in, out);
	}
	
	
	@Override
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
		
			sb.append("Hello " +user.getUserName()+" ** ");
			while (true) {		
				sb = new StringBuilder();
				sb.append("What would you like to do today?"+" ** ");
				sb.append("1 - Upload new service"+" ** ");
				sb.append("2 - Update a service"+" ** ");
				sb.append("3 - Change ftp serveur adresse"+" ** ");
				sb.append("4 - Start a service "+" ** ");
				sb.append("5 - Stop a service "+" ** ");
				sb.append("6 - Uninstall a service"+" ** ");
				sb.append("7 - Upload Jar Package"+" ** ");
				sb.append("8 - Show status of services"+" ** ");
				out.println(sb.toString());
				
				 answer = in.readLine();
				 choix = Integer.parseInt(answer);
				
				switch (choix) {
					case 1: {
						uploadService();
						
						break;
					}
					case 2: {
						updateService();
						break;
					}
					case 3: {
						changeFTPAdresse();
						break;
					}
					case 4: {
						startService();
						break;
					}
					case 5: {
						stopService();
						break;
					}
					case 6: {
						uninstallService();
						break;
					}
					case 7: {
						loadJar();
						break;
					}
					case 8:{
						servicesStatus();
					}
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			errHandler.handle(e.getMessage());
		} catch (Exception e) {
			errHandler.handle("Error occured");
		}
												
	}
	
	private void servicesStatus() {
		out.println(user.showServices().replace("\n", "**")+"**"+"Enter when done seeing status **");
		try {
			in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void uploadService() throws ServiceNonConformeBRIExecption {
		
		URL ftpAdress = user.getFTPAddress();
		URLClassLoader urlcl = null;
		try {
			urlcl = new URLClassLoader(new URL[]{ftpAdress}) {};
			  
		String response;
		out.println("Enter the name of the class you would like to upload");
		response = in.readLine();
		Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(user.getUserName()+"."+response);
		user.addService(response, c);

		}  catch (MalformedURLException e) {
			out.println("FTP adress error");	
		} catch (IOException e) {
			System.out.println("Printing error");
		} catch (ClassNotFoundException e) {
			errHandler.handle("Cette classe n'a pas ete trouve a partir de l'adresse :" + ftpAdress);
		} ;
	}
	
	public void updateService() throws ServiceNonConformeBRIExecption, ServiceNonExistante {
		URL ftpAdress = user.getFTPAddress();
		URLClassLoader urlcl = null;
		try {
			urlcl = new URLClassLoader(new URL[]{ftpAdress}) {};
		String response;
		out.println("Enter the name of the class you would like to update**" + user.showServices().replace("\n", "**"));
		response = in.readLine();
		user.replaceService(response, (Class<? extends Service>) urlcl.loadClass(response));
		
		}  catch (ClassNotFoundException e) {
			
			errHandler.handle("Cette classe n'a pas ete trouve a partir de l'adresse :" + ftpAdress);
		} catch (MalformedURLException e) {
			out.println("FTP adress error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	/**
	 * NEEDS TO BE ABLE TO SEND CLASS TO GARBAGE COLLECTOR
	 * @throws ServiceNonExistante
	 */
	public void uninstallService() throws ServiceNonExistante {
		
		try {
			String response;
			out.println("Enter the name of the class you would like to uninstall**" + user.showServices().replace("\n", "**"));
			response = in.readLine();
			user.uninstallService(response);		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	
	public void  changeFTPAdresse() {
		try {
			String response;
			out.println("Enter your new FTP address");
			response = in.readLine();
			user.SetFTPAddress(new URL(response));
			
		} catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		} 
	}
	
	public void startService() throws ServiceNonStopable {
		try {
			String response;
			out.println("Enter the name of the class you would like to start**" + user.showStoppedServices().replace("\n", "**"));
			response = in.readLine();
			user.startService(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	
	
	public void stopService() throws ServiceNonStartable {
		try {
			String response;
			out.println("Enter the name of the class you would like to stop**" + user.showAvailabeServices().replace("\n", "**"));
			response = in.readLine();
			user.stopService(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
	}
	/**
	 * Charge un package de classes. Il doit y avoir excatement une classe de type service qui sera a preciser
	 * NON-UTILISE 
	 * @throws ServiceNonConformeBRIExecption
	 */
	public void loadPackage() throws ServiceNonConformeBRIExecption {
		URL ftpAdress = user.getFTPAddress();
		URLClassLoader urlcl = null;
		try {
			urlcl = new URLClassLoader(new URL[]{ftpAdress}) {};
		
			  
		String response;
		out.println("Enter the name of the class you would like to upload");
		response = in.readLine();
		Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(response);
		user.addService(response, c);
		
		
		out.println("Enter the names of the complementary classes you would like to upload");
		response = in.readLine();
		do {
			urlcl.loadClass(response);
			out.print(response + "Has been loaded");
			response = in.readLine();
		} while(response.equals("Done"));
		
		
		}  catch (MalformedURLException e) {
			out.println("FTP adress error");
			
		} catch (IOException e) {
			System.out.println("Printing error");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
		
		
	}
	/**
	 * Charge un groupement de class a partir d'un fichier .jar. Doit contenir au moins une classe qui implemente Service
	 * @throws Exception
	 */
	public void loadJar() throws Exception {
		 FTPClient client = new FTPClient();
		  //FileOutputStream fos = null; 
		 String response;
		  String server = user.getServer();
	      int port = user.getServerPort();
		  try {			 
			client.connect(server, port);
			boolean isSuccess = client.login(user.getServerLogin(), user.getServerPassword());
			if(isSuccess) {
				client.enterLocalPassiveMode();
				client.setFileType(FTP.BINARY_FILE_TYPE);
				String JarFilePath = "C:\\Users\\alhas\\Desktop\\DUT Classes\\file.jar";
				
				out.println("Enter the filepath of your jar");
				response = in.readLine();
				
				String remoteFile = response;
	           File jarDownload = new File(JarFilePath);
	           OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(jarDownload));
	           boolean success = client.retrieveFile(remoteFile, outputStream);
	           outputStream.close();
	           if(success) {
		           JarFile jarFile = new JarFile(JarFilePath);
		           Enumeration<JarEntry> e = jarFile.entries();
		           URL[] urls = { new URL("jar:file:" + JarFilePath+"!/") };
		           URLClassLoader cl = URLClassLoader.newInstance(urls);
		           
		           int nbServices = 0;
		           String serviceName = null;
		           Class<? extends Service> service = null;
		           
		           while (e.hasMoreElements()) {
		               JarEntry je = e.nextElement();
		               if(je.isDirectory() || !je.getName().endsWith(".class")){
		                   continue;
		               }
		               // -6 because of .class
		               String className = je.getName().substring(0,je.getName().length()-6);
		               className = className.replace('/', '.');
		               Class c = cl.loadClass(className);
		               
		               if(UserServiceRegistry.conformeBRI(c)) {
		            	   nbServices++;
		            	   service = c;	 
		            	   serviceName = className.split("\\.")[1];
		               }
		            	   
		           }
		           if(nbServices==1) {
		        	   user.addService(serviceName, service);
		           }
	           } else {
	        	   throw new ServiceNonExistante();
	           }
			} else {
				throw new Exception();
			}
			
           		
		  } catch (IOException e) {			 			 
			  e.printStackTrace();
		  } catch (ClassNotFoundException e1) {
			errHandler.handle("Class not found");
		} 	 
		    					 
	}
	private Programmer login() throws IOException {
		String username;
		String password;
		do {
			 out.println("What is your username?"+" ** ");
			 username = in.readLine();
			 out.println("What is your password?"+" ** ");
			 password = in.readLine();	
		} while (!UserRegistry.VerifyLogin(username, password));
		return  UserRegistry.getProgrammer(username);
	}
	
	private Programmer register() throws IOException {
		String username = null;
		Boolean valid = true;
		do {
			try {
				out.println("What is your username?"+" ** ");
				 username = in.readLine();
				 out.println("What is your password?"+" ** ");
				 String password = in.readLine();	
				 out.println("What is your FTPAdress URL?"+" ** ");
				 URL FTPAdress = new URL(in.readLine());
				 out.println("What is your server adress?"+" ** ");
				 String server = in.readLine();	
				 out.println("What is your server port number?"+" ** ");
				 int serverPort = Integer.parseInt(in.readLine());
				 out.println("What is your server login username?"+" ** ");
				 String serverLogin = in.readLine();	
				 out.println("What is your server login password?"+" ** ");
				 String serverPassword = in.readLine();	
				 UserRegistry.addProgrammer(new Programmer(username, password, FTPAdress, server, serverPort, serverLogin, serverPassword));
				 valid = true;
			}catch (Exception e) {
				valid = false;
			}
		} while(!valid);
		 return UserRegistry.getProgrammer(username);
	}
	


}
