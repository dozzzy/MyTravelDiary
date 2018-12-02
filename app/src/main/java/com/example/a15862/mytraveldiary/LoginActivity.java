package com.example.a15862.mytraveldiary;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;



import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent=new Intent(this,MapActivity.class);
        startActivity(intent);

    }
}

