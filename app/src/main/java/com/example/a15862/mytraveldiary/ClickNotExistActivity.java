package com.example.a15862.mytraveldiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClickNotExistActivity extends AppCompatActivity {
    private Button btnJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_not_exist);

        btnJump = (Button) findViewById(R.id.btnJump);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClickNotExistActivity.this, AddDiaryActivity.class);
                startActivity(intent);
            }
        });
    }
}
