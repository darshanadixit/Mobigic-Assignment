package in.darshana.mobigicassignment.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileListResultItem {

	@SerializedName("fileName")
	private List<String> fileName;

	@SerializedName("regOn")
	private String regOn;

	@SerializedName("__v")
	private int V;

	@SerializedName("_id")
	private String id;

	public void setFileName(List<String>  fileName){
		this.fileName = fileName;
	}

	public List<String>  getFileName(){
		return fileName;
	}

	public void setRegOn(String regOn){
		this.regOn = regOn;
	}

	public String getRegOn(){
		return regOn;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}