package com.mychat.utils;

import android.content.Context;
import android.util.TypedValue;

public class DpTools {
    public static int dp2px(Context context, int dpValue){
        //return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dp2pxF(Context context, float dpValue){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }
}
