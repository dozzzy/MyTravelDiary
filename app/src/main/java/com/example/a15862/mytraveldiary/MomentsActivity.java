package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MomentsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    List<Diary> diarys = new ArrayList<>();
    List<String> friends;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_moments);
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username=load.getString("username","DEFAULT");
        db = FirebaseFirestore.getInstance();
        db.collection("Followship").document("Jing").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                friends = (ArrayList)documentSnapshot.getData().get("followed");
                count = friends.size();

                for(String cur:friends){
                    Log.i("Jing",cur);
                    db.collection("Diary").whereEqualTo("username",cur).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            count--;
                            for(QueryDocumentSnapshot qs:queryDocumentSnapshots){
                                Diary d = qs.toObject(Diary.class);
                                diarys.add(d);
                            }
                            if(count == 0){
                                Log.i("Diary","____");
                                Collections.sort(diarys, new Comparator<Diary>() {
                                    @Override
                                    public int compare(Diary o1, Diary o2) {
                                        long l2 = o1.getTime();
                                        long l1 = o2.getTime();
                                        if(l1-l2>0) return 1;
                                        else if(l1==l2) return 0;
                                        else return -1;
                                    }
                                });
                                //TODO:The diarys stored all recent diary of friends , sorted by time
                                for(Diary c:diarys){
                                    Log.i("Diary",c.getEdtDiary());
                                    Log.i("Diary",String.valueOf(c.getTime()));
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            count--;
                        }
                    });
                }
            }
        });
    }
}
