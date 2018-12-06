package com.example.a15862.mytraveldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentsActivity extends AppCompatActivity {

    private TextView txtPlaceName;
    private EditText edtAddComment;
    private Button btnSave;
    private Button btnJump;

    private MyCustomAdapterForComment mAdapter;
    private RecyclerView commentList;
    private List<Comment> comments = new ArrayList<>();

    private Place currentPlace;
    private RatingBar customRating;
    private float score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        txtPlaceName = findViewById(R.id.txtPlaceName);
        customRating = findViewById(R.id.ratingBarCustom);


        Bundle info = getIntent().getExtras();
        currentPlace = (Place) info.getSerializable("Place");
        txtPlaceName.setText(currentPlace.getPlaceName());


    }


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Comment").whereEqualTo("placeName", currentPlace.getPlaceName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (DocumentSnapshot d : queryDocumentSnapshots) {
//                    Comment c = d.toObject(Comment.class);
//                    commentArray.add(c);
//                }
//                mAdapter = new MyCustomAdapterForComment(ClickExistActivity.this, commentArray);
//                commentList.setAdapter(mAdapter);
//
//            }
//        });
//
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                storeComment();
//                currentPlace.addScore(score);
//                pd.updateData(currentPlace);
//                Intent back = new Intent(ClickExistActivity.this, MapActivity.class);
//                startActivity(back);
//            }
//        });
//
//        btnJump.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                storeComment();
//                currentPlace.addScore(score);
//                pd.updateData(currentPlace);
//                Intent intent = new Intent(ClickExistActivity.this, AddDiaryActivity.class);
//                Bundle b = new Bundle();
//                b.putSerializable("Place", currentPlace);
//                intent.putExtras(b);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void storeComment() {
//        String comment = editText.getText().toString();
//        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
//        Comment c = new Comment(load.getString("displayName", "123"), currentPlace.getPlaceName(), comment);
//        CommentDAO cd = new CommentDAO();
//        cd.addComment(c,0);
//    }


}
