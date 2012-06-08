package Manager;

import org.json.*;

import exceptions.AgentServiceException;

public class AgentServiceConf {
	
	private 	String agentName; 	
	private		String urlSendAck;
	private		String urlGetTask; 

	private		int	   sleepTime; 
	
	
	public AgentServiceConf(String jsonConfStr) throws AgentServiceException  {
		JSONObject json;
	
		try{
			json=new JSONObject(jsonConfStr); 
			
			agentName= json.getString("agentName");
			urlSendAck=json.getString("urlSendAck"); 
			urlGetTask=json.getString("urlGetTask"); 
		
			sleepTime=json.getInt("sleepTime");
			
		}catch (JSONException e) {
			throw new AgentServiceException("can't parse legal json from the string :\n "+jsonConfStr,e); 
		}
		
		
		
		
		
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
	public void setUrlGetTask(String urlGetTask) {
		this.urlGetTask = urlGetTask;
	}

	
	
	
	
	
	

}
