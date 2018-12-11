package com.example.a15862.mytraveldiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
//    }

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")
                .defaultBackgroundColor(R.color.colorPrimary)
                .page(new TitlePage(R.drawable.logo,
                        "MyTravelDiary")
                        .titleColor(getResources().getColor(R.color.white))
                )
                .page(new ParallaxPage(R.layout.activity_welcome2,
                        "Easy parallax",
                        "Supply a layout and parallax effects will automatically be applied")
                        .lastParallaxFactor(2f)
                        .background(R.color.colorPrimary)
                )


                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
}
