package com.example.a15862.mytraveldiary.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.a15862.mytraveldiary.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FollowshipDAO {
    List<String> newF;
    private FirebaseFirestore db;

    public FollowshipDAO(){
        newF = null;
        db = FirebaseFirestore.getInstance();
    }

    public void follow(final String curUser, final String target){
        db.collection("Followship").document(curUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData()!=null) newF = (ArrayList)documentSnapshot.getData().get("followed");
                else newF = new ArrayList<>();
                Map<String,Object> data = new HashMap<>();
                if(!newF.contains(target)) newF.add(target);
                data.put("followed",newF);
                db.collection("Followship").document(curUser).set(data);
            }
        });

    }

}
