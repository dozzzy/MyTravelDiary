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
    private EditText edtUserName;
    private EditText edtNickName;
    private EditText edtPassword;
    private Button btnRegister;
    private Button btnAvatar;
    private ImageView imgAvatar;
    private int PICK=123;
    private User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        u=new User();
        setContentView(R.layout.activity_register1);
        edtNickName = findViewById(R.id.edtNickName);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        imgAvatar=findViewById(R.id.imgAvater);
        btnAvatar=findViewById(R.id.btnAvatar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = edtNickName.getText().toString();
                String username = edtUserName.getText().toString();
                String psw = edtPassword.getText().toString();
                Log.i("Jing","click");
                u.setDisplayName(displayName);
                u.setPassword(psw);
                u.setUsername(username);
                UserDAO ud = new UserDAO();
                //ud.addBasicUser(u);
                ud.uploadUser(u);
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("displayName", displayName);
                editor.putString("username",username);
                editor.commit();
                Intent intent = new Intent(Register1Activity.this, LoginNewActivity.class);
                startActivity(intent);
            }
        });
        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode==PICK){
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    imgAvatar.setImageURI(uri);
                    u.setAvatar(uri.toString());
                }
            }

        }

}
