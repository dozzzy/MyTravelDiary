package com.example.a15862.mytraveldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.background)
                .page(new TitlePage(R.drawable.logo,
                        "Title")
                )
                .page(new BasicPage(R.drawable.logo,
                        "Header",
                        "More text.")
//                        .background(R.color.red_background)
                )
                .page(new BasicPage(R.drawable.logo,
                        "Lorem ipsum",
                        "dolor sit amet.")
                )
                .swipeToDismiss(true)
                .build();
    }
}
