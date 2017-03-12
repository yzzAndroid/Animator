package com.yzz.android.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yzz on 2017/3/10 0010.
 */
public class MyAnimatorView extends LinearLayout implements Animator.AnimatorListener {


    private int color;
    private int textcolor;
    private List<String> contentList;
    private List<MyTextView> childList;
    private MyWindow windown;
    private ObjectAnimator closeLeft;
    private ObjectAnimator closeRight;
    private ObjectAnimator openLeft;
    private ObjectAnimator openRight;
    private int radius;
    private int instanceX = 0;
    private int instanceY = 0;
    public static final int MARGIN = 100;
    private int menuHeight;
    private boolean isFirst = true;
    private PropertyValuesHolder holder0;
    private PropertyValuesHolder holder1;
    private PropertyValuesHolder holder2;
    private PropertyValuesHolder holder3;
    private PropertyValuesHolder holder4;
    private PropertyValuesHolder holder00;
    private PropertyValuesHolder holderN0;
    private PropertyValuesHolder holderN1;
    private PropertyValuesHolder holderN2;
    private PropertyValuesHolder holderN3;
    private PropertyValuesHolder holderN4;
    public static final int CLOSE = 0;
    public static final int OPEN = 1;
    private int model = -1;
    private boolean isAnimationing = false;

    public MyAnimatorView(Context context) {
        super(context);
    }

    public MyAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void init(int color, int textcolor, List<String> contentList) {
        this.color = color;
        this.textcolor = textcolor;
        this.contentList = contentList;
        setOrientation(HORIZONTAL);
        if (contentList == null || contentList.size() == 0) return;
        int size = contentList.size();
        LayoutParams lp;
        childList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i < size - 1)
                lp.rightMargin = MARGIN;
            else lp.rightMargin = 0;
            MyTextView mtv = new MyTextView(getContext());
            mtv.setText(contentList.get(i));
            mtv.setLayoutParams(lp);
            childList.add(mtv);
            addView(mtv);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst && radius > 0) {
            initAnimation();
        }
    }

    public void initAnimation() {
        instanceX = childList.get(0).getMeasuredWidth() + MARGIN;
        instanceY = getMeasuredHeight() - menuHeight;
        Log.e("=====initAnimation==", "======" + instanceX + "==" + instanceY);
        int translateX = instanceX / 2;

        holder0 = PropertyValuesHolder.ofFloat("translationX", -translateX, 0);
        holder1 = PropertyValuesHolder.ofFloat("translationY", -instanceY, 0);
        holder2 = PropertyValuesHolder.ofFloat("scaleX", 1, 0.5f);
        holder3 = PropertyValuesHolder.ofFloat("scaleY", 1, 0.5f);
        holder4 = PropertyValuesHolder.ofFloat("alpha", 1, 0f);
        closeLeft = ObjectAnimator.ofPropertyValuesHolder(childList.get(0), holder0, holder1, holder2, holder3, holder4);
        closeLeft.addListener(this);
        closeLeft.setDuration(2000);
        holder00 = PropertyValuesHolder.ofFloat("translationX", translateX, 0);
        closeRight = ObjectAnimator.ofPropertyValuesHolder(childList.get(1), holder00, holder1, holder2, holder3, holder4);
        closeRight.setDuration(2000);
        //还原
        holderN0 = PropertyValuesHolder.ofFloat("translationX", 0, -translateX);
        holderN1 = PropertyValuesHolder.ofFloat("translationY", 0, -instanceY);
        holderN2 = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        holderN3 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
        holderN4 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);

        openLeft = ObjectAnimator.ofPropertyValuesHolder(childList.get(0), holderN0, holderN1, holderN2, holderN3, holderN4);
        openLeft.addListener(this);
        openLeft.setDuration(2000);
        PropertyValuesHolder holderN00 = PropertyValuesHolder.ofFloat("translationX", 0, translateX);
        openRight = ObjectAnimator.ofPropertyValuesHolder(childList.get(1), holderN00, holderN1, holderN2, holderN3, holderN4);
        openRight.setDuration(2000);
    }

    public void docloes() {
        if (isAnimationing)return;
        closeLeft.start();
        closeRight.start();
        model = CLOSE;
    }


    public void doOpen() {
        if (isAnimationing)return;
        openLeft.start();
        openRight.start();
        model = OPEN;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (radius <= 0) return;
        int count = getChildCount();
        int h = getMeasuredHeight() - menuHeight;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(instanceX / 2, h, instanceX / 2 + child.getMeasuredWidth(), h + child.getMeasuredHeight());
            child.setScaleX(0.5f);
            child.setScaleY(0.5f);
            child.setAlpha(0f);
        }
        doOpen();
    }

    public List<MyTextView> getChildList() {
        return childList;
    }

    public void setWindow(MyWindow myWindow) {
        windown = myWindow;
    }


    @Override
    public void onAnimationStart(Animator animation) {
        isAnimationing = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (model == CLOSE)
            windown.removeView();
        isAnimationing = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void setRadius(int i) {
        this.radius = i;
    }

    public void setHeight(int height) {
        this.menuHeight = height;
    }


    class MyTextView extends TextView {
        private Paint mPaint;
        private int radius;

        public MyTextView(Context context) {
            super(context);
            init();
        }

        public MyTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            setPadding(20, 20, 20, 20);
            setGravity(Gravity.CENTER);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(color);
            setTextColor(textcolor);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(radius, radius, radius, mPaint);
            super.onDraw(canvas);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            radius = Math.max(getMeasuredWidth(), getMeasuredHeight()) / 2;
            setMeasuredDimension(radius * 2, radius * 2);
        }
    }

}
