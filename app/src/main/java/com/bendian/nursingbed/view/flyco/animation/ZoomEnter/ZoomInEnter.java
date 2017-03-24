package com.bendian.nursingbed.view.flyco.animation.ZoomEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.bendian.nursingbed.view.flyco.animation.BaseAnimatorSet;


public class ZoomInEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1),//
				ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1),//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1));//
	}
}
