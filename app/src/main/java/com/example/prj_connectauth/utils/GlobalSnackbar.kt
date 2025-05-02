package com.example.prj_connectauth.utils

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar

private fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

object GlobalSnackbar {

    fun invokeSnackbar(view: View, mensagem: String, colorI: String="", anchor:View? = null){

        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_LONG)

        var color = colorI.uppercase()

        when(color){
            "R" -> {
                snackbar.setTextColor("#FF3636".toColorInt())
            }

            "G" -> {
                snackbar.setTextColor("#36FF36".toColorInt())
            }

            else -> {
                snackbar.setTextColor("#FFFF36".toColorInt())
            }
        }

        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams

        anchor?.let {
            snackbar.anchorView = it
        }

        if (anchor == null){
            params.gravity = Gravity.TOP and Gravity.CENTER_HORIZONTAL
            params.setMargins(0, 32.dpToPx(), 0, 0)
            snackbarView.layoutParams = params
        }

        snackbar.setBackgroundTint("#002C2C2C".toColorInt())
        snackbar.show()

    }

}