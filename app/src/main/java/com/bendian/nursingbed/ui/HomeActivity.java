package com.bendian.nursingbed.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bendian.nursingbed.R;
import com.bendian.nursingbed.adapter.MyFragmentAdapter;
import com.bendian.nursingbed.fragment.ControlFragment;
import com.bendian.nursingbed.fragment.HomeFragment;
import com.bendian.nursingbed.fragment.NursingFragment;
import com.bendian.nursingbed.util.ECC;
import com.bendian.nursingbed.util.SDKCoreHelper;
import com.bendian.nursingbed.util.ToastUtil;
import com.bendian.nursingbed.view.BottomMenuDialog;
import com.bendian.nursingbed.view.IconFontTextItem;
import com.bendian.nursingbed.view.ToolbarService;
import com.bendian.nursingbed.view.sliding.DragLayout;
import com.bendian.nursingbed.view.trip.Controller;
import com.bendian.nursingbed.view.trip.PagerBottomTabLayout;
import com.bendian.nursingbed.view.trip.listener.OnTabItemSelectListener;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.SdkErrorCode;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 主Activity
 * Created by Administrator on 2016/8/10.
 */
public class HomeActivity extends BaseActivity implements OnTabItemSelectListener, ToolbarService.OnItemClickListener, BottomMenuDialog.OnMenuItemClick, BottomMenuDialog.BeforeShowCallBack {

    private Controller controller;
    private ToolbarService toolbarService;
    private String[] tag = {"首页", "遥控", "", "消息", "资讯"};
    private ViewPager viewPager;
    public String cityName;
    private static HomeActivity homeActivity;
    private DragLayout dragLayout;
    private MyFragmentAdapter adapter;
    private BottomMenuDialog bottomMenuDialog;
    private PagerBottomTabLayout bottomTabLayout;
    private int Color_On;
    private int Color_Off;
    private int Color_Unable;
    private List<IconFontTextItem> list;
    private boolean isLocalViewOn = true;
    private boolean isFrontCamera = true;

    public static HomeActivity instanse() {
        return homeActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initParam();

        initView();

        addListener();

        setStatusBar(new View[]{findViewById(R.id.toolbar_include),findViewById(R.id.text)});
    }

    private void addListener() {
        ECVoIPCallManager callManager = ECDevice.getECVoIPCallManager();
        if (callManager != null) {
            callManager.setOnVoIPCallListener(SDKCoreHelper.getInstance());
        }
        ECDevice.setOnChatReceiveListener(SDKCoreHelper.getInstance());
    }

    private void initParam() {
        Intent intent = getIntent();
        cityName = intent.getStringExtra("City");
        if (cityName == null) {
            cityName = "南昌";
        }
        HomeActivity.homeActivity = this;

        Color_On = getResources().getColor(R.color.colorPrimary);
        Color_Off = getResources().getColor(R.color.secondaryText);
        Color_Unable = getResources().getColor(R.color.Gray);
    }

    private void initView() {
        initDragLayout();

        bottomTabLayout = (PagerBottomTabLayout) findViewById(R.id.tab);
        if (bottomTabLayout != null) {
            controller = bottomTabLayout.builder()
                    .addTabItem(R.drawable.home, tag[0])
                    .addTabItem(R.drawable.control, tag[1])
                    .addTabItem(R.drawable.menu, "")
                    .addTabItem(R.drawable.im, tag[3])
                    .addTabItem(R.drawable.zixun, tag[4])
                    .setMessageBackgroundColor(0xFF3020)
                    .build();
            controller.setBackground(getResources().getDrawable(R.drawable.menu_bk));
            controller.addTabItemClickListener(this);
        }

        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar_include);
        if (toolbar != null) {
            toolbarService = ToolbarService.instance(toolbar);
            toolbarService.setOnItemClickListener(this);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            HomeFragment homeFragment = new HomeFragment();
            ControlFragment controlFragment = new ControlFragment();
            NursingFragment nursingFragment = new NursingFragment();
            NursingFragment nursingFragment2 = new NursingFragment();
            NursingFragment nursingFragment3 = new NursingFragment();
            ArrayList<Fragment> fragmentArrayList = new ArrayList<>(5);
            fragmentArrayList.add(homeFragment);
            fragmentArrayList.add(controlFragment);
            fragmentArrayList.add(nursingFragment);
            fragmentArrayList.add(nursingFragment3);
            fragmentArrayList.add(nursingFragment2);
            adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentArrayList);
            viewPager.setAdapter(adapter);
        }
    }

    private void initDragLayout() {

        ViewGroup leftMenu = (ViewGroup) findViewById(R.id.leftMenu);
        if (leftMenu != null) {
            //设置侧滑菜单宽度为屏幕的80%
            leftMenu.setLayoutParams(new FrameLayout.LayoutParams((int) (getScreenWidth() * 0.8), ViewGroup.LayoutParams.MATCH_PARENT));
        }
        dragLayout = (DragLayout) findViewById(R.id.dragLayout);
        //设置侧滑菜单状态监听
        dragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {
                if (toolbarService != null) {
                    View navView = toolbarService.getChildAt(0);
                    navView.setAlpha(1 - percent);
                }
            }
        });

    }

    @Override
    public void onSelected(int index, Object tag) {
        onSelectedDispatcher(index, tag, isRepeat);
        if (index != 2) {
            currentSelected = index;
        }
    }

    int currentSelected;
    boolean isRepeat = false;

    private void onSelectedDispatcher(int index, Object tag, boolean isRepeat) {
        if (index == 2) {
            this.isRepeat = true;
            controller.setSelect(currentSelected);
            this.isRepeat = false;
            if (bottomMenuDialog == null) {
                bottomMenuDialog = new BottomMenuDialog(this, R.layout.dialog_bottom_menu, bottomTabLayout.getHeight(), null, this);
            }
            bottomMenuDialog.setOnMenuItemClick(this);
            bottomMenuDialog.show();
            return;
        }
        //Tab被选中的时候调用
        if (toolbarService != null) {
            toolbarService.setTitle(this.tag[index]);
            toolbarService.setItem1(R.string.empty);
            toolbarService.setItem2(R.string.empty);
            switch (index) {
                case 0:
                    break;
                case 1:
//                    toolbarService.setItem2(R.string.paramManager);
                    break;
            }
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(index, false);
        }
    }

    @Override
    public void onRepeatClick(int index, Object tag) {
        //Tab重复点击
    }

    public void onConnectionState(ECDevice.ECConnectState ecConnectState, ECError ecError) {
        if (ecConnectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (ecError.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                //账号异地登陆
                final MaterialDialog mMaterialDialog = new MaterialDialog(this);
                mMaterialDialog.setTitle("账号异常")
                        .setCanceledOnTouchOutside(false)
                        .setMessage("您的账号已在别处登录！")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, EntryActivity.class);
                                intent.putExtra("actionCode", 333);
                                startActivity(intent);
                                HomeActivity.this.finish();
                                mMaterialDialog.dismiss();
                            }
                        })
                        .show();
            } else {
                //重连中
            }
        } else if (ecConnectState == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            // 重连成功
        }
    }

    @Override
    public void finish() {
        super.finish();
        //主Activity退出的时候，退出登录并注销SDK,清理后台。。。
        ECDevice.logout(new ECDevice.OnLogoutListener() {
            @Override
            public void onLogout() {
                // SDK 回调通知当前登出成功
                // 这里可以做一些（与云通讯IM相关的）应用资源的释放工作
                // 如（关闭数据库，释放界面资源和跳转等）
            }
        });
        if (ECDevice.isInitialized())
            ECDevice.unInitial();
        homeActivity = null;
    }

    @Override
    public void onClick(ToolbarService.Item item) {
        switch (item) {
            case item1:
            case item2:
                /*if (viewPager.getCurrentItem() == 1) {
                    // 控制界面ToolBar设置按钮点击事件
                    ControlFragment controlFragment = ControlFragment.instanse();
                    if (controlFragment != null)
                        controlFragment.onMenuClick(item);
                }*/
                break;
            case nav:
                if (dragLayout.getStatus() == DragLayout.Status.CLOSE)
                    dragLayout.open(true);
                break;
        }
    }

    @Override
    public void onItemClick(int index, IconFontTextItem view) {
        switch (index) {
            case 0:
                refreshMenuItem(!ECC.voiceStatus(), index);
                ECC.setVoiceStatus(!ECC.voiceStatus());
                break;
            case 1:
                refreshMenuItem(!ECC.loudSpeakerStatus(), index);
                ECC.setloudSpeakerStatus(!ECC.loudSpeakerStatus());
                break;
            case 2:
                ECC.setLocalVideoEnable(!isLocalViewOn);
                isLocalViewOn = !isLocalViewOn;
                refreshMenuItem(isLocalViewOn, index);
                break;
            case 3:
                try {
                    ControlFragment.instanse().captureView_local.switchCamera();
                    isFrontCamera = !isFrontCamera;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    refreshMenuItem(isFrontCamera, index);
                }
                break;
            case 4:
//                break;
            case 5:
//                break;
            case 6:
                ToastUtil.show("待开发");
                break;
            case 7:
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                break;
        }
    }

    @Override
    public void beforeShow(@NonNull List<IconFontTextItem> list) {
        this.list = list;
        refreshMenuItem(ECC.voiceStatus(), 0);
        refreshMenuItem(ECC.loudSpeakerStatus(), 1);
        refreshMenuItem(isLocalViewOn, 2);
    }

    private void refreshMenuItem(boolean status, int index) {
        IconFontTextItem voiceItem = this.list.get(index);
        switch (index) {
            case 0:
            case 1:
            case 2:
                voiceItem.setAllColor(status ? Color_On : Color_Off);
                voiceItem.setTitle(status ? voiceItem.getTitle().replace("关", "开") :
                        voiceItem.getTitle().replace("开", "关"));
                break;
            case 3:
                voiceItem.setAllColor(status ? Color_Off : Color_On);
                voiceItem.setTitle(status ? voiceItem.getTitle().replace("后", "前") :
                        voiceItem.getTitle().replace("前", "后"));
                break;
            case 4:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
