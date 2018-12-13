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
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCustomAdapterForComment extends RecyclerView.Adapter<MyCustomAdapterForComment.ViewHolder> {

    public List<Comment> upload;
    public Map<Comment, User> userList;
    private Context context;
    private LayoutInflater mInflater;
    private AdapterCallback adapter;


    public MyCustomAdapterForComment(Context aContext, Map<Comment, User> users, List<Comment> aupload, AdapterCallback adapterCallback) {
        context = aContext;  //saving the context we'll need it again.
        upload = aupload;
        userList = users;
        adapter = adapterCallback;
        mInflater = LayoutInflater.from(context);
        Log.e("qwer", "adapter start");
        Log.e("qwer", String.valueOf(users.size()));
    }


    @Override
    public MyCustomAdapterForComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_comment, parent, false);
        return new ViewHolder(view, adapter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment currentCom = upload.get(position);
        User currentUser = userList.get(currentCom);
        holder.txtDisplayName.setText(currentCom.getUsername());
        holder.txtComment.setText(currentCom.getUserComment());
        holder.txtLikesCount.setText(String.valueOf(currentCom.getLike()));
        Log.e("qwer", currentCom.getUserComment());
        if (currentUser.getUsername().equals("fromApi")) {
            // if user is from other api, we sent them a avatar
            Picasso.get().load("http://www.zimphysio.org.zw/wp-content/uploads/2018/01/default-avatar-2.jpg")
                    .into(holder.imgUserPhoto);
            holder.txtUserRates.setText("not our user");
        } else {
            holder.txtUserRates.setText(String.valueOf(currentUser.getLike()));
            // if user is from our app but do not have an avatar, we set a default avatar
            if (currentUser.getAvatar() == null) {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mytraveldiary-d8885.appspot.com/o/avater.png?alt=media&token=fae2ef71-2350-4237-98f3-2a51be9ccb03")
                        .into(holder.imgUserPhoto);
            } else {
                Picasso.get().load(currentUser.getAvatar()).into(holder.imgUserPhoto);
            }
        }


    }


    @Override
    public int getItemCount() {
        return upload.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDisplayName;
        private TextView txtComment;
        private TextView txtUserRates;
        private TextView txtLikesCount;
        private CardView cardView;
        private ImageButton thumbUp;
        private ImageView imgUserPhoto;
        private AdapterCallback adapter;

        public ViewHolder(View itemView, AdapterCallback adapterCallback) {
            super(itemView);
            adapter = adapterCallback;
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            txtDisplayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtUserRates = (TextView) itemView.findViewById(R.id.txtUserRates);
            txtLikesCount = (TextView) itemView.findViewById(R.id.txtLikesCount);
            imgUserPhoto = (ImageView) itemView.findViewById(R.id.imgUserPhoto);
            thumbUp = (ImageButton) itemView.findViewById(R.id.img_up);

            thumbUp.setTag(itemView);
            thumbUp.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // once user click on thumbUp we update UI before update Database so user can know they have clicked successfully
            Log.e("qwer", "change clicked");
            // user can only one comment once.
            v.setEnabled(false);
            View temp = (View) v.getTag();
            TextView tempUserRates = (TextView) temp.findViewById(R.id.txtUserRates);
            TextView tempLikesCout = (TextView) temp.findViewById(R.id.txtLikesCount);
            int likeCount = Integer.parseInt(tempLikesCout.getText().toString()) + 1;
            tempLikesCout.setText(String.valueOf(likeCount));
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(tempUserRates.getText().toString());
            if (isNum.matches()) {
                // if user is not from our app the reputation is "not our user". It is not a number
                int userRate = Integer.parseInt(tempUserRates.getText().toString()) + 1;
                tempUserRates.setText(String.valueOf(userRate));
                userList.get(upload.get(getAdapterPosition())).setLike(userRate);
            }
            upload.get(getAdapterPosition()).setLike(likeCount);
            // we put database update in activity code.
            adapter.onItemClick(upload.get(getAdapterPosition()));
        }
    }
}


