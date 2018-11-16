package com.example.a15862.mytraveldiary.Services;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface GooglePlaceService {
    List<Place> searchPlace(LatLng location, double radius);
}
