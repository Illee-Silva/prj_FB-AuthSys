package com.example.prj_connectauth.utils

import android.view.View
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar

object GlobalSnackbar {

    fun invokeSnackbar(view: View, mensagem: String){

        val snackbar = Snackbar.make(view, "${mensagem}", Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#FF3636".toColorInt())
        snackbar.setBackgroundTint("#000000".toColorInt())
        snackbar.show()

    }

}