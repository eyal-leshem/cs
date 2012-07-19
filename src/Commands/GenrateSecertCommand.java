package Commands;

import java.io.IOException;

import javax.crypto.SecretKey;

import exceptions.AgentServiceException;

import sun.misc.BASE64Encoder;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorExcption;

public class GenrateSecertCommand implements Command {
	

	@Override
	public ACK excute(Implementor imp,Message msg) throws AgentServiceException  {
		
		SecretKey key; 
		String alg=msg.getAlg(); 
		
		//when alg isn't set use the default
		
		
		//implementor generate the secret key 
		try {
			key=imp.genrateSecertKey(alg,msg.getID());
		} catch (ImplementorExcption e) {
			throw new AgentServiceException("implementor can not genarate secret key with alg : "+alg, e);
		} 
		
		//encode the data to string 
		String stringKey;
		try{
			byte[] keyBytes=key.getEncoded(); 
			BASE64Encoder base64Encoder=new BASE64Encoder();
			stringKey=base64Encoder.encode(keyBytes); 
		}
		catch (Exception e) {
			throw new AgentServiceException("can not endoce the key on base 64", e);
		}
		//make an ack message 
		ACK ret=new ACK();				
		ret.setData(stringKey); 
		ret.setDataAlg(alg);
		ret.setDataKind("secret key");
		
		
		return ret;
	}

}
