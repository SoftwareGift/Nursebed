package com.bendian.nursingbed.util;

import android.content.Context;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import com.bendian.nursingbed.fragment.ControlFragment;
import com.bendian.nursingbed.ui.EntryActivity;
import com.bendian.nursingbed.ui.HomeActivity;
import com.bendian.nursingbed.ui.VideoActivity;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 容联云SDK帮助类，该类主要提供SDK初始化、登陆的功能，以及提供了各类消息回调接口的实现，提供消息的收取
 * 和分发功能。
 * Created by Administrator on 2016/8/10.
 */
public class SDKCoreHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener, ECVoIPCallManager.OnVoIPListener, OnChatReceiveListener {
    private static SDKCoreHelper helper;
    private static ECInitParams params;
    //
    private boolean isOnVoIPCall;
    private CallPage callPage = CallPage.ControlFragment;
    public static SDKCoreHelper getInstance(){
        if (helper==null){
            helper = new SDKCoreHelper();
        }
        return helper;
    }

    public CallPage getCallPage() {
        return callPage;
    }

    public void setCallPage(CallPage callPage) {
        this.callPage = callPage;
    }

    public boolean isOnVoIPCall() {
        return isOnVoIPCall;
    }

    /**
     * 初始化并登陆
     * @param context 上下文
     * @param params 登陆参数
     */
    public static void initAndLogin(@NotNull Context context, ECInitParams params){
        if (params!=null) {
            SDKCoreHelper.params = params;
            if (params.validate()){
                ECDevice.login(params);
            }
        }else {
            if (ECDevice.isInitialized()){
                try {
                    EntryActivity.instance().setImageViewGone();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else {
                ECDevice.initial(context, getInstance());
            }
        }
    }

    /**
     * 初始化成功时回调
     */
    @Override
    public void onInitialized() {
        try {
            EntryActivity.instance().setImageViewGone();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * SDK初始化失败
     * @param e 错误信息
     */
    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onConnect() {
        //不作处理
    }
    @Override
    public void onDisconnect(ECError ecError) {
        //不作处理
    }

    /**
     * 账号连接状态回调
     * @param ecConnectState 连接状态
     * @param ecError 错误信息
     */
    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {
        if (EntryActivity.instance()!=null){
            //登录成功则执行跳转
            EntryActivity.instance().forward(ecConnectState,ecError);
            System.out.println("登录成功");
        }else if (HomeActivity.instanse()!=null){
            HomeActivity.instanse().onConnectionState(ecConnectState,ecError);
        }
    }

    /**
     *收到DTMF按键消息
     * @param callId 当前通话的callId
     * @param dtmf 接收到的按键
     */
    @Override
    public void onDtmfReceived(String callId, char dtmf) {

    }

    public void setVideoView(SurfaceView targetView, SurfaceView localView, Class container){
        ECDevice.getECVoIPSetupManager().setVideoView(targetView, localView);
        if (container==ControlFragment.class){
            callPage = CallPage.ControlFragment;
        }else if (container== VideoActivity.class){
            callPage = CallPage.VideoActivity;
        }
    }
    public void setVideoView(SurfaceView targetView, TextureView localView, Class container){
        ECDevice.getECVoIPSetupManager().setNewVideoView(targetView, localView);
        if (container==ControlFragment.class){
            callPage = CallPage.ControlFragment;
        }else if (container== VideoActivity.class){
            callPage = CallPage.VideoActivity;
        }
    }

    /**
     * 呼叫事件回调
     * @param voIPCall 发起VoIP呼叫，SDK会通过该 回调接口通知呼叫过程中的各种呼叫状态
     */
    @Override
    public void onCallEvents(ECVoIPCallManager.VoIPCall voIPCall) {
        if(voIPCall == null) {
            return ;
        }
        switch (callPage){
            case ControlFragment://呼叫界面在ControlFragment时调用
                onCallEventsInControlFragment(voIPCall);
                break;
            case VideoActivity://呼叫界面在单独的Activity时调用
                break;
        }


    }

    private void onCallEventsInControlFragment(ECVoIPCallManager.VoIPCall voIPCall) {
        // 根据不同的事件通知类型来处理不同的业务
        ECVoIPCallManager.ECCallState callState = voIPCall.callState;
        switch (callState) {
            case ECCALL_PROCEEDING:
                // 正在连接服务器处理呼叫请求
                break;
            case ECCALL_ALERTING:
                // 呼叫到达对方客户端，对方正在振铃
                isOnVoIPCall = true;
                break;
            case ECCALL_ANSWERED:
                // 对方接听本次呼叫
                ControlFragment.instanse().checkSwitchBtn(true);
                break;
            case ECCALL_FAILED:
                isOnVoIPCall = false;
                System.out.println("呼叫失败");
                System.out.println(voIPCall.reason+"");
                ControlFragment.instanse().checkSwitchBtn(isOnVoIPCall);
                ControlFragment.instanse().setSurfaceViewVisable(isOnVoIPCall);
                // 本次呼叫失败，根据失败原因播放提示音
                break;
            case ECCALL_RELEASED:
                // 通话释放[完成一次呼叫]
                isOnVoIPCall = false;
                ControlFragment.instanse().setSurfaceViewVisable(false);
                ControlFragment.instanse().checkSwitchBtn(isOnVoIPCall);
                in_:switch (voIPCall.reason){
                    case 170486://本地线路被占用
                        try {
                            ECDevice.getECVoIPCallManager().releaseCall(ECDevice.getECVoIPSetupManager().getCurrentCall());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break in_;
                    default:
                        break in_;
                }
                break;
            default:
                isOnVoIPCall = false;
                break;
        }
    }

    /**
     * 收到对方请求切换音视频的消息
     * @param callId  当前通话的唯一标识
     * @param callType ECVoIPCallManager.CallType.VOICE代表请求增加视频
     */
    @Override
    public void onSwitchCallMediaTypeRequest(String callId , ECVoIPCallManager.CallType callType) {

    }

    /**
     * 收到对方回复切换音视频请求的应答
     * 即对方是否同意了切换音视频请求，该接口主要的功能是通知调用切换音视频请求后的结果， 本地需要根据当前的响应类型来对UI界面做处理，
     有如下两种情况需要做处理：
     如果响应类型为ECVoIPCallManager.CallType.VIDEO,则代表对方同意切换到视频通话，此时可以将界面切换到视频通话界面。
     如果响应类型为ECVoIPCallManager.CallType.VOICE,则当前的通话界面应该保持在音频通话界面，如果有视频通话需要关闭视频
     * @param callId 当前通话的唯一标识
     * @param callType 更新后的媒体状态：CallType#VIDEO}表示视频
     */
    @Override
    public void onSwitchCallMediaTypeResponse(String callId, ECVoIPCallManager.CallType callType) {

    }

    /**
     * 视频分辨率改变，呼叫过程中，对方视频的分辨率发生改变，通过该接口通知对方
     * @param videoRatio 视频分辨率改变信息
     */
    @Override
    public void onVideoRatioChanged(VideoRatio videoRatio) {

    }

    /**
     * 收到新消息
     * @param ecMessage 新消息
     */
    @Override
    public void OnReceivedMessage(ECMessage ecMessage) {

    }

    /**
     * 收到新的消息命令通知
     * @param ecMessageNotify 消息命令
     */
    @Override
    public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {

    }

    /**
     * 收到群组通知消息（有人加入、退出...）
     * 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
     * @param ecGroupNoticeMessage 群组通知消息
     */
    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {

    }

    /**
     * 当前账户离线消息数量
     * @param i 离线消息总数
     */
    @Override
    public void onOfflineMessageCount(int i) {

    }

    /**
     * 当前账户需要获取离线消息的数量
     * @return 消息数 -1:全部获取 0:不获取
     */
    @Override
    public int onGetOfflineMessage() {
        return 0;
    }

    /**
     * 接收到离线消息回调
     * @param list 离线消息
     */
    @Override
    public void onReceiveOfflineMessage(List<ECMessage> list) {

    }

    @Override
    public void onReceiveOfflineMessageCompletion() {
        //离线消息拉取完成
    }

    @Override
    public void onServicePersonVersion(int i) {
        //个人信息版本号
    }

    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {
        //客服消息
    }

    @Override
    public void onSoftVersion(String s, int i) {
        //已过时，不作处理
    }

    public enum CallPage{
        ControlFragment,VideoActivity,NONE
    }

}
