package com.yzz.android.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

/**
 * Created by yzz on 2017/3/10 0010.
 */
public class MyMenuView extends ViewGroup {

    private int color = Color.RED;
    private Paint mPaint;
    private TextView textView;
    public static final int TO_SPECIAL = 0;
    public static final int TO_NOMAL = 1;
    //文字的左右距离
    public static final int MARGIN_LEFT_TEXT = 20;
    public static final int MARGIN_TOP_TEXT = 50;
    private static final int MARGIN_BOTTOM_TEXT = 5;
    public static final int MARGIN_IMG_TOP = 15;
    private int marginLeftOrRightText = MARGIN_LEFT_TEXT;
    private int marginTopText = MARGIN_TOP_TEXT;
    private int imgMarginTop = MARGIN_IMG_TOP;
    private int marginBottomText = MARGIN_BOTTOM_TEXT;
    private int imgRadius ;
    private MyLitterXView myLitterXView;
    private ClickListener mClickListener;
    private boolean isAnimationing = false;


    public MyMenuView(Context context) {
        super(context);
    }

    public MyMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();//这里防止出现异常(具体什么异常我忘记了^_^)
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //强制让容器执行绘制工作
        setWillNotDraw(false);
        textView = new TextView(getContext());
        textView.setText("发布");
        textView.setTextColor(Color.WHITE);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        addView(textView);
        myLitterXView = new MyLitterXView(getContext());
        myLitterXView.setLayoutParams(lp);
        addView(myLitterXView);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener!=null)mClickListener.click(v);
                myLitterXView.change(myLitterXView.model==TO_SPECIAL?TO_NOMAL:TO_SPECIAL);
            }
        });
    }

    public void change(int model){
        myLitterXView.change(model);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,getMeasuredWidth()/2,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        RectF rectF = new RectF(0,0,getMeasuredWidth(),getMeasuredHeight()/2);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredWidth()/2,getMeasuredWidth()/2,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int w = getChildAt(0).getMeasuredWidth();
        int h = getChildAt(0).getMeasuredHeight();
        imgRadius = w/2;
        myLitterXView.init();
        int width = w + marginLeftOrRightText * 2;
        int height =  h + marginBottomText + marginTopText + imgRadius + width/2;

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View view = getChildAt(0);
        view.layout(
                marginLeftOrRightText,
                getMeasuredHeight()-view.getMeasuredHeight()-marginBottomText,
                marginLeftOrRightText+view.getMeasuredWidth(),
                getMeasuredHeight()-marginBottomText
                );
        View v = getChildAt(1);
        v.layout(
                marginLeftOrRightText,imgMarginTop,getMeasuredWidth()-marginLeftOrRightText,imgMarginTop+imgRadius*2
        );
    }


    public interface ClickListener{
        void click(View view);
    }
    public void setClickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }

    public void setMarginLeftOrRightText(int marginLeftOrRightText) {
        this.marginLeftOrRightText = marginLeftOrRightText;
    }

    public void setMarginTopText(int marginTopText) {
        this.marginTopText = marginTopText;
    }

    public void setImgMarginTopText(int imgMarginTop) {
        this.imgMarginTop = imgMarginTop;
    }

    public void setMarginBottomText(int marginBottomText) {
        this.marginBottomText = marginBottomText;
    }


    class MyLitterXView extends View implements Animation.AnimationListener{
        private Paint paint;
        private Point[] hLine;
        private Point[] vLine;
        protected int model = TO_NOMAL;
        private ValueAnimator valueAnimator;
        private RotateAnimation animation1;
        private RotateAnimation animation2;

        public MyLitterXView(Context context) {
            super(context);

        }

        public MyLitterXView(Context context, AttributeSet attrs) {
            super(context, attrs);

        }

        public MyLitterXView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void init(){
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(2);
            hLine = new Point[2];
            vLine = new Point[2];
            hLine[0] = new Point(0,imgRadius);
            hLine[1] = new Point(imgRadius*2,imgRadius);
            vLine[0] = new Point(imgRadius,0);
            vLine[1] = new Point(imgRadius,imgRadius*2);
            animation1 = new RotateAnimation(0,45,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation1.setDuration(500);
            animation1.setFillAfter(true);
            animation2 = new RotateAnimation(45,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation2.setDuration(500);
            animation2.setFillAfter(true);
            animation1.setAnimationListener(this);
            animation2.setAnimationListener(this);
        }

        public void change(int model){
            if (isAnimationing)return;
            switch (model){
                case TO_SPECIAL:
                    startAnimation(animation1);
                    this.model = TO_SPECIAL;
                    break;
                case TO_NOMAL:
                    startAnimation(animation2);
                    this.model = TO_NOMAL;
                    break;
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(0x00000000);
            canvas.drawLine(hLine[0].getX(),hLine[0].getY(),hLine[1].getX(),hLine[1].getY(),paint);
            canvas.drawLine(vLine[0].getX(),vLine[0].getY(),vLine[1].getX(),vLine[1].getY(),paint);
            canvas.restore();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(imgRadius*2,imgRadius*2);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            isAnimationing = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isAnimationing = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }
}
