package Manager;

import java.util.ArrayList;

import org.json.*;

import Implemtor.PluginManger;

import exceptions.AgentServiceException;

public class AgentServiceConf {
	
	private 	String agentName; 	
	private		String urlSendAck;
	private		String urlGetTask; 
	private		String urlNewImplemtor; 
	
	private		int	   sleepTime; 
	
	private	static AgentServiceConf inst=null;  
	
	
	private AgentServiceConf(String jsonConfStr) throws AgentServiceException  {
		JSONObject json;
	
		try{
			json=new JSONObject(jsonConfStr); 
			
			agentName= json.getString("agentName");
			urlSendAck=json.getString("urlSendAck"); 
			urlGetTask=json.getString("urlGetTask"); 
			urlNewImplemtor=json.getString("urlNewImplemtor"); 
		
			sleepTime=json.getInt("sleepTime");
			
		}catch (JSONException e) {
			throw new AgentServiceException("can't parse legal json from the string :\n "+jsonConfStr,e); 
		}	
				
				
	}
	
	public static AgentServiceConf IntallConf(String jsonStr) throws AgentServiceException{
		AgentServiceConf conf=new AgentServiceConf(jsonStr); 
		inst=conf; 
		return inst;
	}
	
	public static AgentServiceConf getInstance() throws AgentServiceException{
		if(inst==null)
			throw new AgentServiceException("need to install befor use via IntallConf(String jsonStr)");
		return inst; 
	}
	
	//////setters and geter's 
	public int getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getUrlSendAck() {
		return urlSendAck;
	}
	public void setUrlSendAck(String urlSendAck) {
		this.urlSendAck = urlSendAck;
	}
	public String getUrlGetTask() {
		return urlGetTask;
	}
	public String getUrlNewImplemtor() {
		return urlNewImplemtor;
	}

	public void setUrlNewImplemtor(String urlNewImplemtor) {
		this.urlNewImplemtor = urlNewImplemtor;
	}

	public void setUrlGetTask(String urlGetTask) {
		this.urlGetTask = urlGetTask;
	}

	
	
	
	
	
	

}
