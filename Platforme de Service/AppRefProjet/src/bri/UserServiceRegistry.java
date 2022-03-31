package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import exceptions.ServiceNonConformeBRIExecption;
import exceptions.ServiceNonExistante;
import exceptions.ServiceNonStartable;
import exceptions.ServiceNonStopable;

public class UserServiceRegistry {
	
	private HashMap<String, Class<? extends Service>> startedServices;
	private HashMap<String, Class<? extends Service>> stoppedServices;
	
	public UserServiceRegistry() {
		startedServices = new HashMap<String, Class<? extends Service>>();
		stoppedServices = new HashMap<String, Class<? extends Service>>();
	}
	
	public Class<? extends Service> getService(String nomService) {
		return (startedServices.get(nomService) != null) ? startedServices.get(nomService) : stoppedServices.get(nomService) ;
	}
	public void replaceService(String nomService,Class<? extends Service> service) throws ServiceNonConformeBRIExecption, ServiceNonExistante {
		if(conformeBRI(service)) {
			getCorrespondingList(nomService).replace(nomService, service);
		} else {
			throw new ServiceNonConformeBRIExecption();
		}
		
		
	}
	/**
	 * 
	 * @param service
	 * @return
	 */
	public static boolean conformeBRI(Class<? extends Service> service) {
		Boolean conforme = true;
		try {
			// implémenter l'interface BRi.Service
			List<Class<?>> inter = Arrays.asList(service.getInterfaces());
			if(!inter.contains(Service.class)) {
				return false;
			}
			
			// ne pas être abstract
			if(Modifier.isAbstract(service.getModifiers())) {
				return false;
			}
			
			// être publique
			if(!Modifier.isPublic(service.getModifiers())) {
				return false;
			}
			
			// avoir un attribut Socket private final
			Field[] fields = service.getDeclaredFields();
			for(Field field : fields) {
				if(field.getType()==Socket.class && Modifier.isPrivate(field.getModifiers())) {
					conforme = true;
				}
			}
						
			// avoir un constructeur public (Socket) sans exception
			Constructor<? extends Service> constr = service.getConstructor(Socket.class);			
			if(constr.getExceptionTypes().length>0) {
				return false;
			}
			
			// avoir une méthode public static String toStringue() sans exception		
			Method toStringue = service.getDeclaredMethod("toStringue", null);			
			if(toStringue.getExceptionTypes().length>0) {
				return false;
			}
			
		} catch (NoSuchMethodException e) {
			return false;
		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return conforme;
	}

	public void uninstallService(String nomService) throws ServiceNonExistante {
		getCorrespondingList(nomService).remove(nomService);	
	}
	
	public void addService(String nomService, Class<? extends Service> c) throws ServiceNonConformeBRIExecption {
		if(conformeBRI(c)) {
			startedServices.put(nomService, c);
		} else {
			throw new ServiceNonConformeBRIExecption();
		}		
	}
	
	public void stopService(String nomService) throws ServiceNonStartable {
		if(startedServices.get(nomService) != null) {
			stoppedServices.put(nomService, startedServices.get(nomService));
			 startedServices.remove(nomService);
		} else {
			throw new ServiceNonStartable();
		}
	}
	
	public void startService(String nomService) throws ServiceNonStopable {
		if(stoppedServices.get(nomService) != null) {
			startedServices.put(nomService, startedServices.get(nomService));
			stoppedServices.remove(nomService);
		} else {
			throw new ServiceNonStopable();
		}
	}
	
	public String showServices() {
		StringBuilder sb =new StringBuilder();		
		sb.append("Running Services" + "\n");
		sb.append(showAvailabeServices());
		sb.append("Stopped Services" + "\n");
		sb.append(showStoppedServices());
		return sb.toString();
	}
	
	public String showAvailabeServices() {
		StringBuilder sb =new StringBuilder();
		for(Entry<String, Class<? extends Service>> entre : startedServices.entrySet()) {
			sb.append(entre.getKey() +"\n");
		}
		return sb.toString();
	}
	
	public String showStoppedServices() {
		StringBuilder sb =new StringBuilder();
		for(Entry<String, Class<? extends Service>> entre : stoppedServices.entrySet()) {
			sb.append(entre.getKey() + "\n");
			
		}
		return sb.toString();
	}
	
	 public HashMap<String, Class<? extends Service>> getCorrespondingList(String nomService) throws ServiceNonExistante {
			if(startedServices.get(nomService) != null) {
				return startedServices;
			} else if(startedServices.get(nomService) != null){
				return stoppedServices;
			} else {
				throw new ServiceNonExistante();
			}
		 
	 }
	public Class<? extends Service> getAvailableService(String response) {
		return startedServices.get(response);
	}

}
