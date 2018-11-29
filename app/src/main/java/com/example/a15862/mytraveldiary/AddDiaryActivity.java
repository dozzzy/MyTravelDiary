package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.Retrofit.IOpenWeatherMap;
import com.example.a15862.mytraveldiary.Retrofit.RetrofitClient;
import com.example.a15862.mytraveldiary.WeatherModel.WeatherResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDiaryActivity extends Activity {

    private Button btnCamera, btnGallery;
    private final int IMAGE_RESULT_CODE = 2;// 表示打开照相机
    private final int PICK = 1;// 选择图片库
    public static final int SPEECH_TO_TEXT = 9998;
    public static final int SAVE = 9997;

    private ImageView imgWeather, imgPhoto;
    private TextView txtDate, txtCity, txtTemperature;
    private TextView edtDiary;
    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;

    private Button btnClear, btnSave;
    private ImageButton btnSpeech2Text, btnTakePicture;

    private Uri photoUri; // for photos
    private int photoCnt;
    private String timeStamp;
    private String cityLoc;
    private String diaryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IMAGE_RESULT_CODE);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK);
            }
        });


        getWeatherInfo();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDiary.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveDiary(diaryName);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 表示 调用照相机拍照
            case IMAGE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imgPhoto.setImageBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                }


                break;
            // 选择图片库的图片
            case PICK:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    imgPhoto.setImageURI(uri);
                }
                break;
        }

    }

    /**
     * get the weather information
     **/
    private void getWeatherInfo() {
        // establish the http request
        Retrofit retrofit = RetrofitClient.getInstance();
        // that query for weather information in the format given in the interface of IOpenWeatherMap
        mService = retrofit.create(IOpenWeatherMap.class);

        Bundle info = getIntent().getExtras();
        double lat = info.getDouble("CurrentLatitude");
        double lon = info.getDouble("CurrentLongitude");

        Call<WeatherResult> model = mService.getWeatherByLatLng(
                lat, lon, Helper.API_KEY_WEATHER, "metric");
        model.enqueue(new Callback<WeatherResult>() {
            // if success
            @Override
            public void onResponse(@NonNull Call<WeatherResult> call, @NonNull Response<WeatherResult> response) {

                // Picasso loads the image, in this case the weather icon from the website,
                // to the ImageView imgWeather
                Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                        .append(response.body().getWeather().get(0).getIcon())
                        .append(".png").toString()).into(imgWeather);

                // Set corresponding TextView to the information retrieved
                //TODO: change City to the location retrieved from the map
                txtCity.setText(response.body().getName());
                txtTemperature.setText(new StringBuilder("The current temperature is ")
                        .append(String.valueOf(response.body().getMain().getTemp())).append("°C").toString());
                txtDate.setText(Helper.convertUnixToDate(response.body().getDt()));
            }

            // if fail
            @Override
            public void onFailure(@NonNull Call<WeatherResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * Create a file Uri for saving an image or video
     **/
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        String path;

        // check if the sd card is mounted
        boolean sdCardMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardMounted) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyTravelDiary");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("qwer", "failed to create directory");
                    return null;
                }
            }

            path = mediaStorageDir.getPath();

        } else {
            // no external sd card
            Log.e("qwer", "Cannot save photo. No external storage detected");
            // use the internal storage
            //path = getFilesDir();
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(path + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private void saveDiary(String diary) {

    }
}