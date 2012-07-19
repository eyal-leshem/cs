package Commands;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import exceptions.AgentServiceException;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorExcption;

public class InstallCertCommand implements Command {
	
	
	@Override
	public ACK excute(Implementor imp,Message msg) throws AgentServiceException  {
		
		//get certificate factory innstace 
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance(msg.getAlg());
		} catch (CertificateException e) {
			throw new AgentServiceException("can not genrate certifcate factory with alogrithem :"+msg.getAlg() ,e);
		} 
		
		//genrate certificate from the data 
		Certificate cert;
		try {
			cert = cf.generateCertificate(new ByteArrayInputStream(msg.getMsgData().getBytes()));
		} catch (CertificateException e) {
			throw new AgentServiceException("can not genrate cerrificate from the msg data", e);
		} 
		
		//install the trusted cert on the implementor
		try {
			imp.installTrustCert(cert,msg.getID());
		} catch (ImplementorExcption e) {
			throw new AgentServiceException("imlemntor can not insall the trust cert , implemtorID:"+imp.getName(), e); 
		} 
		
		ACK ack=new ACK(); 
		return ack ; 
		
		
		
		
		
	}

}
