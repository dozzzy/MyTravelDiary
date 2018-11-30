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

    public DBOP() {
        db = FirebaseFirestore.getInstance();
    }

    public void insertData(Object insertTest,String tablename) {
        db.collection(tablename).document()
                .set(insertTest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("TuZ","insert success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("my", "somehow not work");
            }
        });
    }
    public void insertData(Map<String,Object> insertTest,String tablename) {
        db.collection(tablename).document()
                .set(insertTest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("TuZ","insert success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("my", "somehow not work");
            }
        });
    }

    public void readSingleContract() {
        DocumentReference contact = db.collection("FirstTry").document("firstDoc");

        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                StringBuilder data = new StringBuilder("");
                DocumentSnapshot doc = task.getResult();
                data.append(doc.get("int"));
            }
        });
    }

    public void readObject() {
        DocumentReference contact = db.collection("FirstTry").document("firstDoc");
        contact.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //User user=documentSnapshot.toObject(User.class);
                StringBuilder data = new StringBuilder("");
            }
        });
    }

    public void updateData() {
        DocumentReference contact = db.collection("FirstTry").document("firstDoc");
        contact.update("string", "new String").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("my", "update");
            }
        });
    }

    public void delete() {
        db.collection("FirstTry").document("firstDoc").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("my", "delete");
            }
        });
    }

    public void realtime() {
        DocumentReference contact = db.collection("FirstTry").document("firstDoc");
        contact.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("my", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.e("my", "realtime");
                }
            }
        });
    }

//        private void findUserInDB(User u){
//        db.collection("FirstTry").whereEqualTo("username",u.getUsername()).whereEqualTo("password",u.getPassword())
//            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()){
//                    Toast.makeText(MainActivity.this,"error with username or password",Toast.LENGTH_SHORT).show();
//                }else {
//                    for(DocumentSnapshot d:queryDocumentSnapshots){
//                        user=d.toObject(User.class);
//                    }
//                }
//            }
//        });
//    }

}
