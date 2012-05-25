package Manager;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xml.internal.utils.NSInfo;

import message.Message;

public class Parser {
	
	public ArrayList<Message> parseMessage(String msg) throws Exception{
		
		ArrayList<Message> 	msgArr=new ArrayList<Message>(); 
		int 				nextTokenIndex=msg.indexOf("},{"); 
		String 				tail=msg; 
		String 				head;
		Message 			temp; 
		
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
		
		
		
		return msgArr; 
	}
	
	private Message parseOneMessage(String head) throws Exception {
		JSONObject json=new JSONObject(head); 
		Message ret=new Message(); 
		ret.setID((String) json.get("taskId")); 
		ret.setDependOn((String)json.get("dependOn"));
		ret.setImplementorID((String)json.getString("implementorId"));
		ret.setKind((String)json.getString("kind")); 
		try{
			//we need to replace becuse the problem with '\n' end json parsers 
			if(ret.getKind().equals("install cert")){
				ret.setMsgData((String)json.getString("msgData"),true); 
			}
			ret.setMsgData((String)json.getString("msgData")); 
		}catch (Exception e) {
			// TODO: handle exception
		}
		String temp=(String)json.get("commandDate"); 
		ret.setTimeStamp(Timestamp.valueOf(temp)); 
		
		
		
		
		
		return ret;
	}

	public String buildMessage(Message msg){
		//TODO 
		return null; 
	}

}
