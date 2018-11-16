package com.example.ivc.mtd_p1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {

    public static final String API_KEY_WEATHER = "ab883506df2f0540d750749451be66a5";
    public static final String CLOUD_API_KEY = "AIzaSyCS8evwqgbJ7p7zp7bzEJNe9WVQn0Kg36Q";
    public static Location current_location = null;

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static boolean isCameraAvailable(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static boolean isMicAvailable(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }


    // get the system time and set the format
    public static String convertUnixToDate(int dt) {
        //Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, EEE 'at' HH:mm:ss z");
        String date = sdf.format(Calendar.getInstance().getTime());
        return date;
    }
}
