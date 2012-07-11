package message;

import java.sql.Date;
import java.sql.Timestamp;

public class Message 
{
	String ID;    //message identifier
	
	String dependOn;  

	String agentId;   
 
	Timestamp timeStamp;   //Date+time of sending
 
	String implementorID; //
 
	String msgData;   //string with data (public key...)
 
	String kind;  
	
	String alg; 

	public String getAlg() {
		return alg;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getImplementorID() {
		return implementorID;
	}

	public void setImplementorID(String implementorID) {
		this.implementorID = implementorID;
	}

	public String getMsgData() {
		return msgData;
	}
	
	public void setMsgData(String msgData){
		this.msgData = msgData;
	}

	public void setMsgData(String msgData,boolean replace) {
		if(replace){
			msgData=msgData.replace('#','\n');
			msgData=msgData.replace('*','\r');
		}
		this.msgData = msgData;
	}


	
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDependOn() {
		return dependOn;
	}

	public void setDependOn(String dependOn) {
		this.dependOn = dependOn;
	}
}