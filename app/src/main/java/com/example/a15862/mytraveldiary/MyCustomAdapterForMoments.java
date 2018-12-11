package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.example.a15862.mytraveldiary.Entity.User;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class MyCustomAdapterForMoments extends RecyclerView.Adapter<MyCustomAdapterForMoments.ViewHolder> implements View.OnClickListener {

    private List<User> users;
    private List<Diary> diaries;
    private Context context;
    private LayoutInflater mInflater;


    public MyCustomAdapterForMoments(Context aContext, List<User> u, List<Diary> d) {
        context = aContext;  //saving the context we'll need it again.
        users = u;
        diaries = d;
        mInflater = LayoutInflater.from(context);
        Log.e("qwer",String.valueOf(u.size()));
        Log.e("qwer",String.valueOf(d.size()));
    }


    @Override
    public MyCustomAdapterForMoments.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_moment, parent, false);
        view.setOnClickListener(this);
        return new MyCustomAdapterForMoments.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapterForMoments.ViewHolder holder, int position) {
//        User friend= friendList.get(position);
        User u = users.get(position);
        Diary diary = diaries.get(position);
//        Log.e("qwer",u.getUsername());
        Log.e("qwer",u.getDisplayName());
        holder.itemView.setTag(position);
        holder.txtDisplayName.setText(u.getDisplayName());
        holder.txtDiary.setText(diary.getEdtDiary());
        holder.txtTime.setText(new Date(diary.getTime()).toString());
        if (u.getAvatar() != null) {
            Picasso.get().load(u.getAvatar()).into(holder.imgAvatar);
        } else {
            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/mytraveldiary-d8885.appspot.com/o/avater.png?alt=media&token=fae2ef71-2350-4237-98f3-2a51be9ccb03")
                    .into(holder.imgAvatar);
        }

        if (diary.getPhotoUri() != null) {
            Log.i("TTT", diary.getPhotoUri());
            Picasso.get().load(diary.getPhotoUri()).into(holder.diaryImage);
        } else {
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDisplayName;
        private ImageView imgAvatar;
        private ImageView diaryImage;
        private CardView cardView;
        private TextView txtDiary;
        private TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            txtDisplayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
            diaryImage = (ImageView) itemView.findViewById(R.id.diaryImage);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            txtDiary = (TextView) itemView.findViewById(R.id.txtDiary);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);

        }
    }

    private MyCustomAdapterForMoments.OnItemClickListener myOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MyCustomAdapterForMoments.OnItemClickListener listener) {
        Log.e("haohui", "lsi");
        this.myOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (myOnItemClickListener != null) {
            Log.e("haohui", "lsi");
            myOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
}
