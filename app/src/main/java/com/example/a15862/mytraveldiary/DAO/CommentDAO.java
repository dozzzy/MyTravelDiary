package com.example.a15862.mytraveldiary.DAO;

import com.example.a15862.mytraveldiary.Entity.Comment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CommentDAO {
    private FirebaseFirestore db;
    public CommentDAO(){
        db = FirebaseFirestore.getInstance();
    }
    public void addComment(Comment c){
        Map<String,Object> data = new HashMap<>();
        data.put("placeName",c.getPlaceName());
        data.put("userName",c.getUsername());
        data.put("userComment",c.getUserComment());
        db.collection("Comment").document(c.getUsername()+"."+c.getTime()).set(data);
    }
}
