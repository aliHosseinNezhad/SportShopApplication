package com.gamapp.sportshopapplication.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gamapp.sportshopapplication.R
import com.gamapp.sportshopapplication.databinding.BorderLineLayoutBinding

class BorderLine(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
    defStyleRes: Int
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var lineOrientation: Int = 0
    private var lineWidth: Float = 1f
    private lateinit var binding: BorderLineLayoutBinding


    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        context.let {
            var typedArray = it.obtainStyledAttributes(attrs, R.styleable.BorderLine)
            lineWidth = typedArray.getDimension(R.styleable.BorderLine_line_width, 2f)
            lineOrientation = typedArray.getInt(R.styleable.BorderLine_orientation,0)
            typedArray.recycle()
        }
        binding = BorderLineLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        binding.let {
            if(lineOrientation == 0){
                it.verticalLine.visibility = View.VISIBLE
                it.horizontalLine.visibility = View.GONE
                it.verticalLine.layoutParams.width = lineWidth.toInt()
            }else if(lineOrientation == 1){
                it.verticalLine.visibility = View.GONE
                it.horizontalLine.visibility = View.VISIBLE
                it.horizontalLine.layoutParams.height = lineWidth.toInt()
            }
        }

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

}
