package com.bendian.nursingbed.adapter;

import android.view.View;
import android.widget.Toast;

import com.bendian.nursingbed.util.ToastUtil;

import java.util.Calendar;

/**
 * 防止按钮重复点击
 * Created by Administrator on 2016/8/27.
 */
public abstract class SafeOnClickListener implements View.OnClickListener {
    public static final int DEFAULT = 500;
    //两次点击间隔最短时间
    public int spaceTime = DEFAULT;
    private long lastClickTime = 0;

    public void setSpaceTime(int spaceTime) {
        this.spaceTime = spaceTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > spaceTime) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }else {
            ToastUtil.show("操作频率太快了！");
        }
    }

    protected abstract void onNoDoubleClick(View v);


}
