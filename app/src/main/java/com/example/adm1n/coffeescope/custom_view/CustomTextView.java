package com.example.adm1n.coffeescope.custom_view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.adm1n.coffeescope.R;

/**
 * Created by adm1n on 16.08.2017.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    private int mColor;
    private Paint paint;

    public CustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        //Color
        mColor = resources.getColor(R.color.red);

        paint = new Paint();
        paint.setColor(mColor);
        //Width
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
    }
}
