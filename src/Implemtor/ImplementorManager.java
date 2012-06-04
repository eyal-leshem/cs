package Implemtor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Commands.Command;
import Commands.CommandFactory;
import Manager.AgentServiceConf;

import com.sun.xml.internal.ws.message.ByteArrayAttachment;

import sun.misc.BASE64Encoder;

import message.ACK;
import message.Message;

public class ImplementorManager {
	
	
	private static 	AgentServiceConf		conf; 
	static 			ImplementorManager 		inst=null;	
	private			CommandFactory			commandFactory=CommandFactory.getInstance(); 
	private static 	PluginManger 			pluginManger; 


	
	
	
	
	public static ImplementorManager getInstance() throws Exception{
		if(inst==null){
			inst= new ImplementorManager(); 
		}
		return inst;
		
	}
	
	private ImplementorManager() throws Exception {
		
		try {
			pluginManger=PluginManger.getInstance(); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
		

	

	public ACK commitTask(Message msg) throws Exception{
		
		ACK ack=null;
		
		synchronized (inst) {
			Implementor imp=getImplemntor(msg);			
			try{
				if (imp==null)	noSouchImp(); 
				Command command=commandFactory.getCommand(msg); 
				ack=command.excute(imp,msg);
				ack.setOK(true); 
			}
			catch (Exception e) {
				ack.setOK(false);
				ack.setErrorMsg(e.getMessage());
			}
			finally{
				ack.setTaskId(msg.getID()); 
				ack.setImplemntorName(imp.getName()); 
			}
		} 
		return ack; 
	}


	private void noSouchImp() throws Exception {
		throw new Exception(); 
		
	}

	private Implementor getImplemntor(Message msg) {	
		 return 	pluginManger.getImplementor(msg); 
	}
	

	public static AgentServiceConf getConf() {
		return conf;
	}

	public static void setConf(AgentServiceConf conf) {
		ImplementorManager.conf = conf;
	}

	

}
