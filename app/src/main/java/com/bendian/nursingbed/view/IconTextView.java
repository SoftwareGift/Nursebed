package com.bendian.nursingbed.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bendian.nursingbed.R;

/**
 * 加载图片的TextView
 * Created by Administrator on 2016/8/10.
 */
public class IconTextView extends TextView {
    private int pressBackgroundColor;
    private int backgroundColor;
    private Context mContext;

    public IconTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextView, 0, 0);
            try {
                this.backgroundColor = typedArray.getColor(R.styleable.IconTextView_backColor, 0);
                this.pressBackgroundColor = typedArray.getColor(R.styleable.IconTextView_backColorPress, 0);
                setBackgroundColor(backgroundColor);
            } finally {
                typedArray.recycle();
            }
        }
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
        this.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pressBackgroundColor != 0) {
                    setBackgroundColor(pressBackgroundColor);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (backgroundColor != 0) {
                    setBackgroundColor(backgroundColor);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
