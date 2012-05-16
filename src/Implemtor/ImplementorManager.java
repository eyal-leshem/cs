package Implemtor;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.Map;

import message.Message;

public class ImplementorManager {
	
	Map<String,Implementor> implemtors=new HashMap<String,Implementor>();
	static ImplementorManager inst=null; 
	
	public static ImplementorManager getInstance(){
		if(inst==null){
			return new ImplementorManager(); 
		}
		return inst;
		
	}
	
	private ImplementorManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void change(){
		
	}
	
	public Message commitTask(Message ms){
		//TODO 
		return null; 
	}
	

}
