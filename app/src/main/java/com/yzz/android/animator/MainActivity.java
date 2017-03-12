package com.yzz.android.animator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyMenuView myMenuView;
    private MyWindow myWindow;
    private RelativeLayout contentView;
    private MyAnimatorView myAnimatorView;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myMenuView = (MyMenuView) findViewById(R.id.menu_view);
        final List<String> list = new ArrayList<>();
        list.add("发布");
        list.add("报价");
        contentView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.animator_view, null);
        myAnimatorView = (com.yzz.android.animator.MyAnimatorView) contentView.getChildAt(0);
        myAnimatorView.init(Color.rgb(12, 22, 44), Color.WHITE, list);
        myWindow = new MyWindow(this);
        myAnimatorView.setWindow(myWindow);
        myAnimatorView.setRadius(50);
        int count = myAnimatorView.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = myAnimatorView.getChildAt(i);
            final int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,list.get(finalI),Toast.LENGTH_LONG).show();
                }
            });
        }
        myMenuView.setClickListener(new MyMenuView.ClickListener() {
            @Override
            public void click(View view) {
                if (isFirst) {
                    isFirst = false;
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) myAnimatorView.getLayoutParams();
                    lp.height = myMenuView.getMeasuredHeight() + myMenuView.getWidth() / 2 + MyAnimatorView.MARGIN;
                    myAnimatorView.setLayoutParams(lp);
                    myAnimatorView.setRadius(myMenuView.getWidth() / 2);
                    myAnimatorView.setHeight(myMenuView.getHeight());
                    myWindow.addView(contentView);
                } else {
                    myWindow.addView(contentView);
                    //myAnimatorView.doOpen();这里不可以开始动画，还没有加载完毕
                }
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMenuView.change(MyMenuView.TO_NOMAL);
                myAnimatorView.docloes();
            }
        });

    }


}
