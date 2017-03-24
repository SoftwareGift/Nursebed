package com.bendian.nursingbed.util;

import android.content.Context;
import android.widget.Toast;

/**
 *
 * Created by Administrator on 2016/8/27.
 */
public class ToastUtil {
    private static Toast toast;
    private static Context context;

    public static void setContext(Context context) {
        ToastUtil.context = context;
    }

    public static void show(String content){
        if (context!=null) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }
}
