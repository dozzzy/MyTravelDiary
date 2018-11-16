package com.example.a15862.mytraveldiary.Retrofit;

import com.example.a15862.mytraveldiary.WeatherModel.WeatherResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;

public interface IOpenWeatherMap {
    // use location (latitude, longitude) to get weather information
    // appid is the API_KEY of OpenWeatherMap
    // units decides if the returned temperature to be displayed in Celsius or Fahrenheit. We will use Celsius
    @GET("weather")
    Call<WeatherResult> getWeatherByLatLng(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("appid") String appid,
                                           @Query("units") String unit);
}
