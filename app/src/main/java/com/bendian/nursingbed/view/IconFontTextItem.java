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

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义View，包含图片和文字
 * Created by Administrator on 2016/8/27.
 */
public class IconFontTextItem extends LinearLayout {

    private int backColor;
    private int pressBackColor;
    private IconTextView iconView;
    private TextView titleView;

    public IconFontTextItem(Context context) {
        this(context, null);
    }

    public IconFontTextItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconFontTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.addView(new IconTextView(context), 0);
        this.addView(new TextView(context), 1);
        iconView = (IconTextView) this.getChildAt(0);
        titleView = (TextView) this.getChildAt(1);
        this.setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconFontTextItem, 0, 0);
            try {
                setTitle(a.getString(R.styleable.IconFontTextItem_text));
                setIconRes(a.getResourceId(R.styleable.IconFontTextItem_iconRes, 0));
                setTitleSize(a.getDimension(R.styleable.IconFontTextItem_textSize, 18));
                setTitleColor(a.getColor(R.styleable.IconFontTextItem_textColor, Color.BLACK));
                setIconColor(a.getColor(R.styleable.IconFontTextItem_iconColor, 0));
                setIconSize(a.getDimension(R.styleable.IconFontTextItem_iconSize, 40));
                this.pressBackColor = a.getColor(R.styleable.IconFontTextItem_backColorPress,0);
                this.backColor = a.getColor(R.styleable.IconFontTextItem_backColor,0);
                setBackgroundColor(backColor);
            } finally {
                a.recycle();
            }
        }
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconView.setPadding(8, 8, 8, 0);
        this.setClickable(true);
        this.setLongClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams titleParam = (LayoutParams) titleView.getLayoutParams();
        LayoutParams iconParam = (LayoutParams) iconView.getLayoutParams();
        titleParam.height = getMeasuredHeight() * 5 / 12;
        titleParam.width = getMeasuredWidth();
        iconParam.height = getMeasuredHeight() / 2;
        iconParam.width = getMeasuredHeight() / 2;
        iconParam.topMargin = getMeasuredHeight() / 12;

        iconParam.gravity = Gravity.CENTER;

        iconView.setLayoutParams(iconParam);
        titleView.setLayoutParams(titleParam);

    }

    public IconFontTextItem setTitle(String title) {
        titleView.setText(title);
        return this;
    }

    public IconFontTextItem setIconRes(int iconRes) {
        iconView.setText(iconRes);
        return this;
    }

    public IconFontTextItem setTitleSize(float titleSize) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        return this;
    }

    public IconFontTextItem setTitleColor(int titleColor) {
        titleView.setTextColor(titleColor);
        return this;
    }

    public IconFontTextItem setIconColor(int iconColor) {
        iconView.setTextColor(iconColor);
        return this;
    }

    public IconFontTextItem setAllColor(int color){
        titleView.setTextColor(color);
        iconView.setTextColor(color);
        return this;
    }

    public String getTitle(){
        return titleView.getText().toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /*Drawable background = getBackground();
                if (background != null) {
                    background.setAlpha(180);
                    setBackgroundDrawable(background);
                }*/
                setBackgroundColor(pressBackColor);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setBackgroundColor(backColor);
                break;
        }
        return super.onTouchEvent(event);
    }

    public IconFontTextItem setIconSize(float iconSize) {
        iconView.setTextSize(TypedValue.COMPLEX_UNIT_PX,iconSize);
        return this;
    }
}
