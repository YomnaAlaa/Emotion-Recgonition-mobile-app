package com.affectiva.imagedetectordemo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class slideShow2Activity extends Activity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    Button btnstart;

    private static final Integer[] happyFaces = {R.drawable.happy1, R.drawable.happy2, R.drawable.happy3};
    private static final Integer[] sadFaces = {R.drawable.sad1, R.drawable.sad2, R.drawable.sad3};
    private static final Integer[] angryFaces = {R.drawable.angry5, R.drawable.angry4, R.drawable.angry3};
    private static final Integer[] disgustFaces = {R.drawable.disgust1, R.drawable.disgust2, R.drawable.disgust3};
    private static final Integer[] fearFaces = {R.drawable.fear1, R.drawable.fear2, R.drawable.fear3};
    private static final Integer[] surpriseFaces = {R.drawable.surprise1, R.drawable.surprise2, R.drawable.surprise4};
    String emojie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show2);
        Intent getFromLast = getIntent();
        emojie = getFromLast.getStringExtra("emojie");
        btnstart = (Button)findViewById(R.id.btnLetsStart);
        init();
    }

    private void init() {

        switch (emojie) {
            case "happy":
                for (int i = 0; i < happyFaces.length; i++) {
                    XMENArray.add(happyFaces[i]);

                }
                break;
            case "sad":
                for (int i = 0; i < sadFaces.length; i++) {
                    XMENArray.add(sadFaces[i]);

                }
                break;
            case "angry":
                for (int i = 0; i < angryFaces.length; i++) {
                    XMENArray.add(angryFaces[i]);
                }
                break;
            case "surprise":
                for (int i = 0; i < surpriseFaces.length; i++) {
                    XMENArray.add(surpriseFaces[i]);
                }
                break;
            case "fear":
                for (int i = 0; i < fearFaces.length; i++) {
                    XMENArray.add(fearFaces[i]);
                }
                break;
            case "disgust":
                for (int i = 0; i < disgustFaces.length; i++) {
                    XMENArray.add(disgustFaces[i]);
                }
                break;
        }


        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(slideShow2Activity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        new CountDownTimer(3500,3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                btnstart.setEnabled(true);
            }
        }.start();

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(slideShow2Activity.this, TrainingActivity.class);
                SharedPreferences sh = getSharedPreferences("TrainingEmotion", MODE_PRIVATE);
                SharedPreferences.Editor editor = sh.edit();
                editor.putString("emoji",emojie);
                editor.apply();
                startActivity(ii);
            }
        });

//

//        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == happyFaces.length) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 2500, 2500);
    }
}


