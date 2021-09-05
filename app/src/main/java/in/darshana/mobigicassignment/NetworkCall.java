package in.darshana.mobigicassignment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.io.File;

import in.darshana.mobigicassignment.Model.ResponseGetFileList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall {
    Preference_Manager preference_manager;
    private Activity mActivity;
    private Context mContext;
    RequestNotifier mRequestNotifier;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;

    public NetworkCall(Context mContext1, RequestNotifier mNotifier1) {
        this.mContext = mContext1;
        this.mRequestNotifier = mNotifier1;

        preference_manager = new Preference_Manager(mContext);
    }

    public NetworkCall(Activity mActivity, RequestNotifier mNotifier) {
        this.mActivity = mActivity;
        this.mRequestNotifier = mNotifier;

        preference_manager = new Preference_Manager(mActivity);
    }
    @SuppressLint("MissingPermission")
    public boolean isNewtworkAvailable(){
        if(mActivity != null){
            connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        else if(mContext != null){
            connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return  activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void uploadFile(File file) {
        try {
            NetworkServiceApi networkServiceApi = RetrofitInstance.getRetrofitInstance().create(NetworkServiceApi.class);
            //System.out.println("Upload_Product_Single_Imgs_S3--> " + file.getName());
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("url", file.getName(), requestBodyFile);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("element1", "My Image")
                    .addPart(fileToUpload)
                    .build();

            Call<ResponseUploadFile> call = networkServiceApi.uploadFile(requestBody);
            call.enqueue(new Callback<ResponseUploadFile>() {
                @Override
                public void onResponse(@NonNull Call<ResponseUploadFile> call, @NonNull Response<ResponseUploadFile> response) {
                    if (response.code() == 200)
                        mRequestNotifier.notifySuccess(response);
                    else
                        mRequestNotifier.notifyString("Something Went Wrong Please Try Again....!!!");
                }

                @Override
                public void onFailure(@NonNull Call<ResponseUploadFile> call, @NonNull Throwable t) {
                    Log.e("API_Error  ", "Error Resp_Upload_file--> " + t.getMessage());
                    mRequestNotifier.notifyError(t);
                }
            });
        } catch (Exception e) {
            Log.e("JsonError  ", "Error Resp_Upload_file -->" + e.getMessage());

            e.printStackTrace();
        }
    }
    public void postFile(JsonObject jsonObject) {
        if (isNewtworkAvailable()) {
            try {

                NetworkServiceApi networkServiceApi = RetrofitInstance.getRetrofitInstance().create(NetworkServiceApi.class);

                Call<ResponseFilePost> responseFilePostCall = networkServiceApi.postFile(jsonObject);

                responseFilePostCall.enqueue(new Callback<ResponseFilePost>() {
                    @Override
                    public void onResponse(Call<ResponseFilePost> call, Response<ResponseFilePost> response) {
                        if (response.code() == 200)
                            mRequestNotifier.notifySuccess(response);
                        else
                            mRequestNotifier.notifyString("error...!");
                    }

                    @Override
                    public void onFailure(Call<ResponseFilePost> call, Throwable t) {
                        Log.e("API_Error", "Error Repo_Post_File->" + t.getMessage());
                        mRequestNotifier.notifyError(t);
                    }
                });

            } catch (Exception e) {
                Log.e("Error-->", "Error Repo_Post_File->" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            mRequestNotifier.notifyNoInternet();
        }
    }

    public void getAllFileList() {
            if (isNewtworkAvailable()) {
                try {

                    NetworkServiceApi networkServiceApi = RetrofitInstance.getRetrofitInstance().create(NetworkServiceApi.class);

                    Call<ResponseGetFileList> responseGetFileListCall = networkServiceApi.getAllFiles();

                    responseGetFileListCall.enqueue(new Callback<ResponseGetFileList>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseGetFileList> call, @NonNull Response<ResponseGetFileList> response) {
                            if (response.code() == 200)
                                mRequestNotifier.notifySuccess(response);

                            else
                                mRequestNotifier.notifyString("Something Went Wrong Please Try Again....!!!");

                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseGetFileList> call, @NonNull Throwable t) {
                            Log.e("API_Error  ", "Error  Repo Get All Files->" + t.getMessage());

                            mRequestNotifier.notifyError(t);
                        }
                    });
                } catch (Exception e) {
                    Log.e("Error-->", " Repo Get All Files->" + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                mRequestNotifier.notifyNoInternet();
            }
    }

}
