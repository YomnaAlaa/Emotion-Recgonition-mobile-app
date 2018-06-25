package com.affectiva.imagedetectordemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TempActivity extends Activity {
    Bitmap bitmap1,bitmap2,bitmap3,bitmap4,bitmap5,bitmap6;

    Bitmap[] resizedImages = new Bitmap[6];
    Context context;
    Bitmap[] images;
    Button btnHappy, btnSad, btnAngry, btnSurprise, btnDisgust, btnFear;
    Intent toSlideShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.angry2);//assign your bitmap;
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.disgust);//assign your bitmap;
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.fear);//assign your bitmap;
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.happy);//assign your bitmap;
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.sad);//assign your bitmap;
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.surprise);//assign your bitmap;
        images = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6};
        float metrics = getResources().getDisplayMetrics().density;
        System.out.println(metrics);
        //mdpi
        if (metrics==1.0){
            for (int i = 0; i < images.length; i++) {
                resizedImages[i] = Bitmap.createScaledBitmap(images[i], 96, 96, true);
            }
        }
        //hdpi
        else if (metrics==1.5){
            for (int i = 0; i < images.length; i++) {
                resizedImages[i] = Bitmap.createScaledBitmap(images[i], 144, 144, true);
            }
        }
        //xhdpi
        else if (metrics==2.0){
            for (int i = 0; i < images.length; i++) {
                System.out.println(images[i]+" "+ i+"");
                resizedImages[i] = Bitmap.createScaledBitmap(images[i], 192, 192, true);

            }
        }
        //xxhdpi
        else if (metrics==3.0){
            for (int i = 0; i < images.length; i++) {
                resizedImages[i] = Bitmap.createScaledBitmap(images[i], 240, 240, true);
            }
        }
        //xxxhdpi
        else if (metrics==4.0){
            for (int i = 0; i < images.length; i++) {
                resizedImages[i] = Bitmap.createScaledBitmap(images[i], 288, 288, true);
            }
        }else{
            resizedImages=images;
        }
        BitmapDrawable bdrawable[] = new BitmapDrawable[resizedImages.length];
        for (int i = 0; i < resizedImages.length; i++) {
            bdrawable[i] = new BitmapDrawable(getResources(),resizedImages[i]);
        }
        btnAngry = (Button)findViewById(R.id.btnAngry);
        btnSad = (Button)findViewById(R.id.btnSad);
        btnHappy = (Button)findViewById(R.id.btnHappy);
        btnSurprise = (Button)findViewById(R.id.btnSurprise);
        btnFear = (Button)findViewById(R.id.btnFear);
        btnDisgust = (Button)findViewById(R.id.btnDisgust);



        btnHappy.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[3], null, null);
        System.out.println(bdrawable[3]);
        btnSurprise.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[5], null, null);
        btnSad.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[4], null, null);
        btnFear.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[2],null, null);
        btnAngry.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[0], null, null);
        btnDisgust.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[1], null, null);

        toSlideShow = new Intent(TempActivity.this, slideShow2Activity.class);

        btnAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("angry");
            }
        });

        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("happy");
            }
        });

        btnFear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("fear");
            }
        });

        btnSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("sad");
            }
        });

        btnSurprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("surprise");
            }
        });

        btnDisgust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmojie("disgust");
            }
        });

    }

    void chooseEmojie(String name){
        switch (name){
            case "happy":
                toSlideShow.putExtra("emojie", "happy");
                break;
            case "sad":
                toSlideShow.putExtra("emojie", "sad");
                break;
            case "angry":
                toSlideShow.putExtra("emojie", "angry");
                break;
            case "surprise":
                toSlideShow.putExtra("emojie", "surprise");
                break;
            case "fear":
                toSlideShow.putExtra("emojie", "fear");
                break;
            case "disgust":
                toSlideShow.putExtra("emojie", "disgust");
                break;
        }
        startActivity(toSlideShow);
    }

}

