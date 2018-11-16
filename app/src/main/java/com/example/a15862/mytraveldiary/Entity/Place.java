package com.example.a15862.mytraveldiary.Entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Place {
    LatLng location;
    String placeName;
    double rate;
    List<String> comments;
    String formatAddress;
    public Place(LatLng location,String name,double rate,String address){
        this.location = location;
        placeName = name;
        this.rate = rate;
        formatAddress = address;
        comments = new ArrayList<>();
    }
}
