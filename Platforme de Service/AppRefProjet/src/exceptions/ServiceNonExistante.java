package exceptions;

public class ServiceNonExistante extends ServiceException {
	
	@Override
	public String getMessage() {
		return "Service non reconnue";
	}

}
