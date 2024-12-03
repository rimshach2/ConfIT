package com.fyp.confit;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashPage extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_page );

        final Animation anim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        anim.setDuration(1200);
        anim.reset();
        ImageView splash = findViewById(R.id.splash);
        splash.clearAnimation();
        splash.startAnimation(anim);

        Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashPage.this, LoginPage.class);
                startActivity(i);
            }
        }, 1200);

    }
}
