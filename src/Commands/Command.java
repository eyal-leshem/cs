package Commands;

import message.ACK;
import message.Message;
import Implemtor.Implementor;

public interface Command {
	
	public ACK excute(Implementor imp,Message msg) throws Exception; 

}
