package com.gamapp.sportshopapplication.ui.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.gamapp.sportshopapplication.R
import eightbitlab.com.blurview.BlurView


class RoundBlurView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    BlurView(context, attrs, defStyleAttr) {
    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
//        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundishImageView)
//        cornerRadius = a.getDimensionPixelSize(R.styleable.RoundishImageView_cornerRadius, 0)
//        roundedCorners = a.getInt(R.styleable.RoundishImageView_roundedCorners, CORNER_NONE)
//        a.recycle()
    }

    private val CORNER_NONE = 0
    private val CORNER_TOP_LEFT = 1
    private val CORNER_TOP_RIGHT = 2
    private val CORNER_BOTTOM_RIGHT = 4
    private val CORNER_BOTTOM_LEFT = 8
    private val CORNER_ALL = 15

    private val CORNERS = intArrayOf(
        CORNER_TOP_LEFT,
        CORNER_TOP_RIGHT,
        CORNER_BOTTOM_RIGHT,
        CORNER_BOTTOM_LEFT
    )

    private val path: Path = Path()
    private var cornerRadius = 24
    private var roundedCorners = 24


    fun setCornerRadius(radius: Int) {
        if (cornerRadius != radius) {
            cornerRadius = radius
            setPath()
            invalidate()
        }
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun setRoundedCorners(corners: Int) {
        if (roundedCorners != corners) {
            roundedCorners = corners
            setPath()
            invalidate()
        }
    }

    fun isCornerRounded(corner: Int): Boolean {
        return roundedCorners and corner == corner
    }
    private fun getPath(
        radius: Float, topLeft: Boolean, topRight: Boolean,
        bottomRight: Boolean, bottomLeft: Boolean
    ): Path {
        val path = Path()
        val radii = FloatArray(8)
        if (topLeft) {
            radii[0] = radius
            radii[1] = radius
        }
        if (topRight) {
            radii[2] = radius
            radii[3] = radius
        }
        if (bottomRight) {
            radii[4] = radius
            radii[5] = radius
        }
        if (bottomLeft) {
            radii[6] = radius
            radii[7] = radius
        }
        path.addRoundRect(
            RectF(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat()),
            radii, Path.Direction.CW
        )
        return path
    }
    override fun onDraw(canvas: Canvas) {
        if (!path.isEmpty()) {
            val radius = context.resources.getDimension(R.dimen.round_corner_radius)
            val path: Path = getPath(
                radius,
                true,
                true,
                false,
                false
            )
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setPath()
    }

    private fun setPath() {
        path.rewind()
        if (cornerRadius >= 1f && roundedCorners != CORNER_NONE) {
            val radii = FloatArray(8)
            for (i in 0..3) {
                if (isCornerRounded(CORNERS[i])) {
                    radii[2 * i] = cornerRadius.toFloat()
                    radii[2 * i + 1] = cornerRadius.toFloat()
                }
            }
            path.addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                radii, Path.Direction.CW
            )
        }
    }

}