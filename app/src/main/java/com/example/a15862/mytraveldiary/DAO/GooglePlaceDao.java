package com.example.a15862.mytraveldiary.DAO;

import android.os.Handler;
import android.os.Message;

import com.example.a15862.mytraveldiary.Services.GooglePlaceService;
import com.example.a15862.mytraveldiary.Services.SearchServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GooglePlaceDao extends Handler {
    static private String key = "&key=AIzaSyC1qPnWqt7G0areqx3sDhdElU04b0HTr6A";
    private JSONObject jsonRes = null;

    public JSONObject searchLocation(LatLng location, double radius) throws Exception {
        //generate the query URL based on the input parameter
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?language=en&";
        url += "location=" + location.latitude + "," + location.longitude + "&radius=" + radius;
        url += key;
        return getJson(url);
    }

    public JSONObject getComment(String placeId) throws Exception {
        //if the id is less than 25 , is a self-defined place , we don't have to search it
        if (placeId.length() < 25) return null;
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&fields=name,rating,formatted_phone_number" + key;
        return getJson(url);
    }

    public JSONObject getJson(final String strUrl) throws Exception {
        //create a sub-thread to handle the network IO task
        new Thread() {
            private HttpURLConnection connection;

            public void run() {
                try {
                    URL url = new URL(strUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    //Open the connection and set the timeout,set the method tobe "GET"
                    connection.setConnectTimeout(5 * 1000);
                    connection.setRequestMethod("GET");
                    InputStream in = connection.getInputStream();
                    StringBuilder builder = new StringBuilder();
                    //use the bufferreader to read the InputStream
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    //Create the object based on the entity of HttpResponse
                    JSONObject jsonObject = new JSONObject(builder.toString());
                    //Create a message , contains the status number and the JSONObject
                    Message msg = new Message();
                    msg.obj = jsonObject;
                    msg.what = 1;
                    sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
        while (jsonRes == null) ;
        return jsonRes;
    }

    @Override
    public void handleMessage(Message msg) {
        //When receive msg, means the IO operation is finished , ask main-UI to draw
        super.handleMessage(msg);
        if (msg.what == 1) {
            jsonRes = (JSONObject) msg.obj;
        }
    }
}
