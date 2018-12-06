package com.example.a15862.mytraveldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a15862.mytraveldiary.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class ViewAllFriendsActivity extends AppCompatActivity {
    List<User> friends = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_friends);

    }
}
