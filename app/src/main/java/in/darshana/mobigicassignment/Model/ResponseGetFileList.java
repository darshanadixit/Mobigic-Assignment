package in.darshana.mobigicassignment.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetFileList{

	@SerializedName("statuscode")
	private int statuscode;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("ttlRecords")
	private int ttlRecords;

	@SerializedName("Result")
	private List<FileListResultItem> result;

	public void setStatuscode(int statuscode){
		this.statuscode = statuscode;
	}

	public int getStatuscode(){
		return statuscode;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setTtlRecords(int ttlRecords){
		this.ttlRecords = ttlRecords;
	}

	public int getTtlRecords(){
		return ttlRecords;
	}

	public void setResult(List<FileListResultItem> result){
		this.result = result;
	}

	public List<FileListResultItem> getResult(){
		return result;
	}
}