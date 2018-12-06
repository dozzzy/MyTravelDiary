package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewAllFriendsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_friends);
        db=FirebaseFirestore.getInstance();
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username=load.getString("username","DEFAULT");
        String displayName=load.getString("displayName","DEFAULT");

    }
}
