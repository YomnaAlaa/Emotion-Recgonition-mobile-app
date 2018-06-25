package com.affectiva.imagedetectordemo;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.hardware.Camera.ShutterCallback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.util.Base64;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.io.FileOutputStream;


import android.hardware.Camera.PictureCallback;

import com.squareup.picasso.Picasso;

public class trainingModeActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback,Detector.ImageListener {

    static int i = 0;
    File path = null;
    File f = null;
    FileOutputStream outStream = null;
    static Camera mCamera=null;
    String SD_CARD_DIR;
    SurfaceView mPreview;
    String filePath ;
    int currentCameraId = 0;
    ImageButton capture;
    Button switchBtn;
    ShutterCallback shutterCallback;
    PictureCallback rawCallback;
    ImageSaver imageSaver;
    ImageView resultImage;
    public static final int PICK_IMAGE = 100;
    LinearLayout cont;
    Button recBtn;
    Button guess;
    PhotoDetector detector;
    Bitmap bitmap = null;
    Frame.BitmapFrame frame;
    LinearLayout buttons;
    TableLayout rotations;
    static boolean photoSet=false;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_training_mode);
        ImageView btn1 = (ImageView) findViewById(R.id.exitBtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }

        });


        rotations=(TableLayout)findViewById(R.id.rotation_buttons);
        imageSaver=new ImageSaver(this);
        guess=(Button)findViewById(R.id.guessBtn);
        buttons=(LinearLayout)findViewById(R.id.buttons);
        cont = (LinearLayout)findViewById(R.id.con);
        resultImage=(ImageView)findViewById(R.id.imageCptrd);
        mPreview = (SurfaceView) findViewById(R.id.camera_surface);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        try {
            mCamera = Camera.open(currentCameraId);
        }
        catch (Exception e)
        {
            Log.e("Error","error");
        }
        shutterCallback = new ShutterCallback(){@Override public void onShutter() {Log.d("Preview", "onShutter Called"); }};
        rawCallback = new PictureCallback() {  @Override public void onPictureTaken(byte[] data, Camera camera) {Log.d("Preview", "onPictureRaw Called"); }};
        switchBtn = (Button) findViewById(R.id.switchBtn);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) { mCamera.stopPreview();}
                mCamera.release();
                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                mCamera = Camera.open(currentCameraId);
                setCameraDisplayOrientation(trainingModeActivity.this, currentCameraId, mCamera);
                try { mCamera.setPreviewDisplay(mPreview.getHolder()); } catch (IOException e) { e.printStackTrace(); }
                mCamera.startPreview(); }});
        capture=(ImageButton)findViewById(R.id.captureBtn);
        capture.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { captureImage();}});
        recBtn=(Button)findViewById(R.id.recognizeBtn);
        recBtn.setOnClickListener(new View.OnClickListener() {
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
                    Intent in1 = new Intent(trainingModeActivity.this, MainActivity.class);
                    in1.putExtra("image", filename);
                    startActivity(in1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(trainingModeActivity.this, Guess.class);
                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bitmap.recycle();

                    //Pop intent

                    i.putExtra("image", filename);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void captureImage(){ mCamera.takePicture(shutterCallback, rawCallback, this);}
    public static void setCameraDisplayOrientation(Activity activity,int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
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


        frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);

        detector = new PhotoDetector(this,1, Detector.FaceDetectorMode.LARGE_FACES );
        detector.setImageListener(this);

        startDetector();
        detector.process(frame);
        stopDetector();
        //  resultImage.setImageBitmap(bitmap);




    }


    void setAndProcessBitmap2(Frame.ROTATE rotation, boolean isExpectingFaceDetection) {
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


        if(!photoSet) {
            int w = getWindowManager().getDefaultDisplay().getWidth();
            int h = getWindowManager().getDefaultDisplay().getHeight() / 2;
            frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);

            Bitmap imageBitmap = drawCanvas(w, h, null, frame, null);
            if (imageBitmap != null) {
                resultImage.setImageBitmap(imageBitmap);
                mPreview.setVisibility(View.INVISIBLE);
                switchBtn.setVisibility(View.INVISIBLE);
                capture.setVisibility(View.INVISIBLE);
                resultImage.setVisibility(View.VISIBLE);
                buttons.setVisibility(View.VISIBLE);
                rotations.setVisibility(View.VISIBLE);
            }
        }

        else
        {
            resultImage.setImageBitmap(bitmap);


        }




    }


    @Override
    public void onPause() {
        super.onPause();
        try
        {  mCamera.stopPreview();}
        catch (Exception e)
        {
            Log.e("Error","error");
        }



    }


    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }
    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    @Override
    public void onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        // setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);
        setAndProcessBitmap2(Frame.ROTATE.NO_ROTATION, true);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Result", "onActivityForResult");
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

            } catch (IOException e) {
                Toast.makeText(this,"Unable to open image.",Toast.LENGTH_LONG).show();
            }
            setAndProcessBitmap2(Frame.ROTATE.NO_ROTATION, true);
            //setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, true);

        } else {
            Toast.makeText(this,"No image selected.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
            case Surface.ROTATION_90: degrees = 90; break; //Landscape left
            case Surface.ROTATION_180: degrees = 180; break;//Upside down
            case Surface.ROTATION_270: degrees = 270; break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        try
        {  Camera.Parameters params = mCamera.getParameters();
            params.setRotation(rotate);
            mCamera.setParameters(params);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();}
        catch(Exception e)
        {
            Log.e("Error","error");

        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW","surfaceDestroyed");
    }
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
        Matrix matrix = new Matrix();
        matrix.postRotate((float)frameRot.toDouble());
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,frameWidth,frameHeight,matrix,false);
        c.drawBitmap(rotatedBitmap,null,new Rect(leftOffset,topOffset,leftOffset+scaledWidth,topOffset+scaledHeight),null);
        return blackBitmap;
    }
    @Override
    public void onImageResults(List<Face> faces, Frame image, float timestamp) {
        PointF[] points = null;
        if (faces != null && faces.size() > 0) {
            Face face = faces.get(0);
            points = face.getFacePoints();
        }
        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
        int w=getWindowManager().getDefaultDisplay().getWidth();
        int h =getWindowManager().getDefaultDisplay().getHeight()/2;
        Bitmap imageBitmap = drawCanvas(w,h,points,image,circlePaint);
        if (imageBitmap != null)
        {
            resultImage.setImageBitmap(imageBitmap);
            mPreview.setVisibility(View.INVISIBLE);
            switchBtn.setVisibility(View.INVISIBLE);
            capture.setVisibility(View.INVISIBLE);
            resultImage.setVisibility(View.VISIBLE);
            buttons.setVisibility(View.VISIBLE);
            rotations.setVisibility(View.VISIBLE);
        }
    }

    public void rotate_left(View view) {
        setAndProcessBitmap2(Frame.ROTATE.BY_90_CCW, true);
    }

    public void rotate_right(View view) {
        setAndProcessBitmap2(Frame.ROTATE.BY_90_CW,true);
    }

    @Override
    public void onBackPressed() {
    }
}