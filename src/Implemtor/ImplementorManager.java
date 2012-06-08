package Implemtor;

import java.io.File;

import message.ACK;
import message.Message;
import Commands.Command;
import Commands.CommandFactory;
import Manager.AgentServiceConf;
import exceptions.AgentServiceException;

public class ImplementorManager {
	
	
	private static 	AgentServiceConf		conf; 
	static 			ImplementorManager 		inst=null;	
	private			CommandFactory			commandFactory=CommandFactory.getInstance(); 
	private static 	PluginManger 			pluginManger; 


	
	
	
	/**
	 *the implementor manger is singleton becus we have only one in our program  
	 *
	 * @return an instance of this implementor manger 
	 * @throws Exception
	 */
	public static ImplementorManager getInstance() throws Exception{
		if(inst==null){
			inst= new ImplementorManager(); 
		}
		return inst;
		
	}
	
	private ImplementorManager() throws AgentServiceException  {
		
		
			try {
				pluginManger=PluginManger.getInstance();
			} catch (Exception e) {
				throw new AgentServiceException("can't get intance of plugin manger",e);
			}
			
			String pluginDirPath=new File(".").getAbsolutePath()+System.getProperty("file.separator")+"plugins";
			pluginManger.startObserving(pluginDirPath);
		
		
	}
		

	

	public ACK commitTask(Message msg) {
		
		ACK ack=new ACK();
		
		synchronized (inst) {
			//get the implementor 
			Implementor imp=getImplemntor(msg);			
			try{
				if (imp==null)	noSouchImp(msg.getImplementorID()); 
				
				//Execute the command 
				Command command=commandFactory.getCommand(msg); 
				ack=command.excute(imp,msg);
				
				//Everything go well
				ack.setOK(true); 
			}
			
			//case of exception 
			//create fail message to the server 
			catch (AgentServiceException e) {
				ack.setOK(false);
				ack.setErrorMsg(e.getMessage());
				ack.setFullExceptionString(getCustomStackTrace(e)); 
			}
			
			//the relevant properties for each ack 
			finally{
				ack.setTaskId(msg.getID()); 
				ack.setImplemntorName(imp.getName()); 
			}
		} 
		return ack; 
	}
	
	  /**
	  * Defines a custom format for the stack trace as String.
	  */
	  public static String getCustomStackTrace(Throwable aThrowable) {
	    
		 //add the class name and any message passed to constructor
	    StringBuilder result = new StringBuilder( "full stack trace: " );
	    result.append(aThrowable.toString());
	    String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }

	
	private void noSouchImp(String impName) throws AgentServiceException {
		throw new AgentServiceException("no such implmentor : "+impName); 
		
	}

	
	private Implementor getImplemntor(Message msg) {	
		 return 	pluginManger.getImplementor(msg); 
	}
	
	

	public static AgentServiceConf getConf() {
		return conf;
	}

	
	public static void setConf(AgentServiceConf conf) {
		ImplementorManager.conf = conf;
	}

	

}
