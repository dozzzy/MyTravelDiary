package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.CommentDAO;
import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClickExistActivity extends Activity {
    // after clicked an exist place on map
    private Place currentPlace;
    private TextView txtPlaceName;
    private RatingBar ratingBar;
    PlaceDAO pd;
    String key = "&key=AIzaSyC1qPnWqt7G0areqx3sDhdElU04b0HTr6A";
    String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    private TextView txtTotalComments;
    private TextView txtCategory;
    private ImageView imgPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pd = new PlaceDAO();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_exist);
        txtPlaceName = findViewById(R.id.txtPlaceName);
        txtCategory = findViewById(R.id.txtCategory);
        ratingBar = findViewById(R.id.ratingBar);
        txtTotalComments = findViewById(R.id.txtTotalComments);
        imgPlace = findViewById(R.id.imgPlace);
        Bundle info = getIntent().getExtras();
        currentPlace = (Place) info.getSerializable("Place");
        txtPlaceName.setText(currentPlace.getPlaceName());
        List<String> categorys = currentPlace.getCategory();

        txtCategory.setText(categorys.get(0));
        float rating = currentPlace.getTotalScore() / currentPlace.getScoreCount();
        Log.i("qwer", Float.toString(rating));
        if (rating != 0) {
            ratingBar.setRating(rating);
        }

        if (currentPlace.getPhotoPath() != null) {
            String path = url + currentPlace.getPhotoPath() + key;
            Picasso.get().load(path).into(imgPlace);
        }
        Log.i("qwer", String.valueOf(currentPlace.getTotalComment()));
        txtTotalComments.setText("View comments");
        txtTotalComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qwer", "view comments clicked");
                Intent intent = new Intent(ClickExistActivity.this, ViewCommentsActivity.class);
                // put the place information into bundle
                Bundle b = new Bundle();
                b.putSerializable("Place", currentPlace);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

}
