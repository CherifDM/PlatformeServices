package appli;

import java.net.URL;

import bri.Service;
import bri.UserServiceRegistry;
import exceptions.ServiceNonConformeBRIExecption;
import exceptions.ServiceNonExistante;
import exceptions.ServiceNonStartable;
import exceptions.ServiceNonStopable;

public class Programmer implements IUser {
	private String userName;
	private String password;
	private URL FTPAdress;
	private String server;
	private int serverPort;
	private String serverLogin;
	private String serverPassword;
	
	private UserServiceRegistry services;
	
	public Programmer(String username, String password, URL ftpAddress, String server, int serverPort, String serverLogin,String serverPassword) {
		this.userName = username;
		this.password = password;
		this.FTPAdress = ftpAddress;
		this.server = server;
		this.serverPort = serverPort;
		this.serverLogin = serverLogin;
		this.serverPassword = serverPassword;
		services = new UserServiceRegistry();
	}
	
	@Override
	public String getUserName() {return userName;}
	@Override
	public String getPassword() {return password;}
	@Override
	public void setUserName(String Username) {this.userName = Username;}
	@Override
	public void setPassword(String Password) {this.password = Password;}
	public URL getFTPAddress() {return FTPAdress;}
	public void SetFTPAddress( URL FTPAdress) { this.FTPAdress = FTPAdress;}
	public String getServer() { return server; }
	public int getServerPort() { return serverPort; }
	public String getServerLogin() { return serverLogin; }
	public String getServerPassword() { return serverPassword; }
	
	
	public Class<? extends Service> getService(String nomService) {
		return services.getService(nomService);
	}
	public void replaceService(String nomService,Class<? extends Service> service) throws ServiceNonConformeBRIExecption, ServiceNonExistante {
		services.replaceService(nomService, service);
	}
	public void uninstallService(String nomService) throws ServiceNonExistante {
		services.uninstallService(nomService);
	}
	public void addService(String nomService, Class<? extends Service> c) throws ServiceNonConformeBRIExecption {
		services.addService(nomService, c);
	}
	
	public void stopService(String nomService) throws ServiceNonStartable {
		services.stopService(nomService);
	}
	
	public void startService(String nomService) throws ServiceNonStopable {
		services.startService(nomService);
	}
	
	public String showServices() {
		return services.showServices();
	}
	
	public String showAvailabeServices() {
		return services.showAvailabeServices();
	}
	
	public String showStoppedServices() {
		return services.showStoppedServices();
	}

	public Class<? extends Service> getAvailableService(String response) {
		return services.getAvailableService(response);
	}
	
	

}
