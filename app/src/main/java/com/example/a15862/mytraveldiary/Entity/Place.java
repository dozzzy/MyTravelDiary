package com.example.a15862.mytraveldiary.Entity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Serializable{

    private static final long serialVersionUID = -7620435178023928252L;

    private double latitude;
    private double longitude;
    private int scoreCount = 0;
    private float totalScore = 0;
    private String placeName;
    private String vicinity;
    private String photoPath;
    private List<String> catagory;

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public float getTotalScore() {
        return totalScore;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void addScore(float n){
        scoreCount++;
        totalScore+=n;
    }
    private String pid;

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

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public List<String> getCatagory() {
        return catagory;
    }

    public void setCatagory(List<String> catagory) {
        this.catagory = catagory;
    }

    public Place(LatLng location, String name, String address, String pid){
        this.longitude = location.longitude;
        this.latitude = location.latitude;
        placeName = name;
        vicinity = address;
        catagory = new ArrayList<>();
        this.pid = pid;
    }

    public Place(){
        catagory = new ArrayList<>();
    };


    public String print(){
        return ("Name:" +placeName +"Location:"+latitude+","+longitude +"vicinity:"+vicinity+"photoPath:"+photoPath );
    }
}