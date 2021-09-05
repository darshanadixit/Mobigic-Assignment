package in.darshana.mobigicassignment;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Response;

import static java.security.AccessController.getContext;

public class WelcomeActivity extends AppCompatActivity implements RequestNotifier,View.OnClickListener {
    Context mContext;
    private NetworkCall networkCall;
    private Preference_Manager preference_manager;
    private TextView mTextview;
    private Button mBtnUploadFile, mBtnViewFiles;
    Toolbar mToolbar;
    SQLiteDbHelper sqLiteDbHelper = new SQLiteDbHelper(this);
    private static final int SELECT_FILE = 100;
    private File fileNews = null;
    ArrayList<Uri> mArrayUri;
    ArrayList<FileModel> fileModelArrayList;
    ArrayList<String> mFileArrayList;
    String strFilePath = "";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        mContext = getApplicationContext();
        networkCall = new NetworkCall(this,this);
        preference_manager = new Preference_Manager(mContext);
        mToolbar = findViewById(R.id.welcomeToolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome to Mobigic");
        mTextview = findViewById(R.id.welcome_username);
        mBtnUploadFile = findViewById(R.id.btn_uploadFile);
        mBtnViewFiles = findViewById(R.id.btn_ViewFile);
        mBtnUploadFile.setOnClickListener(this);
        mBtnViewFiles.setOnClickListener(this);
        mArrayUri = new ArrayList<>();
        fileModelArrayList = new ArrayList<>();
        mFileArrayList = new ArrayList<>();

        Intent intent = getIntent();
        String username = intent.getExtras().getString("key_name");

        mTextview.setText("Welcome "+username);

      /*  mBtnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permissionGranted())
                     openImageChooser();
                else
                    requestPermission();
            }
        });*/

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_uploadFile:
                if(permissionGranted())
                    openImageChooser();
                else
                    requestPermission();

                break;

            case R.id.btn_ViewFile:
                Intent intent = new Intent(WelcomeActivity.this, FileListActivity.class);
                intent.putExtra("key_file_list",fileModelArrayList);
                startActivity(intent);
        }
    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_FILE) {
            Uri selectedFileUri = data.getData();
            String path = getRealPathFromURI(mContext, selectedFileUri);

            if (path != null) {
                File f = new File(path);
                selectedFileUri = Uri.fromFile(f);
            }
            fileNews = new File(selectedFileUri.getPath()); //working code
            mArrayUri.add(selectedFileUri);
            networkCall.uploadFile(fileNews);//working code

        }
    }
    public static String getRealPathFromURI(final Context context, final Uri uri) {
        final boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        //DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(isKitkat && DocumentsContract.isDocumentUri(context,uri)){
                //ExternalStorageProvider
                if(isExternalStorageDocument(uri)){
                    final  String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if("primary".equalsIgnoreCase(type)){
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                //downloadprovider
                else if(isDownloadDocument(uri)){
                    final  String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(id));
                    return getDataColumn(context,contentUri,null,null);
                }
                //mediaprovider
                else if(isMediaDocument(uri)){
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if("image".equals(type)){
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("video".equals(type)){
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("audio".equals(type)){
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{ split[1]};

                    return getDataColumn(context,contentUri,selection,selectionArgs);
                }
            }
            //MediaStore and general
            else if("content".equalsIgnoreCase(uri.getScheme())){
                if(isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context,uri,null,null);

            }
            //file
            else if("file".equalsIgnoreCase(uri.getScheme())){
                return uri.getPath();
            }
        }
        return null;
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return  "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection= {column};

        try{
            cursor = context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
            if(cursor != null && cursor.moveToFirst()){
                final int index= cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isMediaDocument(Uri uri) {
        return  "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadDocument(Uri uri) {
        return  "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri){
        return  "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void notifySuccess(Response<?> response) {
        if (response.body() instanceof ResponseUploadFile) {
            ProgressView.dismiss();
            ResponseUploadFile responseUploadFile = (ResponseUploadFile) response.body();
            if (responseUploadFile.getStatuscode() == 200) {
                strFilePath = responseUploadFile.getResult().trim();
                mFileArrayList.add(strFilePath);
                callPostFile();
               // fileModelArrayList.add(new FileModel(strFilePath));

                Toast.makeText(mContext,"File Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
        else if(response.body() instanceof ResponseFilePost){
            ProgressView.dismiss();
            ResponseFilePost responseFilePost = (ResponseFilePost) response.body();
            if (responseFilePost.getStatuscode() == 200) {
                Log.d("Registration Response: ", responseFilePost.getMessage());
                Toast.makeText(mContext, responseFilePost.getMessage(), Toast.LENGTH_SHORT).show();
                //App_Alert.callAlertNormal(mContext, "Success", repoPostNews.getMessage());
                mFileArrayList.clear();
            }else if (responseFilePost.getStatuscode() == 500) {
                Log.d("Error", responseFilePost.getMessage());
                Toast.makeText(mContext, responseFilePost.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                //Log.d("Error", Constants_Errors.someThingWentWrong);
                Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callPostFile() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArrayFiles = new JsonArray();

        for (int i = 0; i < mFileArrayList.size(); i++) {
            // JsonPrimitive jsonPrimitive = new JsonPrimitive();
            jsonArrayFiles.add(mFileArrayList.get(i));
        }

        jsonObject.add("fileName", jsonArrayFiles);
        jsonObject.addProperty("regOn", AppUtils.getSysDate());

        networkCall.postFile(jsonObject);
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
