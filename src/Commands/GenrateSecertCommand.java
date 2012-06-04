package Commands;

import javax.crypto.SecretKey;

import sun.misc.BASE64Encoder;

import message.ACK;
import message.Message;
import Implemtor.Implementor;

public class GenrateSecertCommand implements Command {
	

	@Override
	public ACK excute(Implementor imp,Message msg) throws Exception {
		
		SecretKey key; 
		String alg=msg.getAlg(); 
		
		//when alg isn't set use the defualt
		
		
		//implemtor genrate the secret key 
		key=imp.genrateSecertKey(alg); 
		
		//encode the data to string 
		byte[] keyBytes=key.getEncoded(); 
		BASE64Encoder base64Encoder=new BASE64Encoder();
		String stringKey=base64Encoder.encode(keyBytes); 
		
		//make an ack message 
		ACK ret=new ACK();				
		ret.setData(stringKey); 
		ret.setDataAlg(alg);
		ret.setDataKind("certificate");
		
		
		return ret;
	}

}
