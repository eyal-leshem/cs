package Manager;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;

import Implemtor.ImplementorManager;
import Implemtor.ListenPluginDir;

import message.ACK;
import message.Message;
import java.util.Date;
public class Manager {

	static final String agentName=getNameAgent();
	
	public static void main(String[] args) throws Exception {
		//TODO get agnet name from conf file 
		String agentName="yosi";
		Communicate net=new Communicate(); 
		Parser parser=new Parser(); 		
		ImplementorManager impManager=ImplementorManager.getInstance(); 
		impManager.setAgentName(agentName); 
	
		while(true){
			String updates=net.getNewTasks();
			if(updates!=null){
				ArrayList<Message> messages=parser.parseMessage(updates);
				for(Message msg:messages){
					ACK retMsg=impManager.commitTask(msg);
					net.sendResponse(retMsg,agentName); 
				}	
			}
			
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
								
			} 
		}
	}

	private static String getNameAgent() {
		// TODO Auto-generated method stub
		return "agent1" ;
	}
	

	

}
