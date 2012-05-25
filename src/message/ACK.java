package message;

public class ACK {
	
	boolean	OK=true; 
	String	data;
	String  dataAlg;
	String  dataKind;
	String	taskId; 
	String	implemntorName;
	String  errorMsg; 
	
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
