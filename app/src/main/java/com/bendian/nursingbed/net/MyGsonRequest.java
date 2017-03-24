package com.bendian.nursingbed.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/30.
 */
public class MyGsonRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;
    private Map<String, String> stringMap;
    private Gson mGson;
    private Type mType;

    public MyGsonRequest(int method, String url, Type mType, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mGson = new Gson();
        this.mType = mType;
        this.mListener = listener;
    }

    public MyGsonRequest(String url, Type mType, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(0, url, mType, listener, errorListener);
    }

    public MyGsonRequest(String url, Map<String, String> map, Type mType, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, mType, listener, errorListener);
        this.stringMap = map;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return this.stringMap == null?super.getParams():this.stringMap;
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String e = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (mType==new TypeToken<String>(){}.getType()){
                return (Response<T>) Response.success(e, HttpHeaderParser.parseCacheHeaders(response));
            }
            return (Response<T>) Response.success(this.mGson.fromJson(e, this.mType), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        }
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }
}
