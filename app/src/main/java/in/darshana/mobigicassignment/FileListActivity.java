package in.darshana.mobigicassignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.darshana.mobigicassignment.Model.FileListResultItem;
import in.darshana.mobigicassignment.Model.ResponseGetFileList;
import retrofit2.Response;

public class FileListActivity extends AppCompatActivity implements RequestNotifier {
    Toolbar mToolbar;
    Context mContext;
    private NetworkCall networkCall;
    private Preference_Manager preference_manager;
    ArrayList<FileModel> fileModelArrayList;
    private RecyclerView mRecyclerViewFileList;
    FileListAdapter mFileListAdapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_list_activity);
        mToolbar = findViewById(R.id.file_list_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("File List");

        mContext = getApplicationContext();
        networkCall = new NetworkCall(this,this);
        preference_manager = new Preference_Manager(mContext);

        initView();


    }

    private void initView() {
        //Intent intent = getIntent();
      //  fileModelArrayList = (ArrayList<FileModel>) getIntent().getSerializableExtra("key_file_list");
        mRecyclerViewFileList = findViewById(R.id.recyclerviewFileList);

        apiCallGetFileList();

        /*mFileListAdapter = new FileListAdapter(fileModelArrayList, mContext);
        linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mRecyclerViewFileList.setLayoutManager(linearLayoutManager);
        mRecyclerViewFileList.setAdapter(mFileListAdapter);*/
    }

    private void apiCallGetFileList() {
        //ProgressView.show(mContext);
        networkCall.getAllFileList();
    }

    @Override
    public void notifySuccess(Response<?> response) {
        if (response.body() instanceof ResponseGetFileList){
            ProgressView.dismiss();
            ResponseGetFileList responseGetFileList = (ResponseGetFileList) response.body();
            if(responseGetFileList.getStatuscode() == 200){
                Log.d("Success", responseGetFileList.getMessage());
                List<FileListResultItem> fileListResultItems =responseGetFileList.getResult();
                buildRecyclerView(fileListResultItems);
            }
            else if(responseGetFileList.getStatuscode() == 202){
                Log.d("Error", responseGetFileList.getMessage());
                Toast.makeText(mContext, responseGetFileList.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(responseGetFileList.getStatuscode() == 404){
                Log.d("Error", responseGetFileList.getMessage());
                Toast.makeText(mContext, responseGetFileList.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(responseGetFileList.getStatuscode() == 500){
                Log.d("Error", responseGetFileList.getMessage());
                Toast.makeText(mContext, responseGetFileList.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("Error", "Something Went Wrong");
                Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void buildRecyclerView(List<FileListResultItem> fileListResultItems) {
        mFileListAdapter = new FileListAdapter(fileListResultItems, mContext);
        mRecyclerViewFileList.setAdapter(mFileListAdapter);
        mRecyclerViewFileList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void notifyNoInternet() {
        ProgressView.dismiss();
        Toast.makeText(mContext, "No internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyError(Throwable throwable) {
        ProgressView.dismiss();
        Toast.makeText(mContext, "Throwable "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyString(String s) {
        ProgressView.dismiss();
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }
}
