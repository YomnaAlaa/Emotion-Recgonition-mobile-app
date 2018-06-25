package com.affectiva.imagedetectordemo;


import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SlideShowActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener, ImageFragment.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener{
    ImageButton btnNext, btnPrevious;
    boolean startFlag = false;
    //    Fragment oldFrag = new BlankFragment();
    Fragment fragment;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragSlider);
//        start();
        next();
        previous();
        final Button btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startFlag==false) {
                    j = 0;
                    LinearLayout ll = (LinearLayout) findViewById(R.id.playButtons);
                    ll.setVisibility(View.VISIBLE);
                    btnStart.setText("Try Now");
                    change();
                    startFlag = true;
                }else{

                }
            }
        });

    }


    void start(){
        new CountDownTimer(3000,2000){
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                change();
            }
        }.start();
    }

    void next(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j>=res.length){
                    Toast.makeText(getBaseContext(), "no more image try now to make the emotio :)", Toast.LENGTH_SHORT).show();
                    btnNext.setEnabled(false);
                }else if (j>=0){
                    change();
                }
            }
        });
    }

    void previous(){
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerFlag==false) {
                    j = j - 2;
                    if(j>=0 && j<res.length)
                        change();
                    else{
                        Toast.makeText(getBaseContext(), "no more image try now to make the emotio :)", Toast.LENGTH_SHORT).show();
                        btnPrevious.setEnabled(false);
                    }

                }
            }
        });
    }


    int j =0;
    String[] res = {"vidfff", "imgslide1","vidfff" ,"imgslide2"};
    String flag = "vid";
    void change() {
        if(btnPrevious.isEnabled()==false &&j>=0){
            btnPrevious.setEnabled(true);
        }
        if(btnNext.isEnabled()==false && j<res.length){
            btnNext.setEnabled(true);
        }
        if(j<res.length) {

            if(timerFlag==true){
                Toast.makeText(getBaseContext(), "wait to concentrate", Toast.LENGTH_SHORT).show();
            }else {
                if (flag.equals("vid")) {
                    startVideo(j);
                } else {
                    startImage(j);
                }
            }
        }
    }


    void startVideo(int pos){

        String VidName = res[pos];
        int id = getResources().getIdentifier(VidName, "raw", getPackageName());
        final String path = "android.resource://" + getPackageName() + "/" + id;
        System.out.println("video path sent is: "+VidName+" "+j+" "+path);

        Bundle bundle = new Bundle();
        bundle.putString("vid", path);
        Fragment newFrag = new VideoFragment();
        newFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        transaction.replace(R.id.fragSlider, newFrag);


        transaction.addToBackStack(null);
        transaction.commit();
        flag = "img";
        j++;
//        oldFrag = newFrag;

    }

    boolean timerFlag = false;
    void startImage(final int pos){
        String ImageName = res[pos];

        int id = getResources().getIdentifier(ImageName, "drawable", getPackageName());
        final String path = "android.resource://" + getPackageName() + "/" + id;
        Bundle bundle = new Bundle();
        bundle.putString("img", path);
        System.out.println("image path sent is: "+ImageName+" "+j+" "+path);
        Fragment newFrag = new ImageFragment();
        newFrag.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        transaction.replace(R.id.fragSlider, newFrag);
//                transaction.remove(oldFrag);
        transaction.addToBackStack(null);
        transaction.commit();
//                oldFrag = newFrag;

        timerFlag = false;
        flag = "vid";
        j++;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
