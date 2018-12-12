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

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCustomAdapterForDiary extends RecyclerView.Adapter<MyCustomAdapterForDiary.ViewHolder> implements View.OnClickListener {

    private List<Diary> upload;
    private Context context;
    private LayoutInflater mInflater;


    public MyCustomAdapterForDiary(Context aContext, List<Diary> aupload) {
        Log.e("3rd", "mycustonAdapter constructor");
        context = aContext;  //saving the context we'll need it again.
        upload = aupload;
        Log.e("3rd", String.valueOf(upload.size()));
        mInflater = LayoutInflater.from(context);
        Log.e("3rd", "mycustonAdapter constructor end");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("3rd", "onCreateViewHolder");
        View view = mInflater.inflate(R.layout.list_item_diary, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("3rd", "onBindViewHolder");
        Diary currentUp = upload.get(position);
        holder.txtLocation.setText(currentUp.getTxtCity());

        holder.itemView.setTag(position);
        holder.txtDate.setText(currentUp.getTxtDate());
        holder.txtDiary.setText(currentUp.getEdtDiary());
        // Picasso helps us load photo by using url.
        Picasso.get().load(currentUp.getPhotoUri()).fit().centerCrop().into(holder.photoUri);
    }

    @Override
    public int getItemCount() {
        return upload.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDiary;
        private TextView txtDate;
        private TextView txtLocation;
        private ImageView photoUri;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.e("3rd", "viewholder constructor");
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtDiary = (TextView) itemView.findViewById(R.id.txtDiary);
            photoUri = (ImageView) itemView.findViewById(R.id.imgPhoto);
        }
    }


    private OnItemClickListener myOnItemClickListener = null;

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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
