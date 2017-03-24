package com.bendian.nursingbed.im;

import com.bendian.nursingbed.util.Constant;
import com.yuntongxun.ecsdk.CallStatisticsInfo;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.VoIPCallUserInfo;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

public class SDKHelper {

    public static CallStatisticsInfo getCallStatistics(String paramString) {
        return ECDevice.getECVoIPSetupManager().getCallStatistics(paramString, true);
    }

    public static boolean getLoudSpeakerStatus() {
        return ECDevice.getECVoIPSetupManager().getLoudSpeakerStatus();
    }

    public static boolean getMuteStatus() {
        return ECDevice.getECVoIPSetupManager().getMuteStatus();
    }

    public static void sendMsg(String msg, String userName, ECChatManager.OnSendMessageListener listener) {
        ECMessage localECMessage = ECMessage.createECMessage(ECMessage.Type.TXT);
        localECMessage.setTo(Constant.AppID+"#"+userName);
        localECMessage.setBody(new ECTextMessageBody(msg));
        ECDevice.getECChatManager().sendMessage(localECMessage,listener);
    }

    public static void setCallUserInfo(VoIPCallUserInfo paramVoIPCallUserInfo) {
        ECDevice.getECVoIPSetupManager().setVoIPCallUserInfo(paramVoIPCallUserInfo);
    }

    public static void setLoutSperkerStatus(boolean paramBoolean) {
        ECDevice.getECVoIPSetupManager().enableLoudSpeaker(paramBoolean);
    }

    public static void setMute(boolean paramBoolean) {
        ECDevice.getECVoIPSetupManager().setMute(paramBoolean);
    }

    public static void setVieoBitRates(int paramInt) {
        ECDevice.getECVoIPSetupManager().setVideoBitRates(paramInt);
    }
}