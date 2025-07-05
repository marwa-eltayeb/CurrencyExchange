package com.marwaeltayeb.currencyexchange.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

object AnimationUtils {

    /**
     * Rotates a view (e.g., ImageView) 360 degrees with animation.
     * @param view The view to animate.
     * @param duration The duration of the animation in milliseconds (default is 500ms).
     */
    fun animateViewRotation(view: View, duration: Long = 500) {
        val rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotationAnimator.duration = duration
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.start()
    }
}
