package com.example.a15862.mytraveldiary.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.User;
import com.example.a15862.mytraveldiary.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private FirebaseFirestore db;


    public UserDAO(){
        db = FirebaseFirestore.getInstance();
    }

    public void addBasicUser(User user){
        Map<String,Object> m = new HashMap<>();
        m.put("username",user.getUsername());
        m.put("password",user.getPassword());
        m.put("displayName",user.getDisplayName());
        db.collection("User").document(user.getUsername()).set(m);
    }

    public void saveUserInApp(User user, Context activity){

        SharedPreferences saveUser = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=saveUser.edit();
        editor.putString("username",user.getUsername());
        editor.putString("displayName",user.getDisplayName());

    }

    public User loadUserInApp( Context activity){
        User loadUser=new User();
        SharedPreferences  load = PreferenceManager.getDefaultSharedPreferences(activity);
        loadUser.setDisplayName(load.getString("displayName", "DEFAULT"));
        loadUser.setUsername(load.getString("username","DEFAULT"));
        return loadUser;
    }


    public void est(){
        db.collection("User").whereArrayContains("username","178").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.i("Jing","in");
                for(QueryDocumentSnapshot q:queryDocumentSnapshots){
                    Log.i("Jing",q.get("displayName").toString());
                }
            }
        });
    }



}
