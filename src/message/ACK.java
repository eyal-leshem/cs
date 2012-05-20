package message;

public class ACK {
	
	boolean	OK=true; 
	String	data;
	String	taskId; 
	String	implemntorName;
	public boolean isOK() {
		return OK;
	}
	public void setOK(boolean oK) {
		OK = oK;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getImplemntorName() {
		return implemntorName;
	}
	public void setImplemntorName(String implemntorName) {
		this.implemntorName = implemntorName;
	} 
	
	

}
