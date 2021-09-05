package in.darshana.mobigicassignment;

import com.google.gson.JsonObject;

import in.darshana.mobigicassignment.Model.ResponseGetFileList;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetworkServiceApi {
    @POST("common/Upload_Single_Imgs_To_S3")
    Call<ResponseUploadFile> uploadFile(@Body RequestBody body);

    @POST("fileUpload/post_file")
    Call<ResponseFilePost> postFile(@Body JsonObject jsonBody);

    @GET("fileUpload/getAll_files")
    Call<ResponseGetFileList> getAllFiles();
}
