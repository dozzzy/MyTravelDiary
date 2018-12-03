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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class LoginNewActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtPassword;
    private ImageButton btnPhoneLogin;
    private ImageButton btnEmailLogin;
    private Button btnLogin;
    private FirebaseFirestore db;
    private String TAG="LoginTT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        db=FirebaseFirestore.getInstance();
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtUserName=(EditText)findViewById(R.id.edtUserName);
        btnEmailLogin=(ImageButton)findViewById(R.id.btnEmailLogin);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnPhoneLogin=(ImageButton)findViewById(R.id.btnPhoneLogin);

        btnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register2Activity.class);
                startActivity(intent);
            }
        });
        btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register3Activity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edtUserName.getText().toString();
                String password=edtPassword.getText().toString();
                Log.e(TAG,"btnlogin click");
                db.collection("User").whereEqualTo("username",username).whereEqualTo("password",password)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()){
                        Toast.makeText(getApplicationContext(),"username or password wrong", Toast.LENGTH_SHORT);
                }else {
                        for(DocumentSnapshot d:queryDocumentSnapshots){
                            User user=d.toObject(User.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

                            editor.putString("displayName", user.getDisplayName());

                            editor.putString("username",user.getUsername());

                            editor.commit();


                            Intent i = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(i);
                        }
                    }
                }
        });
            }
        });
    }
}