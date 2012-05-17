package Implemtor;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		
	}
	
	public Message commitTask(Message ms){
		//TODO 
		return null; 
	}
	

}
