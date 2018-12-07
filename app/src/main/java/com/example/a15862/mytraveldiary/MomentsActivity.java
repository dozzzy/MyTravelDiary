package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a15862.mytraveldiary.Entity.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MomentsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    List<Comment> comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_moments);
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        String displayName=load.getString("displayName", "DEFAULT");
        String username=load.getString("username","DEFAULT");
        db.collection("Followship").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });
    }
}
