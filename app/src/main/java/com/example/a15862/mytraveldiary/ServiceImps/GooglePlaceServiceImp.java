package com.example.a15862.mytraveldiary.ServiceImps;

import com.example.a15862.mytraveldiary.DAO.GooglePlaceDao;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.Services.GooglePlaceService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GooglePlaceServiceImp implements GooglePlaceService {
    private GooglePlaceDao gd;
    public GooglePlaceServiceImp(){
        this.gd = new GooglePlaceDao();
    }
    @Override
    public List<Place> searchPlace(LatLng location, double radius) {
        List<Place> res = new ArrayList<>();
        try {
            JSONObject searchRes = gd.searchLocation(location,radius);
            try {
                //The candidate is the value of the key "Results" in JSON
                //for each location in result , get the longitue and latitude and the place name,use marker to draw them
                JSONArray array = searchRes.getJSONArray("results");
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject cur = array.getJSONObject(i);
//                    JSONObject jsonLoc = cur.getJSONObject("geometry").getJSONObject("location");
//                    LatLng loc = new LatLng(jsonLoc.getDouble("lat"), jsonLoc.getDouble("lng"));
//                    String name = cur.getString("name");
//                }
                System.out.println(array.get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


}
