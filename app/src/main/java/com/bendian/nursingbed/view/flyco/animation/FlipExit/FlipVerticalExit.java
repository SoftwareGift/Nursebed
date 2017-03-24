package com.bendian.nursingbed.view.flyco.animation.FlipExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.bendian.nursingbed.view.flyco.animation.BaseAnimatorSet;


public class FlipVerticalExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationX", 0, 90),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}
