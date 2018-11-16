//package com.example.ivc.mtd_p1;
//
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.speech.RecognizerIntent;
//import android.speech.SpeechRecognizer;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.example.ivc.mtd_p1.Model.WeatherResult;
//import com.example.ivc.mtd_p1.Retrofit.IOpenWeatherMap;
//import com.example.ivc.mtd_p1.Retrofit.RetrofitClient;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//import static android.app.Activity.RESULT_OK;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class DiaryFragment extends Fragment {
//
//    private ImageView imgWeather, imgPhoto;
//    private TextView txtDate, txtCity, txtWeather;
//    private EditText edtTitle, edtDiary;
//    private CompositeDisposable compositeDisposable;
//    private IOpenWeatherMap mService;
//
//    private Button btnClear;
//    private Button btnTakePicture;
//    private Button btnSpeech2Text;
//    private Button btnSave;
//
//    public static final int TAKE_PHOTO = 9999;
//    public static final int SPEECH_TO_TEXT = 9998;
//
//    public DiaryFragment() {
////        compositeDisposable = new CompositeDisposable();
////        Retrofit retrofit = RetrofitClient.getInstance();
////        mService = retrofit.create(IOpenWeatherMap.class);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View diaryView = inflater.inflate(R.layout.fragment_diary, container, false);
//
//        txtCity = (TextView) diaryView.findViewById(R.id.txtCity);
//        txtWeather = (TextView) diaryView.findViewById(R.id.txtWeather);
//        txtDate = (TextView) diaryView.findViewById(R.id.txtDate);
//
//        imgWeather = (ImageView) diaryView.findViewById(R.id.imgWeather);
//        imgPhoto = (ImageView) diaryView.findViewById(R.id.imgPhoto);
//
//        edtTitle = (EditText) diaryView.findViewById(R.id.edtTitle);
//        edtDiary = (EditText) diaryView.findViewById(R.id.edtDiary);
//
//        btnSpeech2Text = (Button) diaryView.findViewById(R.id.btnSpeech2Text);
//        btnTakePicture = (Button) diaryView.findViewById(R.id.btnTakePicture);
//        btnClear = (Button) diaryView.findViewById(R.id.btnClear);
//        btnSave = (Button) diaryView.findViewById(R.id.btnSave);
//
//        getWeatherInfo();
//        //Log.e("qwer", "got weather");
//
//        btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edtDiary.setText("");
//            }
//        });
//
//        btnSpeech2Text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // We first check if mic is available
//                if (!Helper.isMicAvailable(getContext())) {
//                    Toast.makeText(getContext(), "Please make sure that your mic is available ", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                // Not all device support the SpeechRecognizer Class.
//                // We can either use SpeechRecognizer.isRecognitionAvailable(getContext()) to check if the service is available
//                // or use the try-catch block
//                // We use try-catch here
//                try {
//                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                    // Specify language model
//                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");
//                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now Please...");
//                    startActivityForResult(i, SPEECH_TO_TEXT);
//                } catch (ActivityNotFoundException e) {
//                    // catch package exception, which can be fixed by downloading the google app.
//                    // But we shall let the user decide whether to install the google app
//                    // So we redirect.
//                    Toast.makeText(getContext(), "Google app required for using this feature. Redirecting now... " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    String appPackageName = "com.google.android.googlequicksearchbox";
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                    } catch (android.content.ActivityNotFoundException e1) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "Device not supported... " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//
//
//            }
//        });
//
//        btnTakePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // We first check if mic is available
//                if (!Helper.isCameraAvailable(getContext())) {
//                    Toast.makeText(getContext(), "Please make sure that your camera is available ", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, TAKE_PHOTO);
//
//            }
//        });
//
//        return diaryView;
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (!(resultCode == RESULT_OK))
//        {
//            Toast.makeText(getContext(), "Start Activity for Result Failed.  Does your device support this feature?", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        switch(requestCode){
//            case SPEECH_TO_TEXT:
//                ArrayList<String> strData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);   //This is our data, in this case the intent will contain a String.
//                edtDiary.setText(strData.get(0));  //the first item contains the text.
//                break;
//            case TAKE_PHOTO:
//                Bundle bundleData = data.getExtras();           //images are stored in a bundle wrapped within the intent...
//                Bitmap Photo = (Bitmap)bundleData.get("data");  //the bundle key is "data".
//                imgPhoto.setImageBitmap(Photo);
//                // the photo will be saved on the Fire database
//                // TODO: integrate FireDB function
//
//                break;
//        }
//
//    }
//
//    private void getWeatherInfo() {
//        // establish the http request
//        Retrofit retrofit = RetrofitClient.getInstance();
//        // that query for weather information in the format given in the interface of IOpenWeatherMap
//        mService = retrofit.create(IOpenWeatherMap.class);
//        //TODO: replace the hard coded location, after integration
//        Call<WeatherResult> model = mService.getWeatherByLatLng(42.33, -71.12, Helper.API_KEY_WEATHER, "metric");
//        model.enqueue(new Callback<WeatherResult>() {
//            // if success
//            @Override
//            public void onResponse(@NonNull Call<WeatherResult> call, @NonNull Response<WeatherResult> response) {
//
//                // Picasso loads the image, in this case the weather icon from the website,
//                // to the ImageView imgWeather
//                Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
//                        .append(response.body().getWeather().get(0).getIcon())
//                        .append(".png").toString()).into(imgWeather);
//
//                // Set corresponding TextView to the information retrieved
//                txtCity.setText(response.body().getName());
//                txtWeather.setText(new StringBuilder("The current temperature is ")
//                        .append(String.valueOf(response.body().getMain().getTemp())).append("°C").toString());
//                txtDate.setText(Helper.convertUnixToDate(response.body().getDt()));
//            }
//            // if fail
//            @Override
//            public void onFailure(@NonNull Call<WeatherResult> call, @NonNull Throwable t) {
//                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }
//
//}
