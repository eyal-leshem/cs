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

import com.sun.xml.internal.ws.message.ByteArrayAttachment;

import sun.misc.BASE64Encoder;

import message.ACK;
import message.Message;

public class ImplementorManager {
	
	CommandFactory commandFactory=CommandFactory.getInstance(); 
	
	private String agentName=null; 
	
	Map<String,Implementor> implemtors=new HashMap<String,Implementor>();
	static ImplementorManager inst=null;
	
	
	public static ImplementorManager getInstance() throws Exception{
		if(inst==null){
			inst= new ImplementorManager(); 
		}
		return inst;
		
	}
	
	private ImplementorManager() throws Exception {
		PluginFactory pluginFactory=new PluginFactory(); 
		ArrayList<Implementor>  implemtorArr=pluginFactory.getClassArr("c:\\temp\\agentService\\plugins"); 
		for (Implementor implementor : implemtorArr) {
			implemtors.put(implementor.getName(),implementor); 
		}
		
		
		
		
		
	}
	
	public void change(){
		synchronized (inst) {
			
		}
		
	}
	
	public ACK commitTask(Message msg) throws Exception{
		
		ACK ack=null;
		synchronized (inst) {
			Implementor imp=getImplemntor(msg);			
			try{
				if (imp==null)	noSouchImp(); 
				Command command=commandFactory.getCommand(msg.getKind()); 
				ack=command.excute(imp);
				
		 
			}
			catch (Exception e) {
				ack.setOK(false);  
			}			
			ack.setTaskId(msg.getID()); 		
		} 
		return ack; 
	}


	private void noSouchImp() throws Exception {
		throw new Exception(); 
		
	}

	private Implementor getImplemntor(Message msg) {
	
			return  implemtors.get(msg.getImplementorID());
	}
	
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	

	

}
