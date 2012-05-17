package Manager;

import java.util.ArrayList;

import Implemtor.ImplementorManager;

import message.Message;

public class Manager {

	
	public static void main(String[] args) throws Exception {
		
		
		
		Communicate net=new Communicate(); 
		Parser parser=new Parser(); 
		ImplementorManager impManager=ImplementorManager.getInstance(); 
		
		while(true){
			String updates=net.getNewTasks(); 
			ArrayList<Message> messages=parser.parseMessage(updates);
			for(Message msg:messages){
				Message retMsg=impManager.commitTask(msg);
				String jsonMsg=parser.buildMessage(retMsg); 
				net.sendResponse(jsonMsg); 
			}		
			
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
								
			} 
		}
	}
	
	
	

}
