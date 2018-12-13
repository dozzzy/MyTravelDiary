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
import com.example.a15862.mytraveldiary.Entity.User;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCommentsActivity extends AppCompatActivity implements AdapterCallback {

    private TextView txtPlaceName;
    private EditText edtAddComment;
    private Button btnSave;
    private Button btnJump;
    private FirebaseFirestore db;

    private MyCustomAdapterForComment mAdapter;
    private RecyclerView commentList;
    public List<Comment> commentArray = new ArrayList<>();
    public List<User> userArray = new ArrayList<>();
    public Map<Comment, User> map = new HashMap<>();
    PlaceDAO pd = new PlaceDAO();
    private Place currentPlace;
    private RatingBar customRating;
    private float score;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Log.e("qwer", "comment on create");
        db = FirebaseFirestore.getInstance();
        txtPlaceName = findViewById(R.id.txtPlaceName);
        customRating = findViewById(R.id.ratingBarCustom);
        edtAddComment = findViewById(R.id.edtAddComment);
        btnSave = findViewById(R.id.btnSaveReturn);
        btnJump = findViewById(R.id.btnSaveJump);
        commentList = findViewById(R.id.commentList);
        edtAddComment.setText("");
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

        Log.e("qwer", "get currentPlace");
        db.collection("Comment").whereEqualTo("placeName", currentPlace.getPlaceName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        Comment c = d.toObject(Comment.class);
                        c.setPlaceName(currentPlace.getPlaceName());
                        commentArray.add(c);
                    }
                }
                // comments are sorted by the number of like
                Collections.sort(commentArray, new Comparator<Comment>() {
                    @Override
                    public int compare(Comment o1, Comment o2) {
                        return o2.getLike() - o1.getLike();
                    }
                });
                count = commentArray.size();
                // when we get all the comments we need to find the creator of them so we can display the creators' reputation.
                for (final Comment c : commentArray) {
                    db.collection("User").document(c.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User u;
                            if (documentSnapshot.exists()) {
                                u = documentSnapshot.toObject(User.class);
                            } else {
                                u = new User();
                                u.setUsername("fromApi");
                            }
                            map.put(c, u);
                            count--;
                            if (count == 0) {
                                mAdapter = new MyCustomAdapterForComment(ViewCommentsActivity.this, map, commentArray, ViewCommentsActivity.this);
                                Log.e("qwer", "set adapter");
                                commentList.setAdapter(mAdapter);
                            }

                        }
                    });
                }

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtAddComment.equals("")) {
                    storeComment();
                }
                currentPlace.addScore(score);
                Log.e("qwer", currentPlace.getPlaceName());
                pd.updateData(currentPlace);
                Log.e("qwer", "sent comment");
                Intent back = new Intent(ViewCommentsActivity.this, MapActivity.class);
                startActivity(back);
            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtAddComment.equals("")) {
                    storeComment();
                }
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

    @Override
    public void onItemClick(Comment comment) {
        // we handle the database operation for "like" here
        Comment clickedComment = comment;
        clickedComment.setLike(clickedComment.getLike() + 1);
        Log.e("qwer", "db change start");
        Log.e("qwer", clickedComment.getUserComment());
        clickedComment.setPlaceName(currentPlace.getPlaceName());
        // need to update the total like of comment
        db.collection("Comment")
                .document(clickedComment.getUsername() + "." + String.valueOf(clickedComment.getTime()))
                .set(clickedComment);
        if (clickedComment.getFromAPI() == 1) {
        } else {
            // if the creator is a user of our app, we need to add its reputation.
            db.collection("User").document(clickedComment.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getData() != null) {
                        User likedUser = documentSnapshot.toObject(User.class);
                        likedUser.setLike(likedUser.getLike() + 1);
                        db.collection("User").document(likedUser.getUsername()).set(likedUser);
                    }
                }
            });
        }

    }


    private void storeComment() {
        String comment = edtAddComment.getText().toString();
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        Comment c = new Comment(load.getString("username", "DEFAULT"), currentPlace.getPlaceName(), comment);
        c.setDisplayName(load.getString("username", "DEFAULT"));
        Log.e("qwer", "storeComment");
        CommentDAO cd = new CommentDAO();
        cd.addComment(c, 0);
    }

}
