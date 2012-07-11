package message;

public class ACK {
	
	//commons 
	boolean	OK=true;
	String	taskId; 
	String	implemntorName;
	
	//ack with data
	String	data;
	String  dataAlg;
	String  dataKind;

	//error ack 
	String  errorMsg; 
	String  fullExceptionString; 
	
	public String getFullExceptionString() {
		return fullExceptionString;
	}
	public void setFullExceptionString(String fullExceptionString) {
		this.fullExceptionString = fullExceptionString;
	}
	public String getDataAlg() {
		return dataAlg;
	}
	public String getDataKind() {
		return dataKind;
	}
	public void setDataKind(String dataKind) {
		this.dataKind = dataKind;
	}
	public void setDataAlg(String dataAlg) {
		this.dataAlg = dataAlg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isOK(){
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
		if(taskId==null){
			return "0"; 
		}
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
