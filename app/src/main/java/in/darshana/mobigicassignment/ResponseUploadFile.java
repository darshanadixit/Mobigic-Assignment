package in.darshana.mobigicassignment;

import com.google.gson.annotations.SerializedName;

public class ResponseUploadFile {
    @SerializedName("statuscode")
    private int statuscode;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("Result")
    private String result;

    public int getStatuscode(){
        return statuscode;
    }

    public boolean isSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public String getResult(){
        return result;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
