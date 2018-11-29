package com.example.a15862.mytraveldiary.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diary {
    private String userID;
    private String placeID;
    private List<String> imageUrl;
    public Diary(){}
    public Diary(String userID,String placeID,List<String> imageUrl){
        this.userID=userID;
        this.placeID=placeID;
        this.imageUrl=imageUrl;
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

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Map<String,Object> toMap(){
        Map<String, Object> map=new HashMap<>();
        if (userID!=null){
            map.put("userid",userID);
        }
        if (placeID!=null){
            map.put("placeid",placeID);
        }
        if (imageUrl!=null){
            map.put("imageUrl",imageUrl);
        }
        return map;
    }
}
