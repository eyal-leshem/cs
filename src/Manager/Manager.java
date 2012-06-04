package Manager;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Implemtor.ImplementorManager;
import message.ACK;
import message.Message;

public class Manager {

	static AgentServiceConf conf; 
	
	public static void main(String[] args) throws Exception {
		getConf(); 
		
		Communicate net=new Communicate(); 
		Parser parser=new Parser(); 	
		ImplementorManager.setConf(conf); 
		ImplementorManager impManager=ImplementorManager.getInstance(); 
		
	
		while(true){
			String updates=net.getNewTasks(conf);
			if(updates!=null){
				ArrayList<Message> messages=parser.parseMessage(updates);
				
				for(Message msg:messages){
					ACK retMsg=impManager.commitTask(msg);
					net.sendResponse(retMsg,conf); 
				}	
			}
			
			
			try {
				Thread.sleep(conf.getSleepTime()*1000);
			} catch (InterruptedException e) {
								
			} 
		}
	}

	private static void getConf() throws Exception  {
		//read the json string that contain the properties from the file 
		File confFile=new File("conf.cnf"); 
		FileReader fr;
		
		try {
			fr = new FileReader(confFile);
		} catch (FileNotFoundException e) {
			throw new Exception("cound not file the configuration file - conf.cnf",e); 
		}
		
		char[] buffer=new  char[(int)confFile.length()];
		
		try {
			fr.read(buffer);
		} catch (IOException e) {
			throw new Exception("problem to read from the configuration file",e); 
		}
		finally{
			fr.close(); 
		}
		
		String jsonConfStr=new String(buffer); 
		
		//and use the json conf contractor 
	   conf= new AgentServiceConf(jsonConfStr); 
		
	}


	

	

}
