package com.iir4.g2.gmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductiveActivity extends AppCompatActivity {

    ImageView img;
    LottieAnimationView lav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductive);

        img = findViewById(R.id.img);
        lav = findViewById(R.id.lottie);

        img.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        lav.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(IntroductiveActivity.this, MainActivity.class);
                    startActivity(intent);
                    IntroductiveActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();


    }
}