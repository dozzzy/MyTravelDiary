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
    private AdapterCallback adapter;


    public MyCustomAdapterForComment(Context aContext, List<Comment> aupload,AdapterCallback adapterCallback) {
        context = aContext;  //saving the context we'll need it again.
        upload = aupload;
        adapter=adapterCallback;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public MyCustomAdapterForComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment currentCom = upload.get(position);
        holder.txtDisplayName.setText(currentCom.getUsername());
        holder.txtComment.setText(currentCom.getUserComment());
        holder.txtLikesCount.setText(String.valueOf(currentCom.getLike()));
        holder.thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                adapter.onItemClick(currentCom);
            }
        });

        //holder.txtUserRates.setText(currentCom.getUser.getUserRates());
        holder.txtLikesCount.setText(String.valueOf(currentCom.getLike()));

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


