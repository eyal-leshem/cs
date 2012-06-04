package Commands;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import message.ACK;
import message.Message;
import Implemtor.Implementor;

public class InstallCertCommand implements Command {
	
	
	@Override
	public ACK excute(Implementor imp,Message msg) throws Exception {
		
		CertificateFactory cf=CertificateFactory.getInstance(msg.getAlg()); 
		Certificate cert= cf.generateCertificate(new ByteArrayInputStream(msg.getMsgData().getBytes())); 
		imp.installTrustCert(cert); 
		
		ACK ack=new ACK(); 
		return ack ; 
		
		
		
		
		
	}

}
