package Logger;

import java.io.File;
import java.util.Date;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/*
 * this class use as adapter for the log4j 
 * for case that we will want to play with 
 * encryption of the log 
 */
public class AgentServiceLogger{
	
	static Logger logger= Logger.getLogger(AgentServiceLogger.class);
	
	static  AgentServiceLogger inst=null; 
	
	private AgentServiceLogger() {}
	
	public static AgentServiceLogger getInstance(){
		if(inst==null){
			return new AgentServiceLogger(); 
		}
		return inst; 
	}
	
	
	public static  void init(){
		String slash=System.getProperty("file.separator"); 
		String path= new File(".").getAbsolutePath()+slash+"logs"+slash+"logConf.cnf" ; 
		PropertyConfigurator.configure(path);
		logger.debug(new Date().toString()+  "  -> start-logging"); 
	}
	
	public void info(String msg){
		String newMsg=editMessage(msg); 
		logger.info(newMsg); 
		
	}
	
	private String editMessage(String msg) {
		String nowDate=getDateNow(); 
		return nowDate+ " -> " + msg; 
	}

	private String getDateNow() {
		return new Date().toString(); 
	}

	public void debug(String msg){
		String newMsg=editMessage(msg); 
		logger.debug(newMsg); 
	}
	
	public void fatal(String msg){
		String newMsg=editMessage(msg); 
		logger.fatal(newMsg);
		
	}
	
	public void error(String msg){
		String newMsg=editMessage(msg); 
		logger.error(newMsg);
	}
	
	public void warn(String msg){
		String newMsg=editMessage(msg); 
		logger.warn(newMsg);
		
	}
	


}
