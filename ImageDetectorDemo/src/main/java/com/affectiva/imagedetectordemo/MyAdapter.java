package com.affectiva.imagedetectordemo;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MyAdapter extends PagerAdapter {

    private ArrayList<Integer> images;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Bitmap im = BitmapFactory.decodeResource(context.getResources(), images.get(position));

        Bitmap mm = resize(context.getResources().getDrawable(images.get(position)));
//        Drawable mm = context.getResources().getDrawable(images.get(position));



        System.out.println("i finisshed resizing");
        myImage.setImageBitmap(mm);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private Bitmap resize(Drawable image) {
        System.out.println("i am resizing now");
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Bitmap bitmapResized;
        if (b.getHeight()>1000 || b.getWidth()>1000){
            bitmapResized = Bitmap.createScaledBitmap(b, 256, 256
                    , false);
        }else{
            bitmapResized = Bitmap.createScaledBitmap(b, ((width/2)), (height/2)
                    , false);
        }


        System.out.println("i finisshed resizing");
        return bitmapResized;
    }
}