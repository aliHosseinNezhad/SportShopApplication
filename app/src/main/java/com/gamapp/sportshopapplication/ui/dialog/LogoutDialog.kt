package com.gamapp.sportshopapplication.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.LogoutDialogLayoutBinding


class LogoutDialog(context: Context) : Dialog(context) {
    lateinit var bindig: LogoutDialogLayoutBinding
    private var param: ((Boolean) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = LogoutDialogLayoutBinding.inflate(LayoutInflater.from(context))
        setContentView(bindig.root)
        window?.let {
            var metrics = get(2,1)
            it.setLayout(metrics.width,metrics.Height)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setUpViews()
    }

    fun onResultListener(param: (result: Boolean) -> Unit) {
        this.param = param
    }

    private fun setUpViews() {
        bindig.ok.setOnClickListener {
            param?.let { it1 -> it1(true) }
            dismiss()
        }
        bindig.cancel.setOnClickListener {
            param?.let { it1 -> it1(false) }
            dismiss()
        }
    }

    private fun get(width: Int, height: Int): Matrices {
        var heightP = context.resources.displayMetrics.heightPixels.toFloat()
        var widthP = context.resources.displayMetrics.widthPixels.toFloat()
        widthP *= 0.9f
        heightP = widthP * height.toFloat() / width.toFloat()
        var metrics = Matrices(widthP.toInt(),heightP.toInt())
        return metrics
    }

    inner class Matrices(var width: Int, var Height: Int)
}