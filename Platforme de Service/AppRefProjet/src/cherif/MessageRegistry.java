package cherif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MessageRegistry {
	private static HashMap<String, HashMap<String, ArrayList<String>>> messages = new HashMap<String, HashMap<String, ArrayList<String>>>();

	public static void send(String sender, String receiver, String message) {
		if(messages.get(receiver) == null) {
			messages.put(receiver, new HashMap<String, ArrayList<String>>());
		}
		if(messages.get(receiver).get(sender)==null) {
			messages.get(receiver).put(sender, new ArrayList<String>());
		}
		
		messages.get(receiver).get(sender).add(message);
	}
	
	public static String seeMessages(String username) {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, ArrayList<String>> entry : messages.get(username).entrySet()) {
			sb.append("From : " + entry.getKey()+" :");
			for(String message: entry.getValue()) {
				sb.append(message);
			}
		}
		return sb.toString();
	}

}
