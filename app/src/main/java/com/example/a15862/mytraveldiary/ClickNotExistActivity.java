package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.CommentDAO;
import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;

public class ClickNotExistActivity extends Activity {
    // create new place on map
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
    private Place currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_not_exist);
        edtPlaceName = findViewById(R.id.edtPlaceName);
        ratingBar = findViewById(R.id.ratingBar);
        btnJump = findViewById(R.id.btnSaveJump);
        spinner = findViewById(R.id.spinner);
        btnSave = findViewById(R.id.btnSaveReturn);
        edtComment = findViewById(R.id.edtComment);
        final Bundle b = getIntent().getExtras();
        lat = b.getDouble("Latitude");
        lon = b.getDouble("Longitude");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] category = getResources().getStringArray(R.array.placeCategory);
                cat = category[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save comment and go back to map
                String placeName = edtPlaceName.getText().toString().trim();
                String placeComment = edtComment.getText().toString().trim();
                // comment can not be null but rating can
                if (placeComment.equals("") || placeComment.equals("")) {
                    Toast.makeText(getApplicationContext(), "Place name and Comment should not be null", Toast.LENGTH_LONG).show();
                } else {
                    storeData();
                    Intent back = new Intent(ClickNotExistActivity.this, MapActivity.class);
                    startActivity(back);
                }

            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save comment and go to write diary
                String placeName = edtPlaceName.getText().toString().trim();
                String placeComment = edtComment.getText().toString().trim();
                if (placeComment.equals("") || placeComment.equals("")) {
                    Toast.makeText(getApplicationContext(), "Place name and Comment should not be null", Toast.LENGTH_LONG).show();
                } else {
                    storeData();
                    Intent intent = new Intent(ClickNotExistActivity.this, AddDiaryActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("Place", currentPlace);
                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        });

    }

    private void storeData() {
        String pname = edtPlaceName.getText().toString();
        String comment = edtComment.getText().toString();
        Place p = new Place();
        p.setLatitude(lat);
        p.setLongitude(lon);
        p.setPlaceName(pname);
        p.getCategory().add(cat);
        p.addScore(score);
        Log.i("This", String.valueOf(p.getLatitude()));
        Log.i("This", String.valueOf(p.getLongitude()));
        //this place is user_defined , generate unique pid for it
        String pid = String.valueOf(p.hashCode());
        p.setPid(pid);
        PlaceDAO pd = new PlaceDAO();
        // add new place to database
        pd.addPlace(p);
        pd.updateData(p);
        currentPlace = p;
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        Comment c = new Comment(load.getString("username", "DEFAULT"), currentPlace.getPlaceName(), comment);
        c.setDisplayName(load.getString("username", "DEFAULT"));
        // add first comment of place
        CommentDAO cd = new CommentDAO();
        cd.addComment(c, 0);
    }
}
