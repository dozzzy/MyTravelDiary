package com.example.a15862.mytraveldiary.Entity;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private String placeID;
    private String userID;
    private String userComment;
    public Comment(){}
    public Comment(String userID,String placeID,String userComment){
        this.placeID=placeID;
        this.userID=userID;
        this.userComment=userID;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Map<String,Object> toMap(){
        Map<String, Object> map=new HashMap<>();
        if (placeID!=null){
            map.put("placeid",placeID);
        }
        if (userID!=null){
            map.put("userid",userID);
        }
        if (userComment!=null){
            map.put("userComment",userComment);
        }
        return map;
    }
}