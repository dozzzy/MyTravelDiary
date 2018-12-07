package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.a15862.mytraveldiary.Entity.User;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Set;

public class MyCustomAdapterForFriends extends RecyclerView.Adapter<MyCustomAdapterForFriends.ViewHolder> implements View.OnClickListener{

    private List<User> friendList;
    private Context context;
    private LayoutInflater mInflater;


    public MyCustomAdapterForFriends(Context aContext, List<User> aupload) {
        context = aContext;  //saving the context we'll need it again.
        friendList = aupload;
        mInflater=LayoutInflater.from(context);
    }


    @Override
    public MyCustomAdapterForFriends.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_friend, parent, false);
        view.setOnClickListener(this);
        return new MyCustomAdapterForFriends.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapterForFriends.ViewHolder holder, int position) {
        User friend= friendList.get(position);
        holder.itemView.setTag(position);
        holder.txtDisplayName.setText(friend.getDisplayName());
        holder.txtUsername.setText(friend.getUsername());
        if (friend.getAvatar()!=null){
            Picasso.get().load(friend.getAvatar()).into(holder.imgAvatar);
        }else{
            Picasso.get()
                    .load("https://firebasestorage.googleapis.com/v0/b/mytraveldiary-d8885.appspot.com/o/avater.png?alt=media&token=fae2ef71-2350-4237-98f3-2a51be9ccb03")
                    .into(holder.imgAvatar);
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDisplayName;
        private TextView txtUsername;
        private ImageView imgAvatar;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card_view);
            txtDisplayName=(TextView)itemView.findViewById(R.id.txtDisplayName);
            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
            imgAvatar=(ImageView)itemView.findViewById(R.id.imgAvatar);
        }
    }

    private MyCustomAdapterForFriends.OnItemClickListener myOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MyCustomAdapterForFriends.OnItemClickListener listener) {
        Log.e("haohui","lsi");
        this.myOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (myOnItemClickListener != null) {
            Log.e("haohui","lsi");
            myOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

}