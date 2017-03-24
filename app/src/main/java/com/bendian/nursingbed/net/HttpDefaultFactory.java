package com.bendian.nursingbed.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/3/29.
 * 设置为单例
 */
public class HttpDefaultFactory {

    private HttpDefaultFactory() {}

    private static  HttpDefaultFactory defaultFactory;

    public static HttpDefaultFactory getInstance(){
        if (defaultFactory==null){
            defaultFactory = new HttpDefaultFactory();
        }
        return defaultFactory;
    }

    /**
     *与服务器建立连接，发送或获取数据
     */
    public void connectedToServer(Context context, Map<String, String> queryMap, String url, Type type,
                                  Response.Listener listener, Response.ErrorListener errorListener){
        if (!checkConnect(context)){
            Toast.makeText(context,"连接失败，请检查网络连接是否打开",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue mQueue = Volley.newRequestQueue(context,new OkHttpStack(new OkHttpClient()));
        MyGsonRequest myGsonRequest = new MyGsonRequest(url,queryMap, type,listener, errorListener);
        mQueue.add(myGsonRequest);
    }

    private boolean checkConnect(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
