package com.gamapp.sportshopapplication.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Display
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources

import androidx.core.graphics.get
import kotlin.math.roundToInt

class ImageUtils {
    companion object {
        fun resizeImage(
            imageResource: Int,
            activity: Activity
        ): Drawable { // R.drawable.large_image
            // Get device dimensions
            val display: Display? = activity.windowManager?.defaultDisplay
            val deviceWidth = display?.width?.toDouble()
            val bd = AppCompatResources.getDrawable(
                activity.applicationContext!!, imageResource
            ) as BitmapDrawable
            val imageHeight = bd.bitmap.height.toDouble()
            val imageWidth = bd.bitmap.width.toDouble()

            val ratio = deviceWidth?.div(imageWidth)
            val newImageHeight = (imageHeight * ratio!!).toInt()
            val bMap = BitmapFactory.decodeResource(activity.resources, imageResource)
            return BitmapDrawable(
                activity.resources,
                getResizedBitmap(bMap, newImageHeight, deviceWidth.toInt())
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun myImageResize(imageResource: Int, activity: Activity): Bitmap? {
            activity.applicationContext.resources.displayMetrics.let {
                var screenWidth = it.widthPixels
                var screenHeight = it.heightPixels
                var bitmapFactory = BitmapFactory.decodeResource(activity.resources, imageResource)

                bitmapFactory.width
                bitmapFactory.height

                var x = bitmapFactory.get(3,5)
                Color.valueOf(x)

                return getMyResizedBitmap(bitmapFactory, screenHeight, screenWidth)
            }
        }

        fun getMyResizedBitmap(
            bitmapFactory: Bitmap?,
            screenHeight: Int,
            screenWidth: Int
        ): Bitmap? {
            bitmapFactory?.let { bitmap ->
                val imageWidth = bitmap.width
                val imageHeight = bitmap.height
                val ratio: Float =
                    if (imageWidth > imageHeight) screenWidth.toFloat() / imageWidth.toFloat()
                    else screenHeight.toFloat() / imageHeight.toFloat()
                return Bitmap.createScaledBitmap(
                    bitmap, (imageWidth * ratio * 0.5f).roundToInt(),
                    (imageHeight * ratio * 0.5f).roundToInt(), true
                );
            } ?: return null
        }

        /************************ Resize Bitmap  */
        fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
            val width = bm.width
            val height = bm.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height

            // create a matrix for the manipulation
            val matrix = Matrix()

            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight)

            // recreate the new Bitmap
            return Bitmap.createBitmap(
                bm, 0, 0, width, height,
                matrix, false
            )
        }
    }
}