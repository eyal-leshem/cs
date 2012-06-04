package Commands;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import sun.misc.BASE64Decoder;

public class  InstallSecertCommand implements Command {
	


	@Override
	public ACK excute(Implementor imp,Message msg) throws Exception {
		
		String data=msg.getMsgData(); 
		String alg=msg.getAlg(); 
		
		BASE64Decoder base64Decoder=new BASE64Decoder(); 
		byte[] keyByte=base64Decoder.decodeBuffer(data);
		
		SecretKey key=new SecretKeySpec(keyByte,alg);
		
		imp.installSecertKey(key); 
		
		ACK ack=new ACK(); 		
		return ack ; 
		
	}

}
