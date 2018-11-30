package com.example.a15862.mytraveldiary.DAO;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlaceDAO {
    private FirebaseFirestore db;
    public PlaceDAO(){
        db = FirebaseFirestore.getInstance();

    }

    public void addPlace(Place p){
        db.collection("Place").add(p);
    }
}
