package com.example.a15862.mytraveldiary;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {

    private List<Diary> upload;
    private Context context;
    private LayoutInflater mInflater;


    public MyCustomAdapter(Context aContext, List<Diary> aupload) {
        Log.e("3rd","mycustonAdapter constructor");
        context = aContext;  //saving the context we'll need it again.
        upload = aupload;
        mInflater=LayoutInflater.from(context);
        Log.e("3rd","mycustonAdapter constructor end");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("3rd","onCreateViewHolder");
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("3rd","onBindViewHolder");
        Diary currentUp=upload.get(position);
//        holder.textViewUsername.setText(currentUp.getUsername());
//        holder.textViewComment.setText(currentUp.getComment());
//        // Picasso helps us load photo by using url.
//        Picasso.get().load(currentUp.getImageUri()).fit().centerCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return upload.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewComment;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
//            Log.e("3rd","viewholder constructor");
//            textViewComment=(TextView)itemView.findViewById(R.id.textViewComment);
//            textViewUsername=(TextView)itemView.findViewById(R.id.textViewUsername);
//            img=(ImageView)itemView.findViewById(R.id.img);
//            return itemView;
        }
    }

}
