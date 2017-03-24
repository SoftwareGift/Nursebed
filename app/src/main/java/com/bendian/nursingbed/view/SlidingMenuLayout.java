package com.bendian.nursingbed.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 侧滑菜单
 * Created by Administrator on 2016/8/25.
 */
public class SlidingMenuLayout extends ViewGroup {

    //按下时的X坐标
    private float downX;
    //按下时的Y坐标
    private float downY;
    private Scroller scroller;
    private float moveX;
    //当前Menu显示状态（true显示 false不显示）
    private boolean isMenuShow = false;
    private VelocityTracker mTracker;

    public SlidingMenuLayout(Context context) {
        super(context);
        init();
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化滚动器, 数值模拟器
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View left_menu = getChildAt(0);
        left_menu.measure(left_menu.getLayoutParams().width, heightMeasureSpec);

        View main_content = getChildAt(1);
        main_content.measure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View left_menu = getChildAt(0);
        View main_content = getChildAt(1);
        if (left_menu == null || main_content == null)
            return;
        if (!isMenuShow) {
            left_menu.layout(-left_menu.getMeasuredWidth(), t, 0, b);
            main_content.layout(l, t, r, b);
        } else {
            left_menu.layout(l,t,left_menu.getMeasuredWidth(),b);
            main_content.layout(left_menu.getMeasuredWidth(),t,r+left_menu.getMeasuredWidth(),b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mTracker==null){
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                int scrollX = (int) (downX - moveX);
                int newScrollPosition = getScrollX() + scrollX;
                if (newScrollPosition < -getChildAt(0).getLayoutParams().width){
                    scrollTo(-getChildAt(0).getLayoutParams().width, 0);
                } else if (newScrollPosition > 0){
                    scrollTo(0, 0);
                } else {
                    scrollBy(scrollX, 0);
                }
                downX = moveX;

                break;
            case MotionEvent.ACTION_UP:
                float currentSpeed = 0;
                if (mTracker!=null) {
                    mTracker.computeCurrentVelocity(1000);
                    currentSpeed = mTracker.getXVelocity();
                }
                int leftCenter = (int) (- getChildAt(0).getMeasuredWidth() / 2.0f);
                if (currentSpeed>200){
                    isMenuShow = true;
                    updateCurrentContent();
                }else if (currentSpeed<-200){
                    isMenuShow = false;
                    updateCurrentContent();
                }else {
                    slidingByPosition(leftCenter);
                }
                if (mTracker!=null){
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
        }
        return true;
    }

    private void slidingByPosition(int leftCenter) {
        if(getScrollX() < leftCenter){
            isMenuShow = true;
            updateCurrentContent();
        }else {
            isMenuShow = false;
            updateCurrentContent();
        }
    }

    /**
     * 根据当前的状态, 执行 关闭/开启 的动画
     */
    private void updateCurrentContent() {
        int startX = getScrollX();
        int dx = 0;
        // 平滑滚动
        if(isMenuShow){
            dx = -getChildAt(0).getMeasuredWidth() - startX;
        } else {
            dx = 0 - startX;
        }
        int duration = Math.abs(dx); // 0 -> 1200
        scroller.startScroll(startX, 0, dx, 0, duration);
        invalidate();// 重绘界面 -> drawChild() -> computeScroll();
    }

    //2. 维持动画的继续
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){ // 直到duration事件以后, 结束
            int currX = scroller.getCurrX();
            scrollTo(currX, 0); // 滚过去
            invalidate(); // 重绘界面-> drawChild() -> computeScroll();循环
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = ev.getX() - downX;
                float offsetY = ev.getY() - downY;

                if (Math.abs(offsetX)>Math.abs(offsetY) && Math.abs(offsetX)>5){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
