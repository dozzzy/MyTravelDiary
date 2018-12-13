package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.User;

public class Register1Activity extends AppCompatActivity {
    // username and password register
    private EditText edtUserName;
    private EditText edtNickName;
    private EditText edtPassword;
    private Button btnRegister;
    private Button btnAvatar;
    private ImageView imgAvatar;
    private EditText edtCheck;
    private int PICK = 123;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u = new User();
        setContentView(R.layout.activity_register1);
        edtNickName = findViewById(R.id.edtNickName);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        imgAvatar = findViewById(R.id.imgAvater);
        edtCheck = findViewById(R.id.edtCheck);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = edtNickName.getText().toString();
                String username = edtUserName.getText().toString();
                String psw = edtPassword.getText().toString();
                String check = edtCheck.getText().toString();
                if (!psw.equals(check)) {
                    Toast.makeText(getApplicationContext(), "check your password", Toast.LENGTH_SHORT).show();
                } else if (psw.equals("") || username.equals("") || displayName.equals("")) {
                    Toast.makeText(getApplicationContext(), "can not fill blank", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Jing", "click");
                    u.setDisplayName(displayName);
                    u.setPassword(psw);
                    u.setUsername(username);
                    // set a default avatar for user
                    u.setAvatar("https://firebasestorage.googleapis.com/v0/b/mytraveldiary-d8885.appspot.com/o/avater.png?alt=media&token=fae2ef71-2350-4237-98f3-2a51be9ccb03");
                    UserDAO ud = new UserDAO();
                    ud.addBasicUser(u);
                    // put user information into sharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("displayName", displayName);
                    editor.putString("username", username);
                    editor.commit();
                    Intent intent = new Intent(Register1Activity.this, LoginNewActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                imgAvatar.setImageURI(uri);
                u.setAvatar(uri.toString());
            }
        }

    }

}
