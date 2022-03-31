package exceptions;

public class ServiceNonStopable extends ServiceException {
	
	@Override
	public String getMessage() {
		return "Service ne peut pas est arreter";
	}

}
