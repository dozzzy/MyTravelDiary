package com.example.a15862.mytraveldiary.DAO;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class DBOP {
    private FirebaseFirestore db;
    public DBOP(){
        db= FirebaseFirestore.getInstance();
    }

    private void insertData(Map<String,Object> insertTest){
        db.collection("FirstTry").document("firstDoc")
                .set(insertTest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("my","somehow not work");
            }
        });
    }
    private void readSingleContract(){
        DocumentReference contact=db.collection("FirstTry").document("firstDoc");

        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                StringBuilder data=new StringBuilder("");
                DocumentSnapshot doc=task.getResult();
                data.append(doc.get("int"));
            }
        });
    }
    private void readObject(){
        DocumentReference contact=db.collection("FirstTry").document("firstDoc");
        contact.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //User user=documentSnapshot.toObject(User.class);
                StringBuilder data=new StringBuilder("");
            }
        });
    }
    private void updateData(){
        DocumentReference contact=db.collection("FirstTry").document("firstDoc");
        contact.update("string","new String").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("my","update");
            }
        });
    }
    private void delete(){
        db.collection("FirstTry").document("firstDoc").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("my","delete");
            }
        });
    }
    private void realtime(){
        DocumentReference contact=db.collection("FirstTry").document("firstDoc");
        contact.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e("my",e.getMessage());
                    return;
                }
                if (documentSnapshot!=null && documentSnapshot.exists()){
                    Log.e("my","realtime");
                }
            }
        });
    }


}
