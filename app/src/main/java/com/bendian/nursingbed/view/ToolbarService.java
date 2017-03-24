package com.bendian.nursingbed.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bendian.nursingbed.R;

/**
 * Toolbar工具类
 * Created by Administrator on 2016/8/10.
 */
public class ToolbarService {

    private final IconTextView navigateIcon;
    private final TextView title;
    private final IconTextView item1;
    private final IconTextView item2;
    private final ViewGroup viewGroupp;
    private OnItemClickListener onItemClickListener;

    private ToolbarService(ViewGroup viewGroup){
        this.viewGroupp = viewGroup;
        navigateIcon = (IconTextView) viewGroup.getChildAt(0);
        title = (TextView) viewGroup.getChildAt(1);
        item1 = (IconTextView) viewGroup.getChildAt(3);
        item2 = (IconTextView) viewGroup.getChildAt(2);
        ClickListener listener = new ClickListener();
        navigateIcon.setOnClickListener(listener);
        item1.setOnClickListener(listener);
        item2.setOnClickListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public View getChildAt(int index){
        return viewGroupp.getChildAt(index);
    }

    public ToolbarService setNavIcon(int res){
        navigateIcon.setText(res);
        return this;
    }
    public ToolbarService setItem1(int res){
        item1.setText(res);
        return this;
    }
    public ToolbarService setItem2(int res){
        item2.setText(res);
        return this;
    }

    public ToolbarService setTitle(String stringTitle){
        title.setText(stringTitle);
        return this;
    }
    public ToolbarService setTitle(int res){
        title.setText(res);
        return this;
    }

    public static ToolbarService instance(ViewGroup viewGroup){
        return new ToolbarService(viewGroup);
    }

    public interface OnItemClickListener{
        void onClick(Item item);
    }
    public enum Item{
        nav,item1,item2
    }
    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (onItemClickListener!=null) {
                switch (v.getId()) {
                    case R.id.toolbarMenu:
                        onItemClickListener.onClick(Item.nav);
                        break;
                    case R.id.toolbarItem1:
                        onItemClickListener.onClick(Item.item1);
                        break;
                    case R.id.toolbarItem2:
                        onItemClickListener.onClick(Item.item2);
                        break;
                }
            }
        }
    }
}
