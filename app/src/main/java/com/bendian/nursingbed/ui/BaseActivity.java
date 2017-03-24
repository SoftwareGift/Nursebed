package com.bendian.nursingbed.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bendian.nursingbed.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/8/8.
 */
public class BaseActivity extends AppCompatActivity {
    private int screenHeight;
    private int screenWidth;

    public int getScreenHeight(){
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar(View[] views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            final ViewGroup linear_bar = (ViewGroup) findViewById(R.id.toolbar_include);
            final int statusHeight = getStatusBarHeight();
            for (final View view:views){
                if (view != null) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            int titleHeight = view.getHeight();
                            ViewGroup.LayoutParams params = view.getLayoutParams();
                            params.height = statusHeight + titleHeight;
                            view.setLayoutParams(params);
                        }
                    });
                }
            }

        }
    }
    /**
     * 获取状态栏的高度
     */
    protected int getStatusBarHeight(){
        try
        {
            Class<?> c=Class.forName("com.android.internal.R$dimen");
            Object obj=c.newInstance();
            Field field=c.getField("status_bar_height");
            int x=Integer.parseInt(field.get(obj).toString());
            return  getResources().getDimensionPixelSize(x);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
