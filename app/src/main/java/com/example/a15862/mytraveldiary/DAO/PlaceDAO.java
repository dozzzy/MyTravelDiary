package com.example.a15862.mytraveldiary.DAO;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class PlaceDAO {
    private FirebaseFirestore db;


    public PlaceDAO(){
        db = FirebaseFirestore.getInstance();
    }

//    public void insertPlace(){
//
//    }
//
//    public List<Place> searchPlaceFromDatabase(){
//
//    }
//
//    public List<Place> searchPlaceFromApi(Location location){
//
//    }


}
