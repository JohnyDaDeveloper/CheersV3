package cz.johnyapps.cheers.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.observers.Observable;
import cz.johnyapps.cheers.tools.Logger;

public class ColorPickerWheelView extends View implements View.OnTouchListener {
    private static final String TAG = "ColorPickerView";
    private static final int WHEEL_SPACING = 10;

    private final Paint huePaint = new Paint();
    private final Paint saturationPaint = new Paint();
    private ColorSelector colorSelector;
    private final Observable<Float[]> colorObservable = new Observable<>();

    private float radius;
    private float cx;
    private float cy;

    private static final int[] COLORS = new int[]{
            Color.RED,
            Color.MAGENTA,
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.YELLOW,
            Color.RED
    };

    public ColorPickerWheelView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public ColorPickerWheelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ColorPickerWheelView(@NonNull Context context, @Nullable AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        huePaint.setAntiAlias(true);
        saturationPaint.setAntiAlias(true);

        setOnTouchListener(this);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Logger.d(TAG, "maxWidth: %s (%s), maxHeight: %s (%s)",
                maxWidth,
                widthMode,
                maxHeight,
                heightMode);

        int width = maxWidth;
        int height = maxHeight;

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            if (maxWidth > maxHeight) {
                width = maxHeight;
            } else {
                height = maxWidth;
            }
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
            if (width < height) {
                height = width;
            }
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
            if (height < width) {
                width = height;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouch(@NonNull View v, @NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            moveSelector(event.getX(), event.getY());
            invalidate();
            updateObservers();
            return true;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        radius = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom()) / 2f - WHEEL_SPACING;

        cx = w / 2f + (getPaddingLeft() - getPaddingRight());
        cy = h / 2f + (getPaddingTop() - getPaddingBottom());

        Shader hueShader = new SweepGradient(cx, cy, COLORS, null);
        Shader saturationShader = new RadialGradient(cx,
                cy,
                radius,
                Color.WHITE,
                0x00FFFFFF,
                Shader.TileMode.CLAMP);

        huePaint.setShader(hueShader);
        saturationPaint.setShader(saturationShader);

        colorSelector = new ColorSelector(cx, cy);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cx, cy, radius, huePaint);
        canvas.drawCircle(cx, cy, radius, saturationPaint);

        if (colorSelector != null) {
            colorSelector.draw(canvas);
        }
    }

    private void updateObservers() {
        float x = colorSelector.x - cx;
        float y = colorSelector.y - cy;
        double r = Math.sqrt(x * x + y * y);
        Float[] hsv = {0f, 0f, 1f};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / radius)));

        colorObservable.setValue(hsv);
    }

    private void moveSelector(float x, float y) {
        float deltaX = x - cx;
        float deltaY = y - cy;

        if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= radius) {
            colorSelector.x = x;
            colorSelector.y = y;
        }
    }

    @NonNull
    public Observable<Float[]> getColorObservable() {
        return colorObservable;
    }

    private static class ColorSelector {
        public float x;
        public float y;

        private static final float SELECTOR_RADIUS = 15;
        private static final int SELECTOR_STROKE_WIDTH = 2;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private ColorSelector(float x, float y) {
            this.x = x;
            this.y = y;

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(SELECTOR_STROKE_WIDTH);
        }

        public void draw(Canvas canvas) {
            canvas.drawCircle(x, y, SELECTOR_RADIUS, paint);
        }
    }
}
