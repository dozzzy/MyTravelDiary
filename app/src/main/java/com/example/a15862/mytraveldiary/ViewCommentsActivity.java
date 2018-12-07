package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.DAO.CommentDAO;
import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentsActivity extends AppCompatActivity {

    private TextView txtPlaceName;
    private EditText edtAddComment;
    private Button btnSave;
    private Button btnJump;

    private MyCustomAdapterForComment mAdapter;
    private RecyclerView commentList;
    private List<Comment> commentArray = new ArrayList<>();
    PlaceDAO pd;
    private Place currentPlace;
    private RatingBar customRating;
    private float score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Log.e("qwer","comment on create");
        txtPlaceName = findViewById(R.id.txtPlaceName);
        customRating = findViewById(R.id.ratingBarCustom);
        edtAddComment = findViewById(R.id.edtAddComment);
        btnSave = findViewById(R.id.btnSaveReturn);
        btnJump = findViewById(R.id.btnSaveJump);
        commentList = findViewById(R.id.commentList);

        commentList.setHasFixedSize(true);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        customRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });

        Bundle info = getIntent().getExtras();
        currentPlace = (Place) info.getSerializable("Place");
        //txtPlaceName.setText(info.getString("Place"));
        txtPlaceName.setText(currentPlace.getPlaceName());

        Log.e("qwer","get currentPlace");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Comment").whereEqualTo("placeName", currentPlace.getPlaceName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        Comment c = d.toObject(Comment.class);
                        commentArray.add(c);
                    }
                }
                mAdapter = new MyCustomAdapterForComment(ViewCommentsActivity.this, commentArray);
                Log.e("qwer","set adapter");
                commentList.setAdapter(mAdapter);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeComment();
                currentPlace.addScore(score);
                pd.updateData(currentPlace);
                Log.e("qwer","sent comment");
                Intent back = new Intent(ViewCommentsActivity.this, MapActivity.class);
                startActivity(back);
            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeComment();
                currentPlace.addScore(score);
                pd.updateData(currentPlace);
                Intent intent = new Intent(ViewCommentsActivity.this, AddDiaryActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("Place", currentPlace);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private void storeComment() {
        String comment = edtAddComment.getText().toString();
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        Comment c = new Comment(load.getString("displayName", "123"), currentPlace.getPlaceName(), comment);
        Log.e("qwer","storeComment");
        CommentDAO cd = new CommentDAO();
        cd.addComment(c,0);
    }

}
