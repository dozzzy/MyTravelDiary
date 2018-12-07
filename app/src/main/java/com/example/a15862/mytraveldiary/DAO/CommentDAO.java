package com.example.a15862.mytraveldiary.DAO;

import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CommentDAO {
    private FirebaseFirestore db;
    private Comment pointer = null;
    public CommentDAO(){
        db = FirebaseFirestore.getInstance();
    }
    public void addComment(Comment c,int fromAPI){
        Map<String,Object> data = new HashMap<>();
        data.put("placeName",c.getPlaceName());
        data.put("username",c.getUsername());
        data.put("userComment",c.getUserComment());
        data.put("fromAPI",fromAPI);
        data.put("time",c.getTime());
        db.collection("Comment").document(c.getUsername()+"."+c.getTime()).set(data);
    }

    public void addLike(Comment c){
        pointer = c;
        db.collection("Comment").whereEqualTo("userComment",c.getUserComment()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot qs:queryDocumentSnapshots){
                    Map<String,Object> data = qs.getData();
                    data.put("like",(int)data.get("like")+1);
                    db.collection("Comment").document(pointer.getUsername()+"."+pointer.getTime()).set(data);
                    pointer = null;
                }
            }
        });
    }
}
