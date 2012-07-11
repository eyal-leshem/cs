package exceptions;

import Logger.AgentServiceLogger;

public class AgentServiceException extends Exception {
	
	public static 	AgentServiceLogger 	logger=AgentServiceLogger.getInstance(); 
	public final    String 				NEW_LINE = System.getProperty("line.separator");
	
	public AgentServiceException(String msg) {
		
		super(msg);
		logger.error("agent service problem :"+msg );
	}
	
	public AgentServiceException(String msg,Throwable e) {
		super(msg, e); 
		logger.error("agent service problem :"+msg);
	
	}

}
