package Manager;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import exceptions.AgentServiceException;
import exceptions.ExceptionHelper;
import Implemtor.ImplementorManager;
import Logger.AgentServiceLogger;
import message.ACK;
import message.Message;

public class Manager {

	static AgentServiceConf conf; 
	/**
	 * go on loop : 
	 * 	1.ask the server for tasks 
	 * 	2. execute the tasks 
	 *  3. sleep x time 
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)  {
		
		
		AgentServiceLogger logger=AgentServiceLogger.getInstance();
		logger.init();		
		
		//get the configuration  of the agent service 
		try {
			getConf();
			
			Communicate net=new Communicate();
			
			//install all models 			 
			Parser parser=new Parser();
			ImplementorManager impManager=ImplementorManager.getInstance(); 
		 
					
			//run for ever 
			while(true){
				
				//get and updates from the net 
				String updates=net.getNewTasks();
				
				if(updates!=null&&!updates.equals("null")){
					ArrayList<Message> messages = null;
				    
					//parse the string message that get from the server 
					try{
						messages=parser.parseMessage(updates);
					}
					catch (Exception e) {
						//error while parsing send acknowledgment to the server  
						ACK ack=new ACK(); 
						ack.setOK(false); 
						ack.setErrorMsg("problem to parse update string -"+updates); 
						ack.setFullExceptionString(ExceptionHelper.getCustomStackTrace(e)); 
						ack.setTaskId("0"); 
						net.sendResponse(ack, conf); 
					}
					
					//commit all the tasks 
					if(messages!=null){
						for(Message msg:messages){
							ACK retMsg=impManager.commitTask(msg);
							net.sendResponse(retMsg); 
						}	
					}
					
				}//end of if(updates!=null)
				
				//commit all task 
				try {
					Thread.sleep(conf.getSleepTime()*1000);
				} catch (InterruptedException e) {} 
				
				
			}//end of while(true) 
			
		} catch (AgentServiceException e1) {
			unCatchException(e1,conf); 
		} 
	}

	/**
	 * inform the server about the fail 
	 * @param e1 - uncatch exception
	 * @param conf - agnet configration 
	 * @param net  - the communicate module for sending the task 
	 */
	private static void unCatchException(AgentServiceException e1,AgentServiceConf conf) {
		//error while parsing send acknowledgment to the server  
		
		Communicate net;
		try {
			net = new Communicate();
		} catch (AgentServiceException e3) {
			//the agent will be dead without notice to server because problem in connection 
			return ;
		} 
		String errorMsg=conf.getAgentName()+"- Unexpected error : "+e1.getMessage()+"\n"+"agent fall - RIP";  
		
		ACK ack=new ACK(); 
		ack.setOK(false); 
		ack.setErrorMsg(errorMsg); 
		ack.setFullExceptionString(ExceptionHelper.getCustomStackTrace(e1)); 
		try {
			net.sendResponse(ack, conf);
		} 
		catch (AgentServiceException e) {
			//try again 
			try {
				net.sendResponse(ack, conf);
			} catch (AgentServiceException e2) {}
		}
		
	}

	/**
	 * create new a agent service configuration from the file conf.cnf
	 * @throws AgentServiceException
	 */
	private static void getConf() throws AgentServiceException  {
		//read the json string that contain the properties from the file 
		File confFile=new File("conf.cnf"); 
		FileReader fr;
		
		try {
			fr = new FileReader(confFile);
		} catch (FileNotFoundException e) {
			throw new AgentServiceException("cound not file the configuration file - conf.cnf",e); 
		}
		
		char[] buffer=new  char[(int)confFile.length()];
		
		try {
			fr.read(buffer);
		} catch (IOException e) {
			throw new AgentServiceException("problem to read from the configuration file",e); 
		}
		finally{
			try {
				fr.close();
			} catch (IOException e) {}
		}
		
		String jsonConfStr=new String(buffer); 
		
	    //and use the json conf contractor 
	   conf= AgentServiceConf.IntallConf(jsonConfStr); 
	   
		
	}


	

	

}
