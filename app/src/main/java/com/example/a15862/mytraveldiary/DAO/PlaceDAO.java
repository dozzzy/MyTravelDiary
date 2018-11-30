package com.example.a15862.mytraveldiary.DAO;

import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class PlaceDAO {
    private FirebaseFirestore db;
    public PlaceDAO(){
        db = FirebaseFirestore.getInstance();

    }

    public void addPlace(Place p){
        Map<String,Object> data = new HashMap<>();
        data.put("latitude",p.getLatitude());
        data.put("longitude",p.getLongitude());
        data.put("pid",p.getPid());
        data.put("comments",p.getComments());
        data.put("placeName",p.getPlaceName());
        data.put("photoPath",p.getPhotoPath());
        data.put("vicinity",p.getVicinity());
        data.put("rate",p.getRate());
        db.collection("Place").document(p.getPlaceName()).set(data);
    }

    public void updateData(Place p) {
        CollectionReference cr = db.collection("Place");
        Map<String,List<String>> m = new HashMap<>();
        m.put("comments",p.getComments());
        cr.document(p.getPlaceName()).update("Comment",p.getComments());
        cr.document(p.getPlaceName()).update("totalScore",p.getTotalScore());
        cr.document(p.getPlaceName()).update("scoreCount",p.getScoreCount());
    }
}