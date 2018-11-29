package com.example.a15862.mytraveldiary.Entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Place {
    LatLng location;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    String pid;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String formatAddress) {
        this.vicinity = formatAddress;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    String placeName;
    double rate;
    List<String> comments;
    String vicinity;
    String photoPath;
    public Place(LatLng location,String name,String address,String pid){
        this.location = location;
        placeName = name;
        vicinity = address;
        comments = new ArrayList<>();
        this.pid = pid;
    }


    public String print(){
        return ("Name:" +placeName +"Location:"+location +"vicinity:"+vicinity+"photoPath:"+photoPath );
    }
}
