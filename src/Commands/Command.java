package Commands;

import message.ACK;
import Implemtor.Implementor;

public interface Command {
	
	public ACK excute(Implementor imp) throws Exception; 

}
