package Manager;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xml.internal.utils.NSInfo;

import exceptions.AgentServiceException;

import message.Message;

public class Parser {
	
	
	public ArrayList<Message> parseMessage(String msg) throws AgentServiceException{
		
		ArrayList<Message> 	msgArr=new ArrayList<Message>(); 
		int 				nextTokenIndex=msg.indexOf("},{"); 
		String 				tail=msg; 
		String 				head;
		Message 			temp; 
		
		if(msg==null || msg.equals("null")){
			return msgArr;
		}
		
		//case of "[{jasonbody}]"  only one json string 
		if(nextTokenIndex==-1){
			tail=msg.substring(1,msg.length());
		}
		try{
			//in case of more then one json string 
			//and because the php json_encode method it will look like 
			//[{json1},{json2},...] 
			while(nextTokenIndex!=-1){
				head=msg.substring(1,nextTokenIndex+1); 
				tail=tail.substring(nextTokenIndex+2);
				temp =parseOneMessage(head); 
				msgArr.add(temp);
				nextTokenIndex=tail.indexOf("},{");
			}
			tail=tail.substring(0,tail.length()-1);
			temp=parseOneMessage(tail);
			msgArr.add(temp); 
		}
		catch (AgentServiceException e) {
			throw new AgentServiceException("can't parse msg :" +msg,e); 
		}
		
		
		
		return msgArr; 
	}
	
	private Message parseOneMessage(String head) throws AgentServiceException {
	
		//generate json object fron the string 
		JSONObject json;
		try {
			json = new JSONObject(head);
		} catch (JSONException e) {
			throw new AgentServiceException("can't genrtate massage from the string",e); 
		} 
		Message ret=new Message(); 
		
		//--Extract information from the json object
		
		try {
			ret.setID((String) json.get("taskId"));
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"taskId\" in json string",e); 
		}
		
		try {
			ret.setDependOn((String)json.get("dependOn"));
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"dependOn\" in json string",e);
		}
		
		try {
			ret.setImplementorID((String)json.getString("implementorId"));
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"implementorId\" in json string",e);
		}
		
		try {
			ret.setKind((String)json.getString("kind"));
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"kind\" in json string",e);
		} 
		
		try {
			ret.setAlg(json.getString("alg"));
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"alg\" in json string",e);
		}
		
		try{
			//we need to replace becuse the problem with '\n' end json parsers 
			if(ret.getKind().equals("install cert")||ret.getKind().equals("install secert")){
				String data=json.getString("data"); 
				data.replace("\\n","\n");
				data.replace("\\r","\r"); 
				ret.setMsgData(data,true); 
				 
			}
		}catch (Exception e) {
			throw new AgentServiceException("problem to get the data from json string",e);
		}
		
		
		String temp;
		try {
			temp = (String)json.get("commandDate");
		} catch (JSONException e) {
			throw new AgentServiceException("can't find the \"commandDate\" in json string",e);
		} 
		//save it as time stemp 
		ret.setTimeStamp(Timestamp.valueOf(temp)); 
		
		
		
		
		
		return ret;
	}

	

}
