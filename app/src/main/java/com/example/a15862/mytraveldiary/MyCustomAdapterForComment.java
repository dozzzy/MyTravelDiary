package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.File;
import java.util.List;

public class MyCustomAdapterForComment extends RecyclerView.Adapter<MyCustomAdapterForComment.ViewHolder> {

    private List<Comment> upload;
    private Context context;
    private LayoutInflater mInflater;
    private FirebaseFirestore db;


    public MyCustomAdapterForComment(Context aContext, List<Comment> aupload) {
        context = aContext;  //saving the context we'll need it again.
        upload = aupload;
        mInflater = LayoutInflater.from(context);
        db=FirebaseFirestore.getInstance();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment currentCom = upload.get(position);
        holder.txtDisplayName.setText(currentCom.getUsername());
        holder.txtComment.setText(currentCom.getUserComment());
        holder.txtLikesCount.setText(currentCom.getLike());
        holder.thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Comment clickedComment=upload.get(holder.getAdapterPosition());
                clickedComment.setLike(clickedComment.getLike()+1);
                Log.i("qwer",clickedComment.getUserComment());
                db.collection("Comment")
                        .document(clickedComment.getUsername()+"."+String.valueOf(clickedComment.getTime()))
                        .set(clickedComment);
                db.collection("User").document(clickedComment.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User likedUser =documentSnapshot.toObject(User.class);
                        likedUser.setLike(likedUser.getLike()+1);
                        db.collection("User").document(likedUser.getUsername()).set(likedUser);
                    }
                });
            }
        });
        // TODO:
        //holder.txtUserRates.setText(currentCom.getUser.getUserRates());
        //holder.txtLikesCount.setText(currentCom.getLikesCount());

    }


    @Override
    public int getItemCount() {
        return upload.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDisplayName;
        private TextView txtComment;
        private TextView txtUserRates;
        private TextView txtLikesCount;
        private CardView cardView;
        private ImageButton thumbUp;



        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            txtDisplayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtUserRates = (TextView) itemView.findViewById(R.id.txtUserRates);
            txtLikesCount = (TextView) itemView.findViewById(R.id.txtLikesCount);
            thumbUp = (ImageButton) itemView.findViewById(R.id.img_up);


            //TODO: update database
        }
    }
}


