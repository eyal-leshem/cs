package Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import message.ACK;
import message.Message;
import Implemtor.Implementor;
import Implemtor.ImplementorManager;
import Implemtor.PluginManger;
import exceptions.AgentServiceException;

public class newInstanceCommand implements Command{
	


	/*
	 * message json format : { "pluginName":"the name of the plugin",  
	 * 				 		   "params":{
	 * 										jsonOfParmas; 
	 * 									}
	 * 
	 */
	
	@Override
	public ACK excute(Implementor imp, Message msg)			throws AgentServiceException {
		
		 		
		String  data= msg.getMsgData(); 
		
		//generate json from the message data 
		JSONObject jsonData=null;
		try {
			jsonData=new JSONObject(data);
		} catch (JSONException e) {
			throw new AgentServiceException("problem to genarate json prom the data", e); 
		} 
		
		//get parameters from the json string
		String pluginName=null; 
		String propData=null; 
		try {
			pluginName=jsonData.getString("pluginName");
			propData=jsonData.getString("params"); 
		} catch (JSONException e) {
			throw new AgentServiceException("can't get proprties from implemntor",e); 
		} 
		
		//create new instance of implementor
		PluginManger pluginManger=PluginManger.getInstance();
		String instName=pluginManger.addNewInstance(pluginName,propData); 
		
		//generate the ack with the message that contain
		//the name of the implemtor
		ACK ack=new ACK(); 
		ack.setDataKind("instance name"); 
		ack.setData(instName); 
		ack.setOK(true); 
		return ack; 
	}
	
	
	
}
