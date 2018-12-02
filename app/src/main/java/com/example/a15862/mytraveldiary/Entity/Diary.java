package com.example.a15862.mytraveldiary.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Diary implements Serializable{
    private static final long serialVersionUID = -7634235178023352252L;
    private String username =null;
    private String diaplayName;
    private String placeID=null;
    private String photoUri=null;
    private String imgWeather=null;
    private String txtDate=null, txtCity=null, txtTemperature=null;
    private String edtDiary=null;
    private long time;

    public Diary(){
        time=System.currentTimeMillis();
    }
    private String id;
    public Diary(String username, String placeID){
        this.username = username;
        this.placeID=placeID;
        time=System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDiaplayName() {
        return diaplayName;
    }

    public void setDiaplayName(String diaplayName) {
        this.diaplayName = diaplayName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String,Object> toMap(){
        Map<String, Object> map=new HashMap<>();
        if (username !=null){
            map.put("userid", username);
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
