package Commands;

import exceptions.AgentServiceException;
import message.Message;

public class CommandFactory {
	
 	private static CommandFactory inst=null;
 	
 	private CommandFactory(){
 		
 	}
 	
 	public static CommandFactory getInstance(){
 		if(inst==null){
 			return new CommandFactory(); 
 		}
 		return inst; 
 	}
 	/*
 	 * return the relevant command for each message 
 	 */
 	public  Command getCommand(Message msg) throws AgentServiceException{
		if(msg.getKind().equals("generate key Pair"))	    return new GenrateKeyPairCommand(); 
		if(msg.getKind().equals("generate secret"))			return new GenrateSecertCommand();
		if(msg.getKind().equals("install cert"))        	return new InstallCertCommand(); 	
		if(msg.getKind().equals("install secret"))			return new InstallSecertCommand(); 
		if(msg.getKind().equals("change conf"))				return new ChangeConfCommand();
		if(msg.getKind().equals("remove certifcate"))		return new DelCertCommand();
		if(msg.getKind().equals("add to crl"))				return new AddToCrlCommand();
		
		
		throw new  AgentServiceException("no such task suiteable to - "+msg.getKind()); 
 	
 	}

}
