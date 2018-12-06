package com.example.a15862.mytraveldiary.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.example.a15862.mytraveldiary.Entity.User;
import com.example.a15862.mytraveldiary.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private FirebaseFirestore db;
    private User uploadUser;
    private StorageReference mStorageRef;
    private StorageTask sk;

    public UserDAO(){
        db = FirebaseFirestore.getInstance();
        mStorageRef=FirebaseStorage.getInstance().getReference();
    }

    public void addBasicUser(User user){
        Map<String,Object> m = new HashMap<>();
        m.put("username",user.getUsername());
        m.put("password",user.getPassword());
        m.put("displayName",user.getDisplayName());
        m.put("avater",user.getAvatar());
        db.collection("User").document(user.getUsername()).set(m);
    }


    public void uploadUser(final User user){
        uploadUser =user;
        if (user.getAvatar()!=null){
            Uri uri=Uri.parse(user.getAvatar());
            // if we have photo to upload we upload photo first, and retrieve the url of photo. then save the url with other things(username, comments....) in firestore
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." +uri);
            // sk record upload task
            sk=fileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // in the past getDownloadUrl can retrieve the url in no time. But now the call can be asynchronous, so we need to use an listener
                                    // after we upload photo to storage, we restore the url of photo and other stuff to firestore
                                    Log.i("upload User","photo in storage");
                                    uploadUser.setAvatar(uri.toString());
                                    db.collection("User").document(uploadUser.getUsername()).set(uploadUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("upload User","photo succ");
                                        }
                                    });
                                }
                            });

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("my",e.getMessage());
                        }
                    });
        } else {
            db.collection("User").document(uploadUser.getUsername()).set(uploadUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("jingD","no photo succ");
                }
            });
        }
    }




    public void updateLike(final String username){
        db.collection("User").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                u.setLike(u.getLike()+1);
                db.collection("User").document(username).set(u);
            }
        });
    }



}
