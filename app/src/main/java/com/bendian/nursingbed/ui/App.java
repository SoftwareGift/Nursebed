package com.bendian.nursingbed.ui;

import android.app.Application;

import com.bendian.nursingbed.util.ToastUtil;

/**
 * Created by Administrator on 2016/8/27.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.setContext(this);
    }
}
