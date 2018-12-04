package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.User;

public class Register1Activity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtNickName;
    private EditText edtPassword;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        edtNickName = findViewById(R.id.edtNickName);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayName = edtNickName.getText().toString();
                String username = edtUserName.getText().toString();
                String psw = edtPassword.getText().toString();
                Log.i("Jing","click");
                User u = new User();
                u.setDisplayName(displayName);
                u.setPassword(psw);
                u.setUsername(username);
                UserDAO ud = new UserDAO();
                ud.addBasicUser(u);

                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

                editor.putString("displayName", displayName);

                editor.putString("username",username);

                editor.commit();


                Intent intent = new Intent(Register1Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}
