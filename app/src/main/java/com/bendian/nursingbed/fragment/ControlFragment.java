package com.bendian.nursingbed.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bendian.nursingbed.R;
import com.bendian.nursingbed.adapter.SafeOnClickListener;
import com.bendian.nursingbed.im.MsgFactory;
import com.bendian.nursingbed.im.MsgType;
import com.bendian.nursingbed.im.SDKHelper;
import com.bendian.nursingbed.ui.HomeActivity;
import com.bendian.nursingbed.util.SDKCoreHelper;
import com.bendian.nursingbed.view.ToolbarService;
import com.kyleduo.switchbutton.SwitchButton;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.voip.video.ECCaptureView;


/**
 * 遥控器界面
 * Created by Administrator on 2016/8/11.
 */
public class ControlFragment extends Fragment implements ECChatManager.OnSendMessageListener {

    public ECCaptureView captureView_target;
    public ECCaptureView captureView_local;
    private String targetName;
    //当前视频通话的唯一标识
    public String currentCallId;
    private SwitchButton button;

    private static ControlFragment fragment;
    private RelativeLayout viewContainer;
    private OnClickListener onClickListener;
    private View view;

    public static ControlFragment instanse() {
        return fragment;
    }

    public ControlFragment() {
        super();
        fragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        initView(view);
        targetName = HomeActivity.instanse().cityName;
        return view;
    }

    private void initView(View view) {
        LinearLayout ll_up = (LinearLayout) view.findViewById(R.id.ll_up);
        LinearLayout ll_down = (LinearLayout) view.findViewById(R.id.ll_down);
        LinearLayout ll_right = (LinearLayout) view.findViewById(R.id.ll_right);
        LinearLayout ll_left = (LinearLayout) view.findViewById(R.id.ll_left);
        LinearLayout ll_stop = (LinearLayout) view.findViewById(R.id.ll_stop);
        LinearLayout ll_switch = (LinearLayout) view.findViewById(R.id.ll_switch);
        LinearLayout ll_reset = (LinearLayout) view.findViewById(R.id.ll_reset);
        LinearLayout ll_open = (LinearLayout) view.findViewById(R.id.ll_open);
        LinearLayout ll_close = (LinearLayout) view.findViewById(R.id.ll_close);
        LinearLayout ll_auto = (LinearLayout) view.findViewById(R.id.ll_auto);
        LinearLayout ll_qu = (LinearLayout) view.findViewById(R.id.ll_qu);
        LinearLayout ll_ping = (LinearLayout) view.findViewById(R.id.ll_ping);

        viewContainer = (RelativeLayout) view.findViewById(R.id.views);

        ViewGroup.LayoutParams params = viewContainer.getLayoutParams();
        params.height = ((HomeActivity) getActivity()).getScreenWidth() * 48 / 64;

        onClickListener = new OnClickListener();

        ll_up.setOnClickListener(onClickListener);
        ll_down.setOnClickListener(onClickListener);
        ll_right.setOnClickListener(onClickListener);
        ll_left.setOnClickListener(onClickListener);
        ll_stop.setOnClickListener(onClickListener);
        ll_reset.setOnClickListener(onClickListener);
        ll_open.setOnClickListener(onClickListener);
        ll_close.setOnClickListener(onClickListener);
        ll_auto.setOnClickListener(onClickListener);
        ll_qu.setOnClickListener(onClickListener);
        ll_ping.setOnClickListener(onClickListener);
        ll_switch.setOnClickListener(new SafeOnClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (button == null) {
                    button = (SwitchButton) v.findViewById(R.id.switchBtn);
                }
                if (button != null) {
                    button.setChecked(!button.isChecked());
                    mackCall();
                }
            }
        });
    }

    /**
     * 进行视频呼叫
     */
    private void mackCall() {
        if (!SDKCoreHelper.getInstance().isOnVoIPCall()) {
            setSurfaceViewVisable(true);
            //设置视频显示View
            SDKCoreHelper.getInstance().setVideoView(captureView_target, captureView_local, ControlFragment.class);
            //设置当前呼叫的界面类型
            SDKCoreHelper.getInstance().setCallPage(SDKCoreHelper.CallPage.ControlFragment);
            //尝试呼叫
            currentCallId = ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VIDEO, HomeActivity.instanse().cityName);
            if (currentCallId == null || currentCallId.equals("")) {
                //说明：mCurrentCallId如果返回空则代表呼叫失败，可能是参数错误引起。否则返回是一串数字，是当前通话的标识。
            }
        } else {
            //尝试挂断电话
            ECDevice.getECVoIPCallManager().releaseCall(ECDevice.getECVoIPSetupManager().getCurrentCall());
        }
    }

    public void setSurfaceViewVisable(boolean visable) {
        try {
            if (visable) {
                if (captureView_local == null) {
                    captureView_local = new ECCaptureView(getContext());
                    RelativeLayout.LayoutParams paramsLocal = new RelativeLayout.LayoutParams(1, 1);
                    captureView_local.setLayoutParams(paramsLocal);
                    captureView_target = new ECCaptureView(getContext());
                    RelativeLayout.LayoutParams paramsTarget = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    captureView_target.setLayoutParams(paramsTarget);
                }
                viewContainer.addView(captureView_target);
                viewContainer.addView(captureView_local);
            } else {
                viewContainer.removeView(captureView_local);
                viewContainer.removeView(captureView_target);
            }
        } catch (Exception e) {
            e.printStackTrace();
            viewContainer.removeAllViews();
            checkSwitchBtn(false);
        }
    }

    /**
     * 为Fragment之外的类提供该表switchButton状态的方法
     */
    public void checkSwitchBtn(final boolean checkOrNot) {
        button.post(new Runnable() {
            @Override
            public void run() {
                button.setChecked(checkOrNot);
                button.invalidate();
            }
        });
    }

    @Override
    public void onSendMessageComplete(ECError ecError, ECMessage ecMessage) {
        //完成消息发送
        System.out.println("发送回调"+ecError.errorCode+ecError.errorMsg);
    }

    @Override
    public void onProgress(String s, int i, int i1) {
        //文件上传进度
    }


    public void onMenuClick(ToolbarService.Item item) {
        switch (item) {
            case item1:
                break;
            case item2:
                break;
        }
    }

    class OnClickListener extends SafeOnClickListener {

        @Override
        protected void onNoDoubleClick(View v) {
            if (!SDKCoreHelper.getInstance().isOnVoIPCall()){
                return;
            }
            System.out.println("控制");
            switch (v.getId()) {
                case R.id.ll_up:
                    SDKHelper.sendMsg(MsgFactory.create("up", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_down:
                    SDKHelper.sendMsg(MsgFactory.create("down", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_right:
                    SDKHelper.sendMsg(MsgFactory.create("right", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_left:
                    SDKHelper.sendMsg(MsgFactory.create("left", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_stop:
                    SDKHelper.sendMsg(MsgFactory.create("stop", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_reset:
                    SDKHelper.sendMsg(MsgFactory.create("reset", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_open:
                    SDKHelper.sendMsg(MsgFactory.create("open", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_close:
                    SDKHelper.sendMsg(MsgFactory.create("close", MsgType.Control), targetName, ControlFragment.this);
//                    Toast.makeText(getContext(),"抱歉，该功能暂未启用",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_auto:
                    Toast.makeText(getContext(),"抱歉，该功能暂未启用",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_qu:
                    SDKHelper.sendMsg(MsgFactory.create("qu", MsgType.Control), targetName, ControlFragment.this);
                    break;
                case R.id.ll_ping:
                    SDKHelper.sendMsg(MsgFactory.create("ping", MsgType.Control), targetName, ControlFragment.this);
                    break;
            }
        }
    }
}
