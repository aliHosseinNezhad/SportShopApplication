package com.gamapp.sportshopapplication.ui.customViews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.TitledImageBinding


class TitledImage(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var binding: TitledImageBinding
    private var tint: Int = 0
    private var scaleIndex: Int = 0
    private var itemCount: String = ""
    private var imageResourceId: Int = 0
    private var titleWeight: Float = 1f
    private var imageWeight: Float = 3f


    init {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet) {
        context.let {
            var typedArray = it.obtainStyledAttributes(attrs, R.styleable.TitledImage)
            imageResourceId = typedArray.getResourceId(R.styleable.TitledImage_src, 0)
            itemCount = typedArray.getString(R.styleable.TitledImage_title) ?: ""
            scaleIndex = typedArray.getInt(R.styleable.TitledImage_android_scaleType, 3)
            tint = typedArray.getColor(R.styleable.TitledImage_tint, Color.BLACK)
            titleWeight = typedArray.getFloat(R.styleable.TitledImage_title_weight, 1f)
            imageWeight = typedArray.getFloat(R.styleable.TitledImage_image_weight, 1f)
            typedArray.recycle()
        }

        binding = TitledImageBinding
            .inflate(LayoutInflater.from(context), this, true)

        binding.image.let {
            it.setImageResource(imageResourceId)
            (it.layoutParams as LinearLayout.LayoutParams).weight = imageWeight
            it.scaleType = getImageScaleType(scaleIndex)
        }
        binding.title.let {
            it.text = itemCount
            (it.layoutParams as LinearLayout.LayoutParams).weight = titleWeight
        }
    }


    private fun getImageScaleType(index: Int): ImageView.ScaleType {
        return when (index) {
            0 -> ImageView.ScaleType.MATRIX
            1 -> ImageView.ScaleType.FIT_XY
            2 -> ImageView.ScaleType.FIT_START
            3 -> ImageView.ScaleType.FIT_CENTER
            4 -> ImageView.ScaleType.FIT_END
            5 -> ImageView.ScaleType.CENTER
            6 -> ImageView.ScaleType.CENTER_CROP
            7 -> ImageView.ScaleType.CENTER_INSIDE
            else -> ImageView.ScaleType.CENTER_INSIDE
        }
    }

}