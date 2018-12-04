package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllDiaryActivity extends AppCompatActivity {
    private List<Diary> diaryList;
    private FirebaseFirestore db;
    RecyclerView cList;
    MyCustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_diary);
        cList = (RecyclerView) findViewById(R.id.cList);
        cList.setHasFixedSize(true);
        cList.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username=load.getString("username","DEFAULT");
        db = FirebaseFirestore.getInstance();
        // we use snapshotListener here so if other users upload new comments, we can see the changes in our view.
        db.collection("Diary").whereEqualTo("username", username).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("3rd", e.getMessage());
                    return;
                }

                diaryList = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("username") != null) {
                        diaryList.add(doc.toObject(Diary.class));
                    }
                }
                mAdapter = new MyCustomAdapter(ViewAllDiaryActivity.this, diaryList);
                cList.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new MyCustomAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Diary diary=diaryList.get(position);
                        Log.e("HAOHUI", "onItemClick: hahaha");
                        CRUDFragment crudFragment = new CRUDFragment();
                        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
                        diary.setUsername(load.getString("displayName","DEFAULT"));
                        Bundle b = new Bundle();
                        b.putSerializable("Diary",diary);

                        crudFragment.setArguments(b);
                        crudFragment.show(getSupportFragmentManager(), "CRUD");
                    }
                });
            }
        });

    }
}
