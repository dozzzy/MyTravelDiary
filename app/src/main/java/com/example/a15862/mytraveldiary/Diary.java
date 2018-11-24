package com.example.a15862.mytraveldiary;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.Retrofit.IOpenWeatherMap;

import io.reactivex.disposables.CompositeDisposable;

public class Diary {
    private ImageView imgWeather, imgPhoto;
    private String txtDate, txtCity, txtTemperature;
    private EditText edtTitle, edtDiary;

    public Diary(String txtDate) {
        this.txtDate = txtDate;
    }

    public ImageView getImgWeather() {
        return imgWeather;
    }

    public void setImgWeather(ImageView imgWeather) {
        this.imgWeather = imgWeather;
    }

    public ImageView getImgPhoto() {
        return imgPhoto;
    }

    public void setImgPhoto(ImageView imgPhoto) {
        this.imgPhoto = imgPhoto;
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

    public EditText getEdtTitle() {
        return edtTitle;
    }

    public void setEdtTitle(EditText edtTitle) {
        this.edtTitle = edtTitle;
    }

    public EditText getEdtDiary() {
        return edtDiary;
    }

    public void setEdtDiary(EditText edtDiary) {
        this.edtDiary = edtDiary;
    }


}
