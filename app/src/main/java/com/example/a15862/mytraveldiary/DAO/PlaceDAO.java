package com.example.a15862.mytraveldiary.DAO;

import android.content.Intent;
import android.util.Log;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class PlaceDAO {
    private FirebaseFirestore db;

    public PlaceDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public void addPlace(Place p) {
        Map<String, Object> data = new HashMap<>();
        data.put("latitude", p.getLatitude());
        data.put("longitude", p.getLongitude());
        data.put("pid", p.getPid());
        data.put("placeName", p.getPlaceName());
        data.put("photoPath", p.getPhotoPath());
        data.put("vicinity", p.getVicinity());
        data.put("category", p.getCategory());
        data.put("scoreCount", p.getScoreCount());
        data.put("totalScore", p.getTotalScore());
        data.put("totalComment", p.getTotalComment());
        db.collection("Place").document(p.getPlaceName()).set(data);
    }

    public void updateData(Place p) {
        CollectionReference cr = db.collection("Place");
        ;
        cr.document(p.getPlaceName()).update("totalScore", p.getTotalScore());
        cr.document(p.getPlaceName()).update("scoreCount", p.getScoreCount());
        cr.document(p.getPlaceName()).update("totalComment", p.getTotalComment());
    }


}
