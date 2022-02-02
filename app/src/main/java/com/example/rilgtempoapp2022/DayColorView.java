package com.example.rilgtempoapp2022;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.rilgtempoapp2022.TempoColor;

/**
 * Custom view to display tempo color
 */
class DayColorView extends View {
    Context context;
    static final float CIRCLE_SCALE = 0.9f; // half circle will occupy 90% of view

    // custom attributes data
    private String captionText;
    private int captionTextColor = Color.BLACK; // default value
    private float captionTextSize = 14;
    private int dayCircleColor = Color.GRAY;

    private TextPaint textPaint;
    private Paint circlePaint;
    private float textWidth;
    private float textHeight;

    public DayColorView(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0);
    }

    public DayColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DayColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // keep context
        this.context = context;
        // Load custom attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DayColorView, defStyle, 0);
        captionText = a.getString(R.styleable.DayColorView_captionText);
        if (captionText == null) captionText = "undefined";
        captionTextColor = a.getColor(R.styleable.DayColorView_captionTextColor, ContextCompat.getColor(context, R.color.black));
        captionTextSize = a.getDimension(R.styleable.DayColorView_captionTextSize, getResources().getDimension(R.dimen.tempo_color_text_size));
        dayCircleColor = a.getColor(R.styleable.DayColorView_dayCircleColor, ContextCompat.getColor(context, R.color.tempo_undecided_day_bg));

        // Recycles the TypedArray, to be re-used by a later caller
        a.recycle();

        // set a text paint and corresponding text measurements from attributes
        textPaint = new TextPaint();
        setTextPaintAndMeasurements();

        // set a circle paint from attributes
        circlePaint = new Paint();
        setCirclePaint();
    }

    private void setTextPaintAndMeasurements() {

        // Set up a default TextPaint object
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(captionTextSize);
        textPaint.setColor(captionTextColor);

        // compute dimensions to be used for drawing text
        textWidth = textPaint.measureText(captionText);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = fontMetrics.bottom;
    }

    private void setCirclePaint() {
        // set up a default Pain object to draw circle
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(dayCircleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // compute view dimensions
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the background circle with the expected color
        float radius = Math.min(contentWidth, contentHeight) * 0.5f * CIRCLE_SCALE;
        canvas.drawCircle(contentWidth * 0.5f, contentHeight * 0.5f, radius, circlePaint );

        // Draw the caption text.
        canvas.drawText(captionText,
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint);
    }

    public void setDayColor(TempoColor color) {
        dayCircleColor = ContextCompat.getColor(context, color.getResId());
        setCirclePaint();
        invalidate();
    }

}