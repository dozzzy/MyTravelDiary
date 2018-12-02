package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Place;

public class ClickExistActivity extends Activity {
    private Place currentPlace;
    private TextView txtPlaceName;
    private RatingBar ratingBar;
    private EditText editText;
    private Button btnSave;
    PlaceDAO pd;
    private float score;
    private Button btnJump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pd = new PlaceDAO();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_exist);
        txtPlaceName = findViewById(R.id.txtPlaceName);
        ratingBar = findViewById(R.id.ratingBar);
        editText = findViewById(R.id.editText);
        btnSave = findViewById(R.id.btnSaveReturn);
        btnJump = findViewById(R.id.btnSaveJump);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });
        Bundle info = getIntent().getExtras();
        currentPlace = (Place)info.getSerializable("Place");
        txtPlaceName.setText(currentPlace.getPlaceName());



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                currentPlace.getComments().add(comment);
                currentPlace.addScore(score);
                pd.updateData(currentPlace);
                Intent back = new Intent(ClickExistActivity.this,MapActivity.class);
                startActivity(back);
            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editText.getText().toString();
                currentPlace.getComments().add(comment);
                currentPlace.addScore(score);
                pd.updateData(currentPlace);
                Intent intent = new Intent(ClickExistActivity.this, AddDiaryActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("Place", currentPlace);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
