package com.affectiva.imagedetectordemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TrainingActivity extends Activity {


    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;


    private int PICK_IMAGE_REQUEST = 1;
@Override
    public void onBackPressed() {
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);


        ImageView btn1 = (ImageView) findViewById(R.id.exitBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }

        });




        Button buttonLoadImage = (Button) findViewById(R.id.btn2);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);*/
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Button buttonTakePicture = (Button) findViewById(R.id.btn1);
        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode ==  RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            //InputStream imageStream = getContentResolver().openInputStream(imageUri);
            System.out.println(imageUri);

            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //   ByteArrayOutputStream stream = new ByteArrayOutputStream();

                String filename = "bitmap.png";
                FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                image.recycle();

                //Pop intent
                Intent in1 = new Intent(TrainingActivity.this, TrainingActivity2.class);
                in1.putExtra("image", filename);
                startActivity(in1);
                /*
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    stream.close();
                    stream = null;
                } catch (IOException e) {

                    e.printStackTrace();
                }

                System.out.println("galleryyyy");
                System.out.println(image);
                Intent i = new Intent(TrainingActivity.this, TrainingActivity2.class).putExtra("img", byteArray);
                startActivity(i);*/
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)  {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            System.out.println("Cameraaaaaa");
            Intent i=new Intent(TrainingActivity.this, TrainingActivity2.class).putExtra("img", image);

            startActivity(i);
        }


    }}