package com.example.a15862.mytraveldiary.Entity;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diary implements Serializable{
    private static final long serialVersionUID = -7634235178023352252L;
    private String userID=null;
    private String placeID=null;
    private String photoUri=null;
    private String imgWeather=null;
    private String txtDate=null, txtCity=null, txtTemperature=null;
    private String edtDiary=null;
    private String id;
    public Diary(){}
    public Diary(String userID,String placeID){
        this.userID=userID;
        this.placeID=placeID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getImgWeather() {
        return imgWeather;
    }

    public void setImgWeather(String imgWeather) {
        this.imgWeather = imgWeather;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }

    public String getTxtCity() {
        return txtCity;
    }

    public void setTxtCity(String txtCity) {
        this.txtCity = txtCity;
    }

    public String getTxtTemperature() {
        return txtTemperature;
    }

    public void setTxtTemperature(String txtTemperature) {
        this.txtTemperature = txtTemperature;
    }

    public String getEdtDiary() {
        return edtDiary;
    }

    public void setEdtDiary(String edtDiary) {
        this.edtDiary = edtDiary;
    }

}
