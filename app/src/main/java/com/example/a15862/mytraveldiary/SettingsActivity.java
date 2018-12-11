package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private EditText txtNickName;
    private EditText edtPassword;
    private EditText edtCheck;
    private Button btnSave;
    private Button btnChangePhoto;
    private ImageView imgLogo;
    private User user;
    private FirebaseFirestore db;
    private int PICK=123;
    private String avatar;
    private String displayName;
    private String username;
    private Boolean photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db=FirebaseFirestore.getInstance();
        Log.e("qwer","setting oncreate");
        txtNickName=(EditText) findViewById(R.id.txtNickName);
        edtPassword=(EditText) findViewById(R.id.edtPassword);
        edtCheck=(EditText) findViewById(R.id.edtCheck);
        btnSave=(Button) findViewById(R.id.btnSave);
        btnChangePhoto=(Button) findViewById(R.id.btnChangePhoto);
        imgLogo=(ImageView) findViewById(R.id.imgLogo);
        photo=false;
        SharedPreferences load = getSharedPreferences("user",Context.MODE_PRIVATE);
        displayName=load.getString("displayName", "DEFAULT");
        username=load.getString("username","DEFAULT");
        avatar=load.getString("avatar","DEFAULT");
        Log.e("qwer",avatar);
        db.collection("User").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user=documentSnapshot.toObject(User.class);
            }
        });

        txtNickName.setText(displayName);
        edtPassword.setText("");
        edtCheck.setText("");
        if (avatar.equals("DEFAULT")){
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mytraveldiary-d8885.appspot.com/o/avater.png?alt=media&token=fae2ef71-2350-4237-98f3-2a51be9ccb03").into(imgLogo);
        } else {
            Log.e("qwer","picasso load photo");
            Picasso.get().load(avatar).into(imgLogo);
            Log.e("qwer","picasso load done");
        }
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo=true;
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtCheck.getText().toString().equals(edtPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),"check your password", Toast.LENGTH_SHORT).show();
                }else{
                    if (!edtCheck.getText().toString().equals("")){
                        user.setPassword(edtPassword.getText().toString());
                    }
                    if (!txtNickName.getText().toString().equals("")){
                        user.setDisplayName(txtNickName.getText().toString());
                    }
                    UserDAO udb=new UserDAO();
                    if (photo){
                        udb.uploadUser(user);
                    }else{
                        udb.updateUserWithoutPhoto(user);
                    }
                    db.collection("User").document(user.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("avatar", user.getAvatar());
                            editor.putString("displayName",user.getDisplayName());
                            editor.commit();
                            Intent intent=new Intent(SettingsActivity.this,MapActivity.class);
                            startActivity(intent);
                        }
                    });
                }
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
                Log.e("qwer","imgLogo set");
                imgLogo.setImageURI(uri);
                Log.e("qwer","imgLogo set done");
                user.setAvatar(uri.toString());
            }
        }
    }
}
