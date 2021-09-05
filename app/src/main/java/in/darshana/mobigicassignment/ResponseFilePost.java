package in.darshana.mobigicassignment;

import com.google.gson.annotations.SerializedName;

public class ResponseFilePost {
    @SerializedName("statuscode")
    private int statuscode;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

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
}
