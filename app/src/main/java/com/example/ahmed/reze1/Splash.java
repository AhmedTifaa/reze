package com.example.ahmed.reze1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.ahmed.reze1.app.AppConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity {
 private ImageView imageView;
 int duration = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView)findViewById(R.id.loading_bar);
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_1), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_2), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_3), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_4), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_5), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_6), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_7), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_8), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_9), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_10), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_11), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_12), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_13), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_14), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_15), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_16), duration);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.ic_image2vector_17), duration);
        animationDrawable.setOneShot(false);
        imageView.setImageDrawable(animationDrawable);

        animationDrawable.start();
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
//                                          SharedPreferences.Editor editor = getSharedPreferences(AppConfig.SHARED_PREFERECE_NAME, MODE_PRIVATE).edit();
//                                          editor.putBoolean(AppConfig.LOGGED_IN_SHARED, true).
//                                                  putString(AppConfig.LOGGED_IN_USER_ID_SHARED, "3101").apply();
                                         Intent myIntent = new Intent(getApplicationContext(), Login.class);
                                          //myIntent.putExtra("user_id",3101);
                                          startActivity(myIntent);
                                          finish();

                                      }
                                  }
                , 3400);
    }
}

