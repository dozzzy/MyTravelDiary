package com.example.a15862.mytraveldiary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private MyCustomAdapterForImages adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static int themeId = R.style.picture_white_style;
    private int chooseMode = PictureMimeType.ofAll();


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
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        edtDiary = (EditText) findViewById(R.id.edtDiary);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnSave = (Button) findViewById(R.id.btnSave);

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