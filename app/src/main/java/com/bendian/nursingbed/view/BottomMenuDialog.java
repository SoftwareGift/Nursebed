package com.bendian.nursingbed.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bendian.nursingbed.R;
import com.bendian.nursingbed.ui.HomeActivity;
import com.bendian.nursingbed.view.flyco.dialog.widget.base.BottomBaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义底部弹出式菜单
 * Created by Administrator on 2016/8/28.
 */
public class BottomMenuDialog extends BottomBaseDialog<BottomMenuDialog> implements View.OnClickListener  {

    private int cancelBarHeight;
    private int layoutRes;
    private View cancelView;
    private List<IconFontTextItem> items;
    private OnMenuItemClick onMenuItemClick;
    private BeforeShowCallBack beforeShowCallBack;

    public BottomMenuDialog(Context context, @LayoutRes int layoutRes, int cancelBarHeight, View animateView,BeforeShowCallBack callBack) {
        super(context, animateView);
        this.layoutRes = layoutRes;
        this.cancelBarHeight = cancelBarHeight;
        this.beforeShowCallBack = callBack;
    }

    @Override
    public View onCreateView() {
        //布局根节点
        View inflate = View.inflate(getContext(), layoutRes, null);
        //初始化整个Dialog的内外尺寸
        initLayoutParams(inflate);
        //初始化内部所有控件;
        initView(inflate);

        if (beforeShowCallBack!=null){
            beforeShowCallBack.beforeShow(items);
        }
        return inflate;
    }

    private void initView(View inflate) {
        items = new ArrayList<>();
        addViewsToList((ViewGroup) inflate);
    }

    private void addViewsToList(ViewGroup inflate) {
        int childCount = inflate.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = inflate.getChildAt(i);
            if (child instanceof IconFontTextItem) {
                items.add((IconFontTextItem) child);
            } else if (child instanceof ViewGroup) {
                addViewsToList((ViewGroup) child);
            }
        }
    }

    private void initLayoutParams(View inflate) {
        LinearLayout ll1 = (LinearLayout) inflate.findViewById(R.id.ll_1);
        LinearLayout ll2 = (LinearLayout) inflate.findViewById(R.id.ll_2);
        ViewGroup.LayoutParams params1 = ll1.getLayoutParams();
        params1.height = HomeActivity.instanse().getScreenWidth() / 4;
        ll1.setLayoutParams(params1);

        ViewGroup.LayoutParams params2 = ll2.getLayoutParams();
        params2.height = HomeActivity.instanse().getScreenWidth() / 4;
        ll2.setLayoutParams(params2);

        cancelView = inflate.findViewById(R.id.cancelView);
        ViewGroup.LayoutParams cancelViewLayoutParams = cancelView.getLayoutParams();
        cancelViewLayoutParams.height = cancelBarHeight;
        cancelView.setLayoutParams(cancelViewLayoutParams);

    }

    @Override
    public void setUiBeforeShow() {
        cancelView.setOnClickListener(this);
        if (items != null) {
            for (IconFontTextItem iftView : items) {
                iftView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelView) {
            this.dismiss();
            return;
        }
        if (!(v instanceof IconFontTextItem))
            return;
        int index;
        if ((index = items.indexOf(v)) != -1 && onMenuItemClick != null) {
            onMenuItemClick.onItemClick(index, (IconFontTextItem) v);
        }

    }

    public void setOnMenuItemClick(OnMenuItemClick onMenuItemClick) {
        this.onMenuItemClick = onMenuItemClick;
    }

    public interface OnMenuItemClick {
        void onItemClick(int index, IconFontTextItem view);
    }

    public interface BeforeShowCallBack{
        void beforeShow(List<IconFontTextItem> list);
    }

}
