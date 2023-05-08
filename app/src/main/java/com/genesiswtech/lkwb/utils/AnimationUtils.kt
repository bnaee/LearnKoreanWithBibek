package com.genesiswtech.lkwb.utils

import android.animation.Animator

import android.animation.AnimatorListenerAdapter
import android.view.View


object AnimationUtils {
    fun slideDown(view: View) {
        view.animate()
            .translationY(view.height.toFloat())
            .alpha(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // superfluous restoration
                    view.setVisibility(View.GONE)
                    view.setAlpha(1f)
                    view.setTranslationY(0f)
                }
            })
    }

    fun slideUp(view: View) {
        view.setVisibility(View.VISIBLE)
        view.setAlpha(0f)
        if (view.getHeight() > 0) {
            slideUpNow(view)
        } else {
            // wait till height is measured
            view.post(Runnable { slideUpNow(view) })
        }
    }

    private fun slideUpNow(view: View) {
        view.setTranslationY(view.height.toFloat())
        view.animate()
            .translationY(0F)
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.setVisibility(View.VISIBLE)
                    view.setAlpha(1f)
                }
            })
    }
}