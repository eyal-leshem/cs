package Commands;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import exceptions.AgentServiceException;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorExcption;
import sun.misc.BASE64Decoder;

public class  InstallSecertCommand implements Command {
	


	@Override
	public ACK excute(Implementor imp,Message msg) throws AgentServiceException  {
		
		String data=msg.getMsgData(); 
		String alg=msg.getAlg(); 
		
		
		BASE64Decoder base64Decoder=new BASE64Decoder(); 
		
		//decode the bytes from the string format message
		byte[] keyByte;
		try {
			keyByte = base64Decoder.decodeBuffer(data);
		} catch (IOException e) {
			throw new AgentServiceException("can't decode the data bytes from the messege ", e); 
		}
		
		SecretKey key=new SecretKeySpec(keyByte,alg);
		
		//install the secret key on the implentor 
		try {
			imp.installSecertKey(key);
		} catch (ImplementorExcption e) {
			throw new AgentServiceException("can't install the trust cert in the implemtor , implemntorID: "+imp.getName(),e); 
		} 
		
		ACK ack=new ACK(); 		
		return ack ; 
		
	}

}
