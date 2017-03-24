package com.bendian.nursingbed.util;

import android.hardware.Camera;
import android.view.SurfaceView;

import com.yuntongxun.ecsdk.ECDevice;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ECC {
    /**
     * 获取扬声器状态
     */
    public static boolean loudSpeakerStatus() {
        return ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus();
    }

    /**
     * 设置扬声器状态
     */
    public static void setloudSpeakerStatus(boolean isOn) {
        ECDevice.getECVoIPSetupManager().enableLoudSpeaker(isOn);
    }

    /**
     * 获取麦克风状态
     */
    public static boolean voiceStatus() {
        return !ECDevice.getECVoIPSetupManager().getMuteStatus();
    }

    /**
     * 设置麦克风状态
     */
    public static void setVoiceStatus(boolean isOn) {
        ECDevice.getECVoIPSetupManager().setMute(!isOn);
    }

    /**
     * 设置显示双方视频的控件
     */
    public static void setVideoView(SurfaceView targetView, SurfaceView localView) {
        ECDevice.getECVoIPSetupManager().setVideoView(targetView, localView);
    }

    /**
     * 设置是否显示预览本地视频图像，若停止，则对方也将收不到本地视频
     * @param enable true为显示预览 false为停止预览
     */
    public static void setLocalVideoEnable(boolean enable){
        ECDevice.getECVoIPSetupManager().setNeedCapture(enable);
    }

}
