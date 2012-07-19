package Implemtor;

import java.io.File;

import message.ACK;
import message.Message;
import Commands.Command;
import Commands.CommandFactory;
import Logger.AgentServiceLogger;
import Manager.AgentServiceConf;
import exceptions.AgentServiceException;
import exceptions.ExceptionHelper;

public class ImplementorManager {
	
	
	private static 	AgentServiceConf		conf; 
	static 			ImplementorManager 		inst=null;	
	private			CommandFactory			commandFactory=CommandFactory.getInstance(); 
	private static 	PluginManger 			pluginManger; 
	private static  AgentServiceLogger		logger=AgentServiceLogger.getInstance(); 


	
	
	
	/**
	 *the implementor manger is singleton becus we have only one in our program  
	 *
	 * @return an instance of this implementor manger 
	 * @throws Exception
	 */
	public static ImplementorManager getInstance() throws AgentServiceException{
		if(inst==null){
			inst= new ImplementorManager(); 
		}
		return inst;
		
	}
	
	private ImplementorManager() throws AgentServiceException  {
		
			conf= AgentServiceConf.getInstance(); 
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
				//implemtor nop mean mission for agent without  implemtor 
				if (imp==null && !msg.getImplementorID().equals("nop"))	noSouchImp(msg.getImplementorID()); 
				
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
				ack.setFullExceptionString(ExceptionHelper.getCustomStackTrace(e)); 
				logger.error("problem to confrim the task - "+msg.getID());
			}
			
			//the relevant properties for each ack 
			finally{
				ack.setTaskId(msg.getID());
				
				//get the implementor name
				if(imp!=null)
					ack.setImplemntorName(imp.getName());
				else 
					ack.setImplemntorName("nop"); 
				
				//report to the log on error 
				logger.error("implemtor '"+msg.getImplementorID()+"' perfrom task "+ msg.getID());
			}
		} 
		return ack; 
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
	


	

}
