package com.affectiva.imagedetectordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.TableLayout;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class TrainingActivity2 extends Activity {

    Button btn2;
    Bitmap bitmap=null;
    TableLayout rotations;
    Frame.BitmapFrame frame;

    ImageView imageView;

    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training2);

        ImageView btn1 = (ImageView) findViewById(R.id.exitBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }

        });



        rotations=(TableLayout)findViewById(R.id.rotation_buttons);
        imageView = (ImageView) findViewById(R.id.imgView);
        Intent intent = getIntent();

        if ((intent.getParcelableExtra("img")) instanceof Bitmap )
        {
            bitmap = (Bitmap) intent.getParcelableExtra("img");
        }
        else
        {
            String filename = getIntent().getStringExtra("image");
            try {
                FileInputStream is = this.openFileInput(filename);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println(bitmap);
        imageView.setImageBitmap(bitmap);



        btn2= (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bitmap.recycle();

                    //Pop intent

                    Intent i =new Intent(TrainingActivity2.this, MainActivity.class);
                    i.putExtra("image", filename);

                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void setAndProcessBitmap(Frame.ROTATE rotation, boolean isExpectingFaceDetection) {
        if (bitmap == null) {
            return;
        }
        switch (rotation) {
            case BY_90_CCW:
                bitmap = Frame.rotateImage(bitmap,-90);
                break;
            case BY_90_CW:
                bitmap = Frame.rotateImage(bitmap,90);
                break;
            case BY_180:
                bitmap = Frame.rotateImage(bitmap,180);
                break;
            default:
                //keep bitmap as it is
        }
        imageView.setImageBitmap(bitmap);




    }

    public void rotate_left(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CCW, true);
    }

    public void rotate_right(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CW,true);
    }
}
