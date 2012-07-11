package Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import exceptions.AgentServiceException;

public class ChangeConfCommand implements Command  {

	@Override
	public ACK excute(Implementor imp, Message msg)
			throws AgentServiceException {
		
		JSONObject newConf;
		try {
			newConf=new JSONObject(msg.getMsgData());
		} catch (JSONException e) {
			throw new AgentServiceException("can't genrate json from the conf string of the server",e); 
		} 
		
		JSONObject oldConf=getOldConf(); 
		
		try{
			int sleeptime= newConf.getInt("sleepTime");
			oldConf.put("sleepTime", sleeptime); 
		}catch (Exception ignore){}
		
		try{
			String url= newConf.getString("urlSendAck");
			oldConf.put("urlSendAck", url.trim()); 
		}catch (Exception ignore){}
		
		try{
			String url= newConf.getString("urlGetTask");
			oldConf.put("urlGetTask", url.trim()); 
		}catch (Exception ignore){}
		
		try{
			String url= newConf.getString("urlNewImplemtor");
			oldConf.put("urlNewImplemtor", url.trim()); 
		}catch (Exception ignore){}
		
	   try {
			FileWriter fw=new FileWriter("conf.cnf");
			fw.write(oldConf.toString());
			fw.flush(); 
			fw.close(); 
	   	} catch (IOException e) {
		throw new AgentServiceException("can't write the new conf to file"); 
	   } 
	   
		
		ACK ack= new ACK(); 
		ack.setData(oldConf.toString()); 
		ack.setDataKind("jsonConf"); 
		
		return ack; 
		
	}

	/**
	 * genarte a json str from the file in conf.cnf 
	 * @return
	 * @throws AgentServiceException
	 */
	private JSONObject getOldConf() throws AgentServiceException {
		
		
		File file= new File("conf.cnf"); 
	
		//open file reader
		FileReader fr; 
		try {
			fr=new FileReader(file);
		} catch (FileNotFoundException e) {
			throw new AgentServiceException("can't open comfiguration file in conf.cnf",e); 
		} 
		
		char[] jsonArr= new char[(int)file.length()];
		
		//try to read from the file 
		try {
			fr.read(jsonArr);
			fr.close(); 
		} catch (IOException e) {
			throw new AgentServiceException("can't read data from configuraniom file at conf.cnf",e); 
		} 
		
		//genrate json 
		JSONObject jsonObject;
		try {
			jsonObject=new JSONObject(new String(jsonArr));
		} catch (JSONException e) {
			throw new AgentServiceException("can't genrate json obkect from the string in the file",e); 
		} 
		
		return jsonObject; 
		
		
	}

}
