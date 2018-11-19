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
    private String txtDate, txtCity, txtWeather;
    private EditText edtTitle, edtDiary;
    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;

    private Button btnClear, btnSave;
    private ImageButton btnSpeech2Text, btnTakePicture;

}
