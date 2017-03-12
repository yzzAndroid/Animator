package com.yzz.android.animator;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.text.Format;

/**
 * Created by yzz on 2017/3/10 0010.
 */
public class MyWindow {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLp;
    private Activity context;
    private View view;

    public MyWindow(Activity context) {
        this.context = context;
        mWindowManager = context.getWindowManager();
        mLp = new WindowManager.LayoutParams();
        mLp.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLp.x = 0;
        mLp.y = 0;
        mLp.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLp.flags = WindowManager.LayoutParams.FLAG_DITHER;
        mLp.format = PixelFormat.TRANSLUCENT;
    }

    public void addView(View view){
        this.view = view;
        mWindowManager.addView(view,mLp);
    }

    public void removeView(){
        if (mWindowManager==null)return;
        mWindowManager.removeView(view);
        context = null;
    }
}
