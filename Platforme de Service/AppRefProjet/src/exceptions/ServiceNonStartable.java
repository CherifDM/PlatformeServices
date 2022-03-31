package exceptions;

public class ServiceNonStartable extends ServiceException {
	
	@Override
	public String getMessage() {
		return "Service ne peut pas est demarer";
	}

}
