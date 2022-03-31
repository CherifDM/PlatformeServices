package exceptions;

public class ServiceNonConformeBRIExecption extends ServiceException {
	
	@Override
	public String getMessage() {
		return "Service non conforme a la norme BRI";
	}

}
