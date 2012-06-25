package Commands;

import java.math.BigInteger;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorExcption;
import exceptions.AgentServiceException;

public class AddToCrlCommand implements Command {

	@Override
	public ACK excute(Implementor imp, Message msg)
			throws AgentServiceException {
		
			BigInteger serial=new BigInteger(msg.getMsgData());
			
			//call to implementor that will ad it to crl 
			try {
				imp.addToCrl(serial);
			} catch (ImplementorExcption e) {
				throw new AgentServiceException("imlemntor -"+imp.getName()+"can't add this cert to the crl" ,e); 
			} 
			
			//return the ack 
			ACK ack=new ACK();
			ack.setOK(true); 
			return ack; 
			
		 
	}

}
