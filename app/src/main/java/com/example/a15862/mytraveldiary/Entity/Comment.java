package com.example.a15862.mytraveldiary.Entity;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    private String placeName;
    private User user;
    private String username;
    private String userComment;
    private int fromAPI;
    private long time;
    public Comment(){
        time=System.currentTimeMillis();
    }
    public Comment(String userID, String placeName, String userComment){
        this.placeName=placeName;
        this.username=userID;
        this.userComment=userComment;
        time=System.currentTimeMillis();
    }

    public int getFromAPI() {
        return fromAPI;
    }

    public void setFromAPI(int fromAPI) {
        this.fromAPI = fromAPI;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeID) {
        this.placeName = placeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userID) {
        this.username = userID;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

}