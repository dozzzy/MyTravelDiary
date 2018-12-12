package com.example.a15862.mytraveldiary;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDiaryActivity extends Activity {

    private ImageView addImg;
    private final int IMAGE_RESULT_CODE = 2;// 表示打开照相机
    private final int PICK = 1;// 选择图片库
    private final int SPEECH_TO_TEXT = 3;
    public static final int SAVE = 9997;
    private Place currentPlace;
    private ImageView imgWeather, imgPhoto;

    private String imgWeatherUri;

    private TextView txtDate, txtCity, txtTemperature;
    private TextView edtDiary;

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;
    private Button btnClear, btnSave;

    private String PHOTO_FILE_NAME = "temp_photo.jpg";
    private String photoUri = null; // for photos
    private int photoCnt;
    private String timeStamp;
    private String cityLoc;
    private String diaryName;
    private Uri tempUri;
    private boolean visible=false;
    private MyCustomAdapterForImages adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static int themeId = R.style.picture_white_style;
    private int chooseMode = PictureMimeType.ofAll();
    private Switch switchVisible;


    private Button btnSpeech2Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        Bundle info = getIntent().getExtras();
        currentPlace = (Place) info.getSerializable("Place");
//        btnCamera = (ImageButton) findViewById(R.id.btnCamera);
//        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtCity.setText(currentPlace.getPlaceName());
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSpeech2Text = (Button) findViewById(R.id.btnSpeech2Text);
        switchVisible=(Switch) findViewById(R.id.switchVisible);

        if (switchVisible!=null){
            switchVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        visible=true;
                    } else {
                        visible=false;
                    }
                }
            });
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(AddDiaryActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new MyCustomAdapterForImages(AddDiaryActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(1);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyCustomAdapterForImages.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelector.create(AddDiaryActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                if (selectList.size() > 0) {
                    Log.i("image", "click to select");
                    PictureSelector.create(AddDiaryActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                }
            }
        });
        btnSpeech2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
                String displayName = load.getString("displayName", "DEFAULT");
                String username = load.getString("username", "DEFAULT");
                Diary diary = new Diary(username, currentPlace.getPlaceName());
                if (photoUri != null) {
                    diary.setPhotoUri(photoUri);
                }
                if (imgWeatherUri != null) {
                    diary.setImgWeather(imgWeatherUri);
                }
                diary.setDiaplayName(displayName);
                diary.setTxtDate(txtDate.getText().toString());
                diary.setTxtCity(txtCity.getText().toString());
                diary.setTxtTemperature(txtTemperature.getText().toString());
                diary.setEdtDiary(edtDiary.getText().toString());
                diary.setVisible(visible);
                DiaryDAO diaryDAO = new DiaryDAO();
                diaryDAO.uploadDiary(diary);
                Intent back = new Intent(AddDiaryActivity.this, MapActivity.class);
                startActivity(back);
            }
        });


    }

    private MyCustomAdapterForImages.onAddPicClickListener onAddPicClickListener = new MyCustomAdapterForImages.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            gallery();
        }
    };

    private void gallery() {
        PictureSelector.create(AddDiaryActivity.this)
                .openGallery(chooseMode)
                .theme(R.style.picture_white_style)
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .isZoomAnim(true)
                .enableCrop(true)
                .compress(true)
                .synOrAsy(true)
                .glideOverride(100, 100)
                .withAspectRatio(1, 1)
                .hideBottomControls(true)
                .isGif(false)
                .freeStyleCropEnabled(true)
                .circleDimmedLayer(false)
                .showCropFrame(true)
                .showCropGrid(true)
                .openClickSound(false)
                .selectionMedia(selectList)
                .minimumCompressSize(100)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i("imageAdd", media.getPath());
                        photoUri = "file://" + media.getPath();
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;

                case SPEECH_TO_TEXT:
                    ArrayList<String> strData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);   //This is our data, in this case the intent will contain a String.
                    edtDiary.setText(strData.get(0));  //the first item contains the text.
                    break;
            }
        }


        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(AddDiaryActivity.this);
                } else {
                    Toast.makeText(AddDiaryActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

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
//                txtCity.setText(response.body().getName());
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



    private void saveDiary(String diary) {

    }
}