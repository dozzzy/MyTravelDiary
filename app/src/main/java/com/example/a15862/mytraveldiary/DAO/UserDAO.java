package com.example.a15862.mytraveldiary.DAO;

import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private FirebaseFirestore db;


    public UserDAO(){
        db = FirebaseFirestore.getInstance();
    }

    public void addBasicUser(User user){
        Log.i("Jing","oncall");
        Map<String,Object> m = new HashMap<>();
        m.put("username",user.getUsername());
        m.put("password",user.getPassword());
        m.put("displayName",user.getDisplayName());
        Log.i("Jing","Dao");
        db.collection("User").document(user.getUsername()).set(m);
    }

}
