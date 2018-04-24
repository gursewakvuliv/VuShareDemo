package com.vusharedemo.ui.customview;


import android.content.Context;
import android.util.AttributeSet;


public class SquareWidthFitImageView extends android.support.v7.widget.AppCompatImageView {

    public SquareWidthFitImageView(Context context) {
        super(context);
    }


    public SquareWidthFitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public SquareWidthFitImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    	 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
