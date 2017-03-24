package com.bendian.nursingbed.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bendian.nursingbed.R;

/**
 * 自定义View，包含图片和文字
 * Created by Administrator on 2016/8/27.
 */
public class ImageTextItem extends LinearLayout {

    private ImageView iconView;
    private TextView titleView;

    public ImageTextItem(Context context, String title, int iconRes, int width, int height) {
        this(context);
        this.setLayoutParams(new LayoutParams(width, height));
        try {
            setTitle(title);
            setIconRes(iconRes);
            setIconColor(R.color.colorAccent);
        } catch (Exception e) {
            e.printStackTrace();
            setIconColor(Color.BLACK);
        }
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, height * 2 / 9);
    }

    public ImageTextItem(Context context) {
        this(context, null);
    }

    public ImageTextItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.addView(new ImageView(context), 0);
        this.addView(new TextView(context), 1);
        iconView = (ImageView) this.getChildAt(0);
        titleView = (TextView) this.getChildAt(1);
        this.setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextItem, 0, 0);
            try {
                setTitle(a.getString(R.styleable.ImageTextItem_text));
                setIconRes(a.getResourceId(R.styleable.ImageTextItem_iconRes, 0));
                setTitleSize(a.getLayoutDimension(R.styleable.ImageTextItem_textSize, 18));
                setTitleColor(a.getColor(R.styleable.ImageTextItem_textColor, Color.BLACK));
                setIconColor(a.getColor(R.styleable.ImageTextItem_iconColor, 0));
            } finally {
                a.recycle();
            }
        }
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconView.setPadding(8, 8, 8, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout.LayoutParams titleParam = (LayoutParams) titleView.getLayoutParams();
        LinearLayout.LayoutParams iconParam = (LayoutParams) iconView.getLayoutParams();
        titleParam.height = getMeasuredHeight() / 3;
        titleParam.width = getMeasuredWidth();
        iconParam.height = getMeasuredHeight() / 2;
        iconParam.width = getMeasuredHeight() / 2;
        iconParam.topMargin = getMeasuredHeight() / 12;
        iconParam.bottomMargin = getMeasuredHeight() / 12;

        iconParam.gravity = Gravity.CENTER;

        iconView.setLayoutParams(iconParam);
        titleView.setLayoutParams(titleParam);

    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setIconRes(int iconRes) {
        iconView.setImageResource(iconRes);
    }

    public void setTitleSize(float titleSize) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }

    public void setTitleColor(int titleColor) {
        titleView.setTextColor(titleColor);
    }

    public void setIconColor(int iconColor) {
        iconView.setColorFilter(iconColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable background = getBackground();
                if (background != null) {
                    background.setAlpha(200);
                    setBackgroundDrawable(background);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Drawable background1 = getBackground();
                if (background1 != null) {
                    background1.setAlpha(255);
                    setBackgroundDrawable(background1);
                }
                break;
        }
        return true;
    }
}
