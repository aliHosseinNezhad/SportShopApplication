package com.gamapp.sportshopapplication.ui.customViews;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

public class RoundBlurView2 extends androidx.appcompat.widget.AppCompatImageView {
    private Path mMaskPath;
    private Paint mMaskPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mCornerRadius   = 10;

    public RoundBlurView2(Context context) {
        super(context);

//        init(context);
    }

    public RoundBlurView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

//        init(context);
    }

    public RoundBlurView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

//        init(context);
    }
//
//    private void init(Context context) {
//        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);
//        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        mMaskPaint.setColor(context.getResources().getColor(R.color.transparent));
//
//        mCornerRadius = (int) context.getResources().getDimension(R.dimen.image_border_curvature);
//    }
//
//    /**
//     * Set the corner radius to use for the RoundedRectangle.
//     */
//    public void setCornerRadius(int cornerRadius) {
//        mCornerRadius = cornerRadius;
//        generateMaskPath(getWidth(), getHeight());
//        invalidate();
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
//        super.onSizeChanged(w, h, oldW, oldH);
//
//        if (w != oldW || h != oldH) {
//            generateMaskPath(w, h);
//        }
//    }
//
//    private void generateMaskPath(int w, int h) {
//        mMaskPath = new Path();
//        mMaskPath.addRoundRect(new RectF(0,0,w,h), mCornerRadius, mCornerRadius, Path.Direction.CW);
//        mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if(canvas.isOpaque()) { // If canvas is opaque, make it transparent
//            canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
//        }
//
//        super.onDraw(canvas);
//
//        if(mMaskPath != null) {
//            canvas.drawPath(mMaskPath, mMaskPaint);
//        }
//    }
}