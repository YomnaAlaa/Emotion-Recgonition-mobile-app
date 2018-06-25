package com.affectiva.imagedetectordemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * A sample app showing how to use ImageDetector.
 *
 * This app is not a production release and is known to have bugs. Specifically, the UI thread is blocked while the image is being processed,
 * and the app will crash if the user tries loading a very large image.
 *
 * For some images, facial tracking dots will not appear in the correct location.
 *
 * Also, the UI element that displays metrics is not aesthetic.
 *
 */
public class MainActivity extends Activity implements Detector.ImageListener {

    public static final String LOG_TAG = "Affectiva";
    public static final int PICK_IMAGE = 100;

    ImageView imageView;
    TextView metricScoreTextViews;

    LinearLayout metricsContainer;


    PhotoDetector detector;
    Bitmap bitmap = null;
    Frame.BitmapFrame frame;

    TextView txt, txt2;

    ImageView img1;
    String res, emoji;
    DatabaseHelper db;
    UserInfo info;
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "i am the main activity", Toast.LENGTH_SHORT).show();
        initUI();

        ImageView btn1 = (ImageView) findViewById(R.id.exitBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }

        });


        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(is);
            Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(LOG_TAG, "onCreate");

        try {
            loadInitialImage();
        } catch (IOException ioe) {
            Log.e(LOG_TAG, "loading initial image", ioe);
        }



    }

    private int getId(String username){
        Cursor c = db.getid(username);
        int id= 0;
        if (c.moveToFirst()) {
            do {
                // Process the data:
                id = c.getInt(DatabaseHelper.COL_USERID);
                // Append data to the message:
                Toast.makeText(this, "this is db in the getID----->"+id, Toast.LENGTH_SHORT).show();
                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
            } while (c.moveToNext());
        }
        Log.i("id----===>",id+"");
        c.close();
        return  id;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume");

    }

    void loadInitialImage() throws IOException {
        if (bitmap == null) {
            bitmap = getBitmapFromAsset(this, "images/default.jpg");
        }
        setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);
    }

    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "onPause");

    }

    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    void initUI() {
       // metricsContainer = (LinearLayout) findViewById(R.id.metrics_container);
       // metricScoreTextViews = MetricsPanelCreator.createScoresTextViews();
        //MetricsPanelCreator.populateMetricsContainer(metricsContainer,metricScoreTextViews,this);

        imageView = (ImageView) findViewById(R.id.image_view);
        txt=(TextView)findViewById(R.id.txt);
        img1 = (ImageView) findViewById(R.id.img1);



    }



    public Bitmap getBitmapFromAsset(Context context, String filePath) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap;
        istr = assetManager.open(filePath);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream istr;
        Bitmap bitmap;
        istr = getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

   /* public void select_new_image(View view) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }*/

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

        frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);

        detector = new PhotoDetector(this,1, Detector.FaceDetectorMode.LARGE_FACES );
        detector.setDetectAllEmotions(true);
        detector.setDetectAllExpressions(true);
        detector.setDetectAllAppearances(true);
        detector.setImageListener(this);

        startDetector();
        detector.process(frame);
        stopDetector();

    }

    @SuppressWarnings("SuspiciousNameCombination")
    Bitmap drawCanvas(int width, int height, PointF[] points, Frame frame, Paint circlePaint) {
        if (width <= 0 || height <= 0) {
            return null;
        }

        Bitmap blackBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        blackBitmap.eraseColor(Color.BLACK);
        Canvas c = new Canvas(blackBitmap);

        Frame.ROTATE frameRot = frame.getTargetRotation();
        Bitmap bitmap;

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int canvasWidth = c.getWidth();
        int canvasHeight = c.getHeight();
        int scaledWidth;
        int scaledHeight;
        int topOffset = 0;
        int leftOffset= 0;
        float radius = (float)canvasWidth/100f;

        if (frame instanceof Frame.BitmapFrame) {
            bitmap = ((Frame.BitmapFrame)frame).getBitmap();
        } else { //frame is ByteArrayFrame
            byte[] pixels = ((Frame.ByteArrayFrame)frame).getByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(pixels);
            bitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
        }

        if (frameRot == Frame.ROTATE.BY_90_CCW || frameRot == Frame.ROTATE.BY_90_CW) {
            int temp = frameWidth;
            frameWidth = frameHeight;
            frameHeight = temp;
        }

        float frameAspectRatio = (float)frameWidth/(float)frameHeight;
        float canvasAspectRatio = (float) canvasWidth/(float) canvasHeight;
        if (frameAspectRatio > canvasAspectRatio) { //width should be the same
            scaledWidth = canvasWidth;
            scaledHeight = (int)((float)canvasWidth / frameAspectRatio);
            topOffset = (canvasHeight - scaledHeight)/2;
        } else { //height should be the same
            scaledHeight = canvasHeight;
            scaledWidth = (int) ((float)canvasHeight*frameAspectRatio);
            leftOffset = (canvasWidth - scaledWidth)/2;
        }

        float scaling = (float)scaledWidth/(float)frame.getOriginalBitmapFrame().getWidth();

        Matrix matrix = new Matrix();
        matrix.postRotate((float)frameRot.toDouble());
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,frameWidth,frameHeight,matrix,false);
        c.drawBitmap(rotatedBitmap,null,new Rect(leftOffset,topOffset,leftOffset+scaledWidth,topOffset+scaledHeight),null);


       /* if (points != null) {
            //Save our own reference to the list of points, in case the previous reference is overwritten by the main thread.

            for (PointF point : points) {

                //transform from the camera coordinates to our screen coordinates
                //The camera preview is displayed as a mirror, so X pts have to be mirrored back.
                float x = (point.x * scaling) + leftOffset;
                float y = (point.y * scaling) + topOffset;

                c.drawCircle(x, y, radius, circlePaint);
            }
        }*/

        return blackBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LOG_TAG, "onActivityForResult");
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

            } catch (IOException e) {
                Toast.makeText(this,"Unable to open image.",Toast.LENGTH_LONG).show();
            }

            setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);

        } else {
            Toast.makeText(this,"No image selected.",Toast.LENGTH_LONG).show();
        }
    }

    public void rotate_left(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CCW, true);
    }

    public void rotate_right(View view) {
        setAndProcessBitmap(Frame.ROTATE.BY_90_CW,true);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame image, float timestamp) {

        PointF[] points = null;

        if (faces != null && faces.size() > 0) {
            Face face = faces.get(0);
            setMetricTextViewText(face);
            points = face.getFacePoints();
        } /*else {
            for (int n = 0; n < MetricsManager.getTotalNumMetrics(); n++) {
                metricScoreTextViews[n].setText("---");
            }
        }*/

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        int w=getWindowManager().getDefaultDisplay().getWidth();
        int h =getWindowManager().getDefaultDisplay().getHeight()/2;
        Bitmap imageBitmap = drawCanvas(w,h,points,image,circlePaint);
        if(imageBitmap!=null)
            imageView.setImageBitmap(imageBitmap);
    }

    private void setMetricTextViewText(Face face) {
        // set the text for all the numeric metrics (scored or measured)
        float max=0;
        float score=0;
        int maxEmotion=0;
        String maxEmo="";
        for (int n = 0; n < 8; n++) {
            score=getScore(n,face);
            if (score>max) {
                max = score;
                maxEmotion=n;
                System.out.println("Score is "+ score+ " "+ maxEmotion);

            }

            //   metricScoreTextViews[n].setText(String.format("%.3f", getScore(n, face)));
        }

        switch (maxEmotion){
            case 0:
                maxEmo="Anger";
                break;
            case 1:
                maxEmo="Contempt";
                break;
            case 2:
                maxEmo="Disgust";
                break;
            case 3:
                maxEmo="Engagement";
                break;
            case 4:
                maxEmo="Fear";
                break;
            case 5:
                maxEmo="Joy";
                break;
            case 6:
                maxEmo="Sad";
                break;
            case 7:
                maxEmo="Surprise";

        }
        System.out.println("Score is "+ max+ " "+ maxEmo);
        if (maxEmo=="Joy"|| maxEmo=="Engagement")
            maxEmo="Happy";
        if (maxEmo=="Contempt")
            maxEmo="Angry";
        showScore(maxEmo);

    }

public void showScore(String Emo){



    res =getIntent().getStringExtra("result");
       String dialogMessage="";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        int score=0;
    SharedPreferences sh = getSharedPreferences("TrainingEmotion", MODE_PRIVATE);
    emoji = sh.getString("emoji", "");
    if (Emo =="Happy")
        img1.setImageResource(R.drawable.happy);
    if (Emo =="Sad")
        img1.setImageResource(R.drawable.sad);
    if (Emo =="Angry")
        img1.setImageResource(R.drawable.angry2);
    if (Emo =="Fear")
        img1.setImageResource(R.drawable.fear);
    if (Emo =="Disgust")
        img1.setImageResource(R.drawable.disgust);
    if (Emo =="Surprise")
        img1.setImageResource(R.drawable.surprise);
    txt.setText(Emo);

    if (res!=null) {

        if (res.equals(Emo)) {
//            Random e = new Random();
            score++;
           // builder1.setMessage("Your answer is correct!");
            dialogMessage="Your answer is correct!";
        } else {
           // builder1.setMessage("Your answer is wrong!");
            dialogMessage="Your answer is wrong!";

        }
        System.out.print("SCORE IS  "+score);
        db= new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        String username=pref.getString("userName","");
        int userId=getId(username);
        info= new UserInfo(userId);
         info.setScore(score);
         info.setUsertime(Calendar.getInstance().getTime().toString());
         db.InsertuserInfo(info);

    }
    else if (emoji !=null){

        String emojiResult=emoji.toLowerCase();
        String EmoVal= Emo.toLowerCase();

        if(emojiResult.equals(EmoVal))
        {
            dialogMessage = "That's correct!";
//            Random e = new Random();
            score++;

        }
        else
        {
            dialogMessage = "That's wrong, try again";

        }
        System.out.print("SCORE IS  "+score);
        db= new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        String username=pref.getString("userName","");
        int userId=getId(username);
        info= new UserInfo(userId);
        info.setScore(score);
        info.setUsertime(Calendar.getInstance().getTime().toString());
        db.InsertuserInfo(info);


    }

    builder1.setMessage(dialogMessage);
           builder1.setCancelable(true);
           builder1.setPositiveButton(
                   "Done",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {

                       Intent i = new Intent( MainActivity.this, scoreActivity.class);
                       startActivity(i);

                       }


                   });
           builder1.setNegativeButton(
                   "Keep playing",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           Intent i;
                           if (res!=null){
                               i  =new Intent(MainActivity.this, trainingModeActivity.class);
                               startActivity(i);
                           }
                           else{
                               i =new Intent(MainActivity.this, TrainingActivity.class);
                               startActivity(i);}
                       }
                   });

           AlertDialog alert11 = builder1.create();
           alert11.show();


       }

    float getScore(int metricCode, Face face) {

        float score;

        switch (metricCode) {
            case MetricsManager.ANGER:
                score = face.emotions.getAnger();
                System.out.println("Anger");
                break;
            case MetricsManager.CONTEMPT:
                score = face.emotions.getContempt();
                System.out.println("Contempt");
                break;
            case MetricsManager.DISGUST:
                score = face.emotions.getDisgust();
                System.out.println("Disgust");
                break;
            case MetricsManager.FEAR:
                score = face.emotions.getFear();
                System.out.println("Fear");
                break;
            case MetricsManager.JOY:
                score = face.emotions.getJoy();
                System.out.println("Joy");
                break;
            case MetricsManager.SURPRISE:
                score = face.emotions.getSurprise();
                System.out.println("Surprise");
                break;
            case MetricsManager.SADNESS:
                score = face.emotions.getSadness();
                System.out.println("Sadness");
                break;
            case MetricsManager.ENGAGEMENT:
                score = face.emotions.getEngagement();
                System.out.println("Engagement");
                break;

            default:
                score = Float.NaN;
                break;
        }
        return score;
    }
}
