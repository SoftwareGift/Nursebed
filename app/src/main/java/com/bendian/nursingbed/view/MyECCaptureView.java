package com.bendian.nursingbed.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.yuntongxun.ecsdk.voip.video.ECCaptureView;

/**
 * 自定义SurfaceView继承自ECCaptureView
 * 可拖拽、缩放
 * Created by Administrator on 2016/9/5.
 */
public class MyECCaptureView extends ECCaptureView implements View.OnClickListener {

    private int lastX;
    private int lastY;
    private ViewParent parent;
    private int parentH;
    private int parentW;
    private int downX;
    private int downY;
    private boolean clickCancel = false;
    private ViewClickListener listener;
    private boolean moveAble = true;

    public void setMoveAble(boolean moveAble) {
        this.moveAble = moveAble;
    }

    public MyECCaptureView(Context context) {
        this(context, null);
    }

    public MyECCaptureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyECCaptureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                lastY = (int) event.getRawY();
                downX = lastX;
                downY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int l = this.getLeft() + dx;
                int b = this.getBottom() + dy;
                int r = this.getRight() + dx;
                int t = this.getTop() + dy;
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + this.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + this.getHeight();
                }
                if (r > ((ViewGroup) getParent()).getWidth()) {
                    r = ((ViewGroup) getParent()).getWidth();
                    l = r - getWidth();
                }
                if (b > ((ViewGroup) getParent()).getHeight()) {
                    b = ((ViewGroup) getParent()).getHeight();
                    t = b - getHeight();
                }
                if (moveAble) {
                    this.layout(l, t, r, b);
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                if (Math.abs(lastX - downX) > 5 || Math.abs(lastY - downY) > 5) {
                    clickCancel = true;
                }
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (parent == null) {
                    parent = getParent();
                    initParentParams((ViewGroup) parent);
                }
                int x = getLeft() + getWidth() / 2;
                int y = getTop() + getHeight() / 2;
                if (x <= parentW / 2 && y <= parentH / 2) {
                    layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    //第一象限
                } else if (x > parentW / 2 && y < parentH / 2) {
                    layout(parentW - getMeasuredWidth(), 0, parentW, getMeasuredHeight());
                    //第二象限
                } else if (x < parentW / 2 && y > parentH / 2) {
                    layout(0, parentH - getMeasuredHeight(), getMeasuredWidth(), parentH);
                    //第三象限
                } else if (x >= parentW / 2 && y >= parentH / 2) {
                    layout(parentW - getMeasuredWidth(), parentH - getMeasuredHeight(), parentW, parentH);
                    //第四象限
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void initParentParams(ViewGroup viewGroup) {
        parentH = viewGroup.getHeight();
        parentW = viewGroup.getWidth();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (listener!=null&&!clickCancel){
            listener.click(v);
        }
        clickCancel = false;
    }

    public void setListener(ViewClickListener listener) {
        this.listener = listener;
    }

    public interface ViewClickListener{
        void click(View view);
    }

}
