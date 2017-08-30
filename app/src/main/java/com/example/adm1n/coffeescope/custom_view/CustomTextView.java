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
    private Paint paintRed;
    private Paint paintWhite;
    private float padding = 3f;

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

        paintRed = new Paint();
        paintRed.setColor(mColor);
        paintRed.setStrokeWidth(3);

        paintWhite = new Paint();
        paintWhite.setColor(resources.getColor(R.color.white));
        paintWhite.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, (getHeight() / 2) + padding, getWidth(), (getHeight() / 2) + padding, paintWhite);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintRed);
        canvas.drawLine(0, getHeight() / 2 - padding, getWidth(), getHeight() / 2 - padding, paintWhite);
    }
}
