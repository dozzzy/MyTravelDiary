package com.example.a15862.mytraveldiary;

import org.json.JSONException;
import org.json.JSONObject;

public interface placeInfoReceiver {
    //Interface that enables Main-activity to communicate with Services Object
    void receive(JSONObject res) throws JSONException;

    void receiveComment(JSONObject res) throws JSONException;

    void receiveKeySearch(JSONObject res) throws JSONException;
}
