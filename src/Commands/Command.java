package Commands;

import exceptions.AgentServiceException;
import message.ACK;
import message.Message;
import Implemtor.Implementor;

public interface Command {
	/**
	 * tell the implementor to exceute  the command 
	 * 
	 * @param imp - the implementor  
	 * @param msg - the message to send 
	 * @return  ACK - for send back to the server ;    
	 * @throws Exception
	 */
	public ACK excute(Implementor imp,Message msg) throws AgentServiceException; 

}
