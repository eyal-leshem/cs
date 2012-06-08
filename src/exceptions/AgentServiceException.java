package exceptions;

public class AgentServiceException extends Exception {
	
	public AgentServiceException(String msg) {
		super(msg); 
	}
	
	public AgentServiceException(String msg,Throwable e) {
		super(msg, e); 
	}

}
