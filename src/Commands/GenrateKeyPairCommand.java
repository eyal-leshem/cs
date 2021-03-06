package Commands;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import sun.security.provider.X509Factory;
import java.security.cert.CertificateEncodingException;

import exceptions.AgentServiceException;

import sun.misc.BASE64Encoder;
import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorExcption;

public class GenrateKeyPairCommand implements Command {
	
	final String beginCertString=X509Factory.BEGIN_CERT; 
	final String endCertString=X509Factory.END_CERT; 

	@Override
	public ACK excute(Implementor imp,Message msg) throws AgentServiceException {
		
		Certificate cert;
		
		//tell the implementor to generate key pair and return 
		//the certificate 
		try {
			cert = imp.genrateKeyPair("cn=a,ou=a,o=a,l=a,s=a,c=a",msg.getID());			
		} catch (ImplementorExcption e1) {
			throw new AgentServiceException("implemtor cant genrate key pair from certificate ", e1); 
		}
		BASE64Encoder base64Encoder=new BASE64Encoder();
		
		//get the cert bytes 
		byte[] certBytes;
		try {
			certBytes = cert.getEncoded();
		} catch (CertificateEncodingException e) {
		    throw new AgentServiceException("can not encode the cert",e); 
		}
		
		
		//encode it in base64 
		String crtBody=base64Encoder.encode(certBytes); 
		String crtFinal=beginCertString+"\r\n"+crtBody+"\r\n"+endCertString; 		
		
		//set the relvant data to the ack 
		ACK ret=new ACK();				
		ret.setData(crtFinal); 
		ret.setDataAlg(msg.getAlg());
		ret.setDataKind("certificate"); 
		
		return ret; 
	}

}
