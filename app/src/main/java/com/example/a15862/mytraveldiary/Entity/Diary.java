package com.example.a15862.mytraveldiary.Entity;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diary {
    private String userID;
    private String placeID;
    private String photoUri;
    private String imgWeather;
    private String txtDate, txtCity, txtTemperature;
    private String edtDiary;

    public Diary(){}
    public Diary(String userID,String placeID){
        this.userID=userID;
        this.placeID=placeID;
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

    public Map<String,Object> toMap(){
        Map<String, Object> map=new HashMap<>();
        if (userID!=null){
            map.put("userid",userID);
        }
        if (placeID!=null){
            map.put("placeid",placeID);
        }
        if (photoUri!=null){
            map.put("photoUri",photoUri);
        }
        if (imgWeather!=null){
            map.put("imgWeather",imgWeather);
        }
        if (txtDate!=null){
            map.put("txtDate",txtDate);
        }
        if (txtCity!=null){
            map.put("txtCity",txtCity);
        }
        if (txtTemperature!=null){
            map.put("txtTemperature",txtTemperature);
        }
        if (edtDiary!=null){
            map.put("edtDiary",edtDiary);
        }
        return map;
    }
}
