package in.darshana.mobigicassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEdtUserName;
    private EditText mEdtPassword;

    private Button mBtnLogin;
    private Button mBtnSignUp;
    private SQLiteDbHelper mSqLiteDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Toolbar mToolbar;
    String loginUserName,loginPassword,signupUsername,signupPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSqLiteDbHelper = new SQLiteDbHelper(this);
        mEdtUserName = findViewById(R.id.edt_signup_username);
        mEdtPassword = findViewById(R.id.edt_login_password);

        mBtnLogin = findViewById(R.id.btn_Login);
        mBtnSignUp = findViewById(R.id.btn_signup);

        mBtnLogin.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Login:
                userLogin();
                break;

            case R.id.btn_signup:
                userSignUp();
                break;
        }
    }

    private void userSignUp() {
        signupUsername = mEdtUserName.getText().toString().trim();
        signupPassword = mEdtPassword.getText().toString().trim();

        if (signupUsername.isEmpty()|| signupPassword.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Make Sure you entered all the details");
            builder.setTitle("Error");
            builder.setPositiveButton(android.R.string.ok, null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            // Gets the data repository in write mode
             sqLiteDatabase = mSqLiteDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_USERNAME, signupUsername);
            values.put(UserContract.UserEntry.COLUMN_PASSWORD, signupPassword);

            showMessage("Success", "Sign Up Successfully");
            clearText();

            long newRowId;
            newRowId = sqLiteDatabase.insert(
                    UserContract.UserEntry.TABLE_NAME,
                    UserContract.UserEntry.COLUMN_NAME_NULLABLE,
                    values);
        }


    }

    private void userLogin() {
        sqLiteDatabase = mSqLiteDbHelper.getReadableDatabase();

        if(mEdtPassword.getText().toString().trim().isEmpty() ||
                mEdtUserName.getText().toString().trim().isEmpty()){
            showMessage("Error","Please Enter Username and Password");
        }
        else {
            /*Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM UserDetails WHERE " +
                            "username=" + mEdtUserName.getText().toString().trim() + "" +
                            " AND password=" + mEdtPassword.getText().toString().trim()+"",
                    null);*/
            cursor = sqLiteDatabase.query(UserContract.UserEntry.TABLE_NAME,null," "+ UserContract.UserEntry.COLUMN_USERNAME + "=?", new String[]{mEdtUserName.getText().toString().trim()},null,null,null);
            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst();
                    loginUserName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USERNAME));
                    loginPassword = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
                    cursor.close();
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.putExtra("key_name", loginUserName);

                    startActivity(intent);
                    clearText();
                } else {
                    showMessage("Error", "Username and Password does not match");
                    clearText();
                }
            }
        }
    }
    public void clearText()
    {
        mEdtUserName.setText("");
        mEdtPassword.setText("");
    }

    public void showMessage(String title,String message)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}