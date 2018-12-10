package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.DiaryDAO;
import com.example.a15862.mytraveldiary.Entity.Diary;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.Retrofit.IOpenWeatherMap;
import com.example.a15862.mytraveldiary.Retrofit.RetrofitClient;
import com.example.a15862.mytraveldiary.WeatherModel.WeatherResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ModifyActivity extends Activity {

    private ImageButton btnCamera, btnGallery;
    private final int IMAGE_RESULT_CODE = 2;// 表示打开照相机
    private final int PICK = 1;// 选择图片库
    private final int SPEECH_TO_TEXT = 3;
    public static final int SAVE = 9997;
    private Place currentPlace;
    private ImageView imgWeather, imgPhoto;

    private String imgWeatherUri;
    private Switch switchVisible;
    private TextView txtDate, txtCity, txtTemperature;
    private TextView edtDiary;

    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;
    private Button btnClear, btnSave;
    private Button btnSpeech2Text;

    private String PHOTO_FILE_NAME = "temp_photo.jpg";
    private Uri photoUri = null; // for photos
    private int photoCnt;
    private String timeStamp;
    private String cityLoc;
    private String diaryName;
    private Uri tempUri;
    private Diary curDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        Bundle info = getIntent().getExtras();
        curDiary = (Diary) info.getSerializable("Diary");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSpeech2Text = (Button) findViewById(R.id.btnSpeech2Text);
        switchVisible=(Switch)findViewById(R.id.switchVisible);
        if (switchVisible!=null){
            switchVisible.setChecked(curDiary.isVisible());
            switchVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        curDiary.setVisible(true);
                    } else {
                        curDiary.setVisible(false);
                    }
                }
            });
        }


        Picasso.get().load(curDiary.getImgWeather()).into(imgWeather);
        txtDate.setText(curDiary.getTxtDate());
        txtTemperature.setText(curDiary.getTxtTemperature());
        txtCity.setText(curDiary.getTxtCity());
        edtDiary.setText(curDiary.getEdtDiary());



        btnClear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                edtDiary.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                curDiary.setEdtDiary(edtDiary.getText().toString());
                db.collection("Diary").document(curDiary.getTime()+":"+curDiary.getUsername()).set(curDiary).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("jingD","no photo succ");
                        finish();
                    }
                });
            }
        });

        btnSpeech2Text.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "speech2text clicked ", Toast.LENGTH_LONG).show();

                // We first check if mic is available
                if (!Helper.isMicAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Please make sure that your mic is available ", Toast.LENGTH_LONG).show();
                    return;
                }

                // Not all device support the SpeechRecognizer Class.
                // We can either use SpeechRecognizer.isRecognitionAvailable(getContext()) to check if the service is available
                // or use the try-catch block
                // We use try-catch here
                try {
                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    // Specify language model
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now Please...");
                    startActivityForResult(i, SPEECH_TO_TEXT);
                } catch (ActivityNotFoundException e) {
                    // catch package exception, which can be fixed by downloading the google app.
                    // But we shall let the user decide whether to install the google app
                    // So we redirect.
                    Toast.makeText(getApplicationContext(), "Google app required for using this feature. Redirecting now... " + e.getMessage(), Toast.LENGTH_LONG).show();
                    String appPackageName = "com.google.android.googlequicksearchbox";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException e1) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Device not supported... " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        db.collection("Place").whereEqualTo("placeName", curDiary.getPlaceName())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot d : queryDocumentSnapshots) {
                    currentPlace = d.toObject(Place.class);
                    getWeatherInfo();
                    txtDate.setText(curDiary.getTxtDate());
                    txtCity.setText(curDiary.getTxtCity());
                    edtDiary.setText(curDiary.getEdtDiary());
                }
            }
        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            // 表示 调用照相机拍照
//            case IMAGE_RESULT_CODE:
//                if (resultCode == RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    imgPhoto.setImageBitmap(bitmap);
//                    Toast.makeText(getApplicationContext(), "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
//                }
//                break;
//            // 选择图片库的图片
//            case PICK:
//                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
//                    imgPhoto.setImageURI(uri);
//                    photoUri = uri;
//
//                }
//                break;
//        }
//
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPEECH_TO_TEXT:
                    ArrayList<String> strData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);   //This is our data, in this case the intent will contain a String.
                    edtDiary.setText(strData.get(0));  //the first item contains the text.
                    break;
            }
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


        double lat = currentPlace.getLatitude();
        double lon = currentPlace.getLongitude();

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
                imgWeatherUri = new StringBuilder("https://openweathermap.org/img/w/")
                        .append(response.body().getWeather().get(0).getIcon())
                        .append(".png").toString();

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
//                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
//
//    /**
//     * Create a file Uri for saving an image or video
//     **/
//    private static Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    /**
//     * Create a File for saving an image or video
//     */
//    private static File getOutputMediaFile(int type) {
//        String path;
//
//        // check if the sd card is mounted
//        boolean sdCardMounted = Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED);
//        if (sdCardMounted) {
//
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), "MyTravelDiary");
//            // This location works best if you want the created images to be shared
//            // between applications and persist after your app has been uninstalled.
//
//            // Create the storage directory if it does not exist
//            if (!mediaStorageDir.exists()) {
//                if (!mediaStorageDir.mkdirs()) {
//                    Log.e("qwer", "failed to create directory");
//                    return null;
//                }
//            }
//
//            path = mediaStorageDir.getPath();
//
//        } else {
//            // no external sd card
//            Log.e("qwer", "Cannot save photo. No external storage detected");
//            // use the internal storage
//            //path = getFilesDir();
//            return null;
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile;
//        mediaFile = new File(path + File.separator + "IMG_" + timeStamp + ".jpg");
//        return mediaFile;
//    }

    private void saveDiary(String diary) {

    }
}
