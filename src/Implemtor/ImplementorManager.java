package Implemtor;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import message.ACK;
import message.Message;

public class ImplementorManager {
	
	Map<String,Implementor> implemtors=new HashMap<String,Implementor>();
	static ImplementorManager inst=null; 
	
	public static ImplementorManager getInstance() throws Exception{
		if(inst==null){
			return new ImplementorManager(); 
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
	
	public ACK commitTask(Message msg){
		synchronized (inst) {
			Implementor imp=getImplemntor(msg); 
			if (imp==null)	noSouchImp(); 
			if(msg.equals("genrate key Pair"))			genrateKeyPair(imp); 
			if(msg.equals("install cert")) 				installCert(imp); 
			if(msg.equals("genrate secret"))			genrateSecret(imp);
			if(msg.equals("install secret"))			installSeceret(imp); 
			
			
			
			
			
		} 
		return null; 
	}

	private void installSeceret(Implementor imp) {
		// TODO Auto-generated method stub
		
	}

	private void genrateSecret(Implementor imp) {
		// TODO Auto-generated method stub
		
	}

	private void installCert(Implementor imp) {
		// TODO Auto-generated method stub
		
	}

	private void genrateKeyPair(Implementor imp) {
		// TODO Auto-generated method stub
		
	}

	private void noSouchImp() {
		// TODO what happend when we haven't implemntor with this name 
		
	}

	private Implementor getImplemntor(Message msg) {
	
			return  implemtors.get(msg);
	}
	

}
