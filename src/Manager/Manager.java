package Manager;

import java.util.ArrayList;

import Implemtor.ImplementorManager;
import Implemtor.ListenPluginDir;

import message.Message;

public class Manager {

	
	public static void main(String[] args) throws Exception {
		
		
		
		Communicate net=new Communicate(); 
		Parser parser=new Parser(); 
		String str="[{\"taskId\":\"1\",\"dependOn\":\"0\",\"kind\":\"GenerateKey\",\"implementorId\":\"Tomcat\",\"commandDate\":\"2012-05-19 22:06:44\"}!{\"taskId\":\"2\",\"dependOn\":\"0\",\"kind\":\"GenerateKey\",\"implementorId\":\"Tomcat\",\"commandDate\":\"2012-05-19 22:06:44\"}]";
		parser.parseMessage(str); 
		
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
