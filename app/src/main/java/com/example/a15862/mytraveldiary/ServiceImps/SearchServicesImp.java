package com.example.a15862.mytraveldiary.ServiceImps;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.a15862.mytraveldiary.Services.SearchServices;
import com.example.a15862.mytraveldiary.placeInfoReceiver;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SearchServicesImp extends Handler implements SearchServices {
    static private String key = "&key=AIzaSyC1qPnWqt7G0areqx3sDhdElU04b0HTr6A";
    placeInfoReceiver context;

    public SearchServicesImp(placeInfoReceiver main){
        context = main;
    }
    @Override
    public void searchLocation(LatLng location, double radius) throws Exception {

        //generate the query URL based on the input parameter
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?language=en&";

        url+="location="+location.latitude+","+location.longitude +"&radius="+radius;
        url+=key;
        Log.i("Jing",url);
        getJson(url,0);
    }

    @Override
    public void getComment(String pid) throws Exception {
        //if the id is less than 25 , is a self-defined place , we don't have to search it
        if(pid.length()<25) return;
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+pid+"&fields=name,reviews"+key;
        Log.i("Jing",url);
        getJson(url,1);

    }

    @Override
    public void searchByName(LatLng location,String name,double radius) {
        //generate the query URL based on the input parameter
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?language=en&";
        url+="input="+name;
        url+="&location="+location.latitude+","+location.longitude +"&radius="+radius;
        url+=key;
        Log.i("Jing",url);
        try {
            getJson(url,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void getPhoto(String photoId) throws Exception {
//        String url = "https://maps.googleapis.com/maps/api/place/photo?&photoreference="+photoId;
//        url += key;
//        Log.i("Jing",url);
//        getJson(url,2);
//    }

    public void getJson(final String strUrl,final int action_code) throws Exception {
        //create a sub-thread to handle the network IO task
        new Thread(){
            private HttpURLConnection connection;
            public void run(){
                try{
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
                    msg.what = 1;
                    msg.obj = jsonObject;
                    msg.arg1 = action_code;
                    //send message back to handler
                    sendMessage(msg);
                }
                catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    @Override
    public void handleMessage(Message msg) {
        //When receive msg, means the IO operation is finished , ask main-UI to draw
        super.handleMessage(msg);
        try {
            int actionCoode = msg.arg1;
            //Call the method in mainActivity to draw the marker
            if(actionCoode == 0) context.receive((JSONObject)msg.obj);
            else if(actionCoode == 2){
                context.receiveKeySearch((JSONObject) msg.obj);
            }
            else context.receiveComment((JSONObject)msg.obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
