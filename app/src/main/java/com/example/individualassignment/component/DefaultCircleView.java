package com.example.individualassignment.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DefaultCircleView extends View {
    private Paint paint;
    private int color;
    private int desiredSize;

    public DefaultCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setColor(Color.LTGRAY); // Use Color.LTGRAY for a lighter gray shade

        // Set default desired size
        desiredSize = 500;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setActivityLevel(int activityLevel) {
        if (activityLevel == 1) {
            desiredSize = 500;
        } else if (activityLevel == 2) {
            desiredSize = 400;
        } else if (activityLevel == 3) {
            desiredSize = 300;
        } else if (activityLevel == 4) {
            desiredSize = 200;
        }
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredSize, widthSize);
        } else {
            width = desiredSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredSize, heightSize);
        } else {
            height = desiredSize;
        }

        setMeasuredDimension(width, height);
    }
}