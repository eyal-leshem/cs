package Commands;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import sun.misc.BASE64Encoder;
import message.ACK;
import message.Message;
import Implemtor.Implementor;

public class GenrateKeyPairCommand implements Command {
	
	final String beginCertString="-----BEGIN CERTIFICATE-----\r\n"; 
	final String endCertString="\r\n-----END CERTIFICATE-----\r\n"; 

	@Override
	public ACK excute(Implementor imp,Message msg) throws Exception{
		Certificate cert=imp.genrateKeyPair("cn=a,ou=a,o=a,l=a,s=a,c=a");
		BASE64Encoder base64Encoder=new BASE64Encoder();
		
		byte[] certBytes;
		try {
			certBytes = cert.getEncoded();
		} catch (CertificateEncodingException e) {
		    throw new Exception("can't encode the cert",e); 
		}
		
		
		String crtBody=base64Encoder.encode(certBytes); 
		String crtFinal=beginCertString+crtBody+endCertString; 		
		
		ACK ret=new ACK();				
		ret.setData(crtFinal); 
		ret.setDataAlg(msg.getAlg());
		ret.setDataKind("certificate"); 
		
		return ret; 
	}

}
