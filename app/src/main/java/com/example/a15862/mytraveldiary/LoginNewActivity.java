package com.example.a15862.mytraveldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class LoginNewActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnPhoneLogin;
    private Button btnEmailLogin;
    private Button btnLogin;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        db=FirebaseFirestore.getInstance();
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtUserName=(EditText)findViewById(R.id.edtUserName);
        btnEmailLogin=(Button)findViewById(R.id.btnEmailLogin);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnPhoneLogin=(Button)findViewById(R.id.btnPhoneLogin);

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
                db.collection("user").whereEqualTo("username",username).whereEqualTo("password",password)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()){
                }else {
                        for(DocumentSnapshot d:queryDocumentSnapshots){
                            User user=d.toObject(User.class);
                        }
                    }
                }
        });
            }
        });
    }
}
