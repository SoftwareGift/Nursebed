package com.bendian.nursingbed.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.bendian.nursingbed.R;
import com.bendian.nursingbed.util.SDKCoreHelper;
import com.bendian.nursingbed.view.MyECCaptureView;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;

/**
 * 视频通话界面
 * Created by Administrator on 2016/9/5.
 */
public class VideoActivity extends BaseActivity implements View.OnClickListener, MyECCaptureView.ViewClickListener {

    private MyECCaptureView localView;
    private RelativeLayout container;
    private MyECCaptureView targetView;
    private RelativeLayout.LayoutParams fullScreenParam_target;
    private RelativeLayout.LayoutParams smallParam_local;
    private RelativeLayout.LayoutParams smallParam_target;
    private RelativeLayout.LayoutParams fullScreenParam_local;
    private int targetH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        initView();
        initCall();
    }

    private void initCall() {
        SDKCoreHelper.getInstance().setVideoView(targetView, localView, VideoActivity.class);
        ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VIDEO, "Nan");
    }

    private void initView() {
        //根布局
        container = (RelativeLayout) findViewById(R.id.container);
        //初始化视频显示SurfaceView
        initVideoViews();
    }

    private void initVideoViews() {
        localView = new MyECCaptureView(this);
        targetView = new MyECCaptureView(this);

        targetH = (getScreenWidth() - getStatusBarHeight()) * 48 / 64;

        fullScreenParam_target = new RelativeLayout.LayoutParams(getScreenWidth(), (getScreenWidth() - getStatusBarHeight()) * 48 / 64);
        fullScreenParam_target.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        fullScreenParam_local = new RelativeLayout.LayoutParams((getScreenHeight()-getStatusBarHeight()-targetH)*32/48, getScreenHeight()-getStatusBarHeight()-targetH);

        smallParam_target = new RelativeLayout.LayoutParams(getScreenWidth(), targetH);
        smallParam_target.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        smallParam_local = new RelativeLayout.LayoutParams(getScreenWidth() / 3, getScreenWidth() / 3 * 48 / 32);

        localView.setLayoutParams(smallParam_local);
        targetView.setLayoutParams(fullScreenParam_target);

        container.addView(localView);
        container.addView(targetView);

        localView.setClickable(true);
        localView.setListener(this);
        targetView.setMoveAble(false);
        targetView.setListener(this);
        targetView.setClickable(false);
        localView.setClickable(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void finish() {
        super.finish();
        ECDevice.getECVoIPCallManager().releaseCall(ECDevice.getECVoIPSetupManager().getCurrentCall());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void click(View view) {
        if (localView.getLayoutParams() == smallParam_local) {
            localView.setLayoutParams(fullScreenParam_local);
            targetView.setLayoutParams(smallParam_target);
            localView.setMoveAble(false);
        } else {
            localView.setLayoutParams(smallParam_local);
            targetView.setLayoutParams(fullScreenParam_target);
            localView.setMoveAble(true);
        }
    }
}
