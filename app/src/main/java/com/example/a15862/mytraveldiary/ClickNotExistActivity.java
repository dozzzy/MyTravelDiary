package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Place;

public class ClickNotExistActivity extends Activity {
    private Button btnJump;
    private EditText edtPlaceName;
    private Spinner spinner;
    private String cat;
    private RatingBar ratingBar;
    private Button btnSave;
    private EditText edtComment;
    double lat;
    double lon;
    private float score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_not_exist);
        edtPlaceName = findViewById(R.id.edtPlaceName);
        ratingBar = findViewById(R.id.ratingBar);
        btnJump = (Button) findViewById(R.id.btnJump);
        spinner = findViewById(R.id.spinner);
        btnSave = findViewById(R.id.btnSave);
        edtComment = findViewById(R.id.edtComment);
        Bundle b = getIntent().getExtras();
        lat = b.getDouble("Latitude");
        lon = b.getDouble("Longitude");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });

        Log.i("Jing","1");


        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClickNotExistActivity.this, AddDiaryActivity.class);
                startActivity(intent);
            }
        });
        Log.i("Jing","2");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] catagory = getResources().getStringArray(R.array.placeCategory);
                cat = catagory[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });
        Log.i("Jing","3");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = edtPlaceName.getText().toString();
                String comment = edtComment.getText().toString();
                Place p = new Place();
                p.setLatitude(lat);
                p.setLongitude(lon);
                p.setPlaceName(pname);
                p.getComments().add(comment);
                p.getCatagoty().add(cat);
                p.addScore(score);
                PlaceDAO pd = new PlaceDAO();
                pd.addPlace(p);
            }
        });

        Log.i("Jing","4");
    }
}
