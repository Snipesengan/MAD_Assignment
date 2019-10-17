package curtin.edu.au.mad_assignment.model;

import android.graphics.Bitmap;

import java.io.Serializable;


public class ProxyBitmap implements Serializable {

    private final int width,height;
    private final int[] pixels;

    public ProxyBitmap(Bitmap bitmap){
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int [width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
    }

    public Bitmap getBitmap(){
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

}
