package com.bendian.nursingbed.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bendian.nursingbed.R;
import com.bendian.nursingbed.domain.User;
import com.bendian.nursingbed.net.HttpDefaultFactory;
import com.bendian.nursingbed.net.URL;
import com.bendian.nursingbed.util.Constant;
import com.bendian.nursingbed.util.SDKCoreHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.SdkErrorCode;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * 程序入口Activity
 * Created by Administrator on 2016/8/8.
 */
public class EntryActivity extends BaseActivity implements View.OnClickListener, URL {

    private EditText et_userName;
    private EditText et_password;
    private TextView btn_login;
    private static EntryActivity entryActivity;
    public ImageView imageView;
    private RadioGroup radioGroup;

    public static EntryActivity instance() {
        return entryActivity;
    }

    /**
     * UI代码，可以忽略
     */
    public void setImageViewGone() {
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
                imageView.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animation);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        if (!this.isTaskRoot()) {
            finish();
            return;
        }

        entryActivity = this;

        initParam();

        initView();

        SDKCoreHelper.initAndLogin(this, null);

    }

    private void initParam() {
        try {
            Intent intent = getIntent();
            int actionCode = intent.getIntExtra(getString(R.string.actionCode), 0);
            if (actionCode == 333) {
                Toast.makeText(this, R.string.msg_logout, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (TextView) findViewById(R.id.btn_login);

        et_userName.setKeyListener(null);
        et_password.setKeyListener(null);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (btn_login != null) {
            btn_login.setOnClickListener(this);
        }
        imageView = (ImageView) findViewById(R.id.image);
    }

    /**
     * 点击EditText以外的区域隐藏键盘，可忽略
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            /**
             * 先通过账号密码登录到己方服务器
             */
            /*HttpDefaultFactory httpDefaultFactory = HttpDefaultFactory.getInstance();
            httpDefaultFactory.connectedToServer(this, new HashMap<String, String>() {
                {
                    put("userName", et_userName.getText().toString());
                    put("pwd", et_password.getText().toString());
                    put("actionCode","100");
                }
            }, base+"/user.action", new TypeToken<String>() {
            }.getType(), new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        String userStr = response.toString();
                        Gson gson = new Gson();
                        User user = gson.fromJson(userStr, User.class);
                        if (user!=null){
                            *//**成功登录己方服务器后再执行容联服务器的登录*//*
                            login(user.get_userName(),user.get_pwd());
                        }
                    }catch (ClassCastException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });*/
            login(et_userName.getText().toString());
        }
    }

    /**
     * 登录到容联服务器
     */
    private void login(String userName) {
        ECDevice.setOnDeviceConnectListener(SDKCoreHelper.getInstance());
        ECInitParams p = ECInitParams.createParams();
        //自定义登录方式：
        p.setUserid(userName);
        p.setAppKey(getString(R.string.appid));
        p.setToken(getString(R.string.apptocken));
        // 设置登陆验证模式（是否验证密码）NORMAL_AUTH-自定义方式
        p.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        p.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        SDKCoreHelper.initAndLogin(this, p);
    }

    /**
     * 登录结果‘回调’方法
     */
    public void forward(ECDevice.ECConnectState ecConnectState, ECError ecError) {
        if (ecConnectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (ecError.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                //账号异地登陆
            } else {
                //连接状态失败
            }
        } else if (ecConnectState == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            // 登陆成功
            Intent intent = new Intent(this, HomeActivity.class);
            RadioButton button = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            if (button != null) {
                intent.putExtra(getString(R.string.city), button.getText().toString().equals(getString(R.string.nanChang)) ? getString(R.string.nanchang) : getString(R.string.nanjing));
            }
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        entryActivity = null;
    }
}
