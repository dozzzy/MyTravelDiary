package com.example.a15862.mytraveldiary.Services;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface SearchServices {
    void searchLocation(LatLng location, double radius) throws Exception;

    void getComment(String pid) throws Exception;
}
