package com.example.a15862.mytraveldiary.Entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Place {
    double latitude;
    double longitude;
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    String pid;

    public double getLatitude() {
        return latitude;
    }

    public void setLocation(LatLng location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        this.longitude = location.longitude;
        this.latitude = location.latitude;
        placeName = name;
        vicinity = address;
        comments = new ArrayList<>();
        this.pid = pid;
    }

    public Place(){
    };


    public String print(){
        return ("Name:" +placeName +"Location:"+latitude+","+longitude +"vicinity:"+vicinity+"photoPath:"+photoPath );
    }
}
