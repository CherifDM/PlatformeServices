package bri;

import java.util.HashMap;
import java.util.Map.Entry;

import appli.Amateur;
import appli.IUser;
import appli.Programmer;

public class UserRegistry {
	
	private static HashMap<String, Programmer> programmers = new HashMap<String, Programmer>();
	private static HashMap<String, Amateur> amateurs = new HashMap<String, Amateur>();

	public static boolean VerifyLogin(String username, String password) {
		IUser user = getUser(username);
		if(user == null ) {
			return false;
		} else if(!user.getPassword().equals(password)) {
			return false;
		}
		return true;
	}

	public static Programmer getProgrammer(String username) {
		return programmers.get(username);
	}
	
	public static void addProgrammer(Programmer user) {
		programmers.put(user.getUserName(), user);
	}

	public static String toStringue() {
		//Make Hash map in ServiceRegistry with ServiceID , Service / Users keep ServiceID
		StringBuilder sb = new StringBuilder();
		for(Entry<String, Programmer> entry : programmers.entrySet()) {
			sb.append(entry.getKey() +"\n");
			sb.append(entry.getValue().showAvailabeServices());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static Class<? extends Service> getAvailableServiceClass(String user, String response) {
		return programmers.get(user).getAvailableService(response);
	}

	public static void addAmateur(Amateur amateur) {
		amateurs.put(amateur.getUserName(), amateur);
	}
	
	public static IUser getUser(String username) {
		IUser user = (programmers.get(username) == null) ?  amateurs.get(username) : programmers.get(username);
		return user;
	}
	


}
