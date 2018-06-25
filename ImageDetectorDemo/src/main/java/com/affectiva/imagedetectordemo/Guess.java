package com.affectiva.imagedetectordemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Guess extends Activity implements View.OnClickListener {

    String result;
    String filename;


    Bitmap bitmap1,bitmap2,bitmap3,bitmap4,bitmap5,bitmap6;

    Bitmap[] resizedImages = new Bitmap[6];
    Context context;
    Bitmap[] images;
    Button happy, sad, angry, surprise, disgust, fear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);


        happy = (Button) findViewById(R.id.btnHappy);
        happy.setOnClickListener(this); // calling onClick() method
        sad = (Button) findViewById(R.id.btnSad);
        sad.setOnClickListener(this);
        angry = (Button) findViewById(R.id.btnAngry);
        angry.setOnClickListener(this);
        fear = (Button) findViewById(R.id.btnFear);
        fear.setOnClickListener(this); // calling onClick() method
        disgust = (Button) findViewById(R.id.btnDisgust);
        disgust.setOnClickListener(this);
        surprise = (Button) findViewById(R.id.btnSurprise);
        surprise.setOnClickListener(this);
        filename = getIntent().getStringExtra("image");

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


            happy.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[3], null, null);
            System.out.println(bdrawable[3]);
            surprise.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[5], null, null);
            sad.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[4], null, null);
            fear.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[2],null, null);
            angry.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[0], null, null);
            disgust.setCompoundDrawablesWithIntrinsicBounds(null, bdrawable[1], null, null);

            }
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnHappy:
                result="Happy";
                break;

            case R.id.btnSad:
                result="Sad";
                break;

            case R.id.btnAngry:
                result="Angry";
                break;

            case R.id.btnFear:
                result="Fear";
                break;
            case R.id.btnDisgust:
                result="Disgusted";
                break;
            case R.id.btnSurprise:
                result="Surprise";
                break;

            default:
                break;
        }
        Intent i= new Intent (Guess.this, MainActivity.class).putExtra("result", result);
        i.putExtra("image", filename);
        startActivity(i);

    }
}
