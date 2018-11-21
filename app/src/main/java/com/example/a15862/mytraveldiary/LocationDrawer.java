package com.example.a15862.mytraveldiary;

import org.json.JSONException;
import org.json.JSONObject;

public interface LocationDrawer {
    //Interface that enables Main-activity to communicate with Services Object
    void draw(JSONObject res) throws JSONException;
}
