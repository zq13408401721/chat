package com.mychat.anim.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;


/**
 *gif图片解析
 */

public class AnimatedGifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;
    private UpdateListener mListener;

    public AnimatedGifDrawable(){

    }

    /**
     * 不设置宽高
     * @param source
     * @param listener
     */
    public void onCreate(InputStream source,UpdateListener listener){
        onCreate(source,listener,0,0);
    }

    /**
     * 把gif资源文件转成动画
     * @param source
     * @param listener
     * @param width
     * @param height
     */
    public void onCreate(InputStream source, UpdateListener listener,int width,int height) {
        mListener = listener;
        GifDecoder decoder = new GifDecoder();
        decoder.read(source);

        // Iterate through the gif frames, add each as animation frame
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            Bitmap bitmap = decoder.getFrame(i);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            // Explicitly set the bounds in order for the frames to display
            if(width > 0 && height > 0){
                drawable.setBounds(0, 0, width, height);
            }else{
                drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            }
            addFrame(drawable, decoder.getDelay(i));
            if (i == 0) {
                // Also set the bounds for this container drawable
                if(width > 0 && height > 0){
                    setBounds(0, 0, width, height);
                }else{
                    setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                }
            }
        }
    }

    /**
     * Naive method to proceed to next frame. Also notifies listener.
     */
    public void nextFrame() {
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
        if (mListener != null) mListener.update();
    }

    /**
     * Return display duration for current frame
     */
    public int getFrameDuration() {
        return getDuration(mCurrentIndex);
    }

    /**
     * Return drawable for current frame
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    /**
     * Interface to notify listener to update/redraw 
     * Can't figure out how to invalidate the drawable (or span in which it sits) itself to force redraw
     */
    public interface UpdateListener {
        void update();
    }

}
