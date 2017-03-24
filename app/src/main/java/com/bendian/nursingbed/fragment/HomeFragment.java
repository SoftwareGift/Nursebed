package com.bendian.nursingbed.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bendian.nursingbed.R;
import com.bendian.nursingbed.ui.VideoActivity;
import com.bendian.nursingbed.ui.VoiceActivity;
import com.bendian.nursingbed.view.IconFontTextItem;
import com.bendian.nursingbed.view.flyco.dialog.listener.OnBtnClickL;
import com.bendian.nursingbed.view.flyco.dialog.widget.MaterialDialog;

/**
 * Created by Administrator on 2016/8/11.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private LinearLayout home_video;
    private LinearLayout home_voice;
    private LinearLayout home_params;
    private LinearLayout home_user;
    private LinearLayout home_capture;
    private LinearLayout home_about;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        home_video = (IconFontTextItem) view.findViewById(R.id.home_video);
        home_voice = (IconFontTextItem) view.findViewById(R.id.home_voice);
        home_params = (IconFontTextItem) view.findViewById(R.id.home_params);
        home_user = (IconFontTextItem) view.findViewById(R.id.home_user);
        home_capture = (IconFontTextItem) view.findViewById(R.id.home_capture);
        home_about = (IconFontTextItem) view.findViewById(R.id.home_about);

        home_video.setOnClickListener(this);
        home_voice.setOnClickListener(this);
        home_params.setOnClickListener(this);
        home_user.setOnClickListener(this);
        home_capture.setOnClickListener(this);
        home_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_video:
                final MaterialDialog dialog = new MaterialDialog(getContext())
                        .title("温馨提示")
                        .content("您确定要立即进入视频通话界面么？")
                        .btnNum(2);
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        startActivity(new Intent(getContext(), VideoActivity.class));
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.home_voice:
                startActivity(new Intent(getContext(), VoiceActivity.class));
                break;
            case R.id.home_params:
                break;
            case R.id.home_user:
                break;
            case R.id.home_capture:
                break;
            case R.id.home_about:
                break;
        }
    }


}
