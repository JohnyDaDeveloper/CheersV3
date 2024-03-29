package cz.johnyapps.cheers.views.graphview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.KeyValue;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.NumberUtils;
import cz.johnyapps.cheers.tools.ThemeUtils;
import cz.johnyapps.cheers.tools.TimeUtils;

public class GraphView extends View implements View.OnTouchListener, OnValueClickListener {
    private static final String TAG = "GraphView";

    private static final long TIME_GAP = 7200000;
    private static final int SPEED_MULTIPLIER = 40;
    private static final int MAX_VALUE_MULTIPLE_OF = 250;

    @NonNull
    private final Paint backgroundPain = new Paint();
    @NonNull
    private final Paint basePaint = new Paint();
    @NonNull
    private Paint debugPaint = new Paint();
    private float basePaintHalfWidth;
    private boolean debug = false;
    private float clickLineAtX = -1f;
    private float clickLineAtY = -1f;

    private float left;
    private float top;
    private float right;
    private float bottom;

    private float bottomMarkerHalfHeight;
    private float width;

    private int minValue = 0;
    private int maxValue = 10;
    private StaticLayout minValueText;
    private StaticLayout maxValueText;
    @Nullable
    private String valueSuffix;
    private int valueSize = 15;
    private int valueHitBoxSize = 30;
    private int lineWidth = 10;

    private float movedBy = 0;
    private float prevMovementMovedBy = movedBy;
    private float movementStart = -1f;
    private float maxMoveBy = 0;
    private long movementStartTime;
    private float speed = -1;
    private boolean allowAutoScroll = false;
    @NonNull
    private final Handler handler = new Handler();

    @NonNull
    private List<KeyValue<GraphValueSet, GraphValue>> graphValues = new ArrayList<>();
    @NonNull
    private List<Render> renders = new ArrayList<>();
    @Nullable
    private OnValueClickListener onValueClickListener;

    public GraphView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public GraphView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GraphView(@NonNull Context context, @Nullable AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.GraphView, theme, 0);

            debug = array.getBoolean(R.styleable.GraphView_debug, false);
            valueSuffix = array.getString(R.styleable.GraphView_valueSuffix);
            valueSize = array.getDimensionPixelSize(R.styleable.GraphView_valueSize, ThemeUtils.dpToPx(getContext(), 7));
            lineWidth = array.getDimensionPixelSize(R.styleable.GraphView_lineWidth, ThemeUtils.dpToPx(getContext(), 4));

            valueHitBoxSize = Math.max(ThemeUtils.dpToPx(getContext(), 15), valueSize);

            array.recycle();
        }

        if (debug) {
            setGraphValueSets(generatePreviewValues());
        }

        backgroundPain.setColor(ThemeUtils.getAttributeColor(R.attr.colorSurface, getContext()));

        basePaint.setStrokeWidth(lineWidth);
        basePaint.setColor(ThemeUtils.getAttributeColor(R.attr.colorOnSurface, getContext()));

        debugPaint = createDebugPaint();

        bottomMarkerHalfHeight = basePaint.getStrokeWidth() * 2;
        basePaintHalfWidth = basePaint.getStrokeWidth() / 2f;
        movedBy = -basePaintHalfWidth;
        initializeTexts();

        setOnTouchListener(this);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (allowAutoScroll) {
                    if (Math.abs(speed) > 2) {
                        if (speed > 0) {
                            speed -= Math.max(1, speed * 0.1f);
                        } else {
                            speed -= Math.min(-1, speed * 0.1f);
                        }

                        movedBy = Math.max(Math.min(movedBy - speed, maxMoveBy), -basePaintHalfWidth);
                        invalidate();
                    } else {
                        allowAutoScroll = false;
                    }
                }

                handler.post(this);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            movedBy = Math.max(Math.min(prevMovementMovedBy + movementStart - event.getX(), maxMoveBy), -basePaintHalfWidth);
            invalidate();

            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            allowAutoScroll = false;
            movementStartTime = System.currentTimeMillis();
            prevMovementMovedBy = movedBy;
            movementStart = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float movedByThisSwipe = prevMovementMovedBy - movedBy;
            long movementLength = System.currentTimeMillis() - movementStartTime;

            if (movementLength < 170 && Math.abs(movedByThisSwipe) < 50) {
                onClick(event.getX(), event.getY());
            } else {
                speed = movedByThisSwipe / movementLength * SPEED_MULTIPLIER;
                allowAutoScroll = Math.abs(speed) > SPEED_MULTIPLIER * 2;
            }

            movementStart = -1f;
            movementStartTime = 0;
            return false;
        }

        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawPaint(backgroundPain);

        left = getPaddingLeft() + findValueTextMaxWidth() + basePaintHalfWidth;
        top = getPaddingTop() + maxValueText.getHeight() / 2f;
        right = getWidth() - getPaddingRight() - basePaintHalfWidth;
        bottom = getHeight() - getPaddingBottom() - bottomMarkerHalfHeight - findTimeTextMaxHeight();

        width = right - left;

        if (renders.size() > 0) {
            maxMoveBy = (renders.size() - 1) * width - basePaintHalfWidth;

            if (movedBy > maxMoveBy) {
                movedBy = maxMoveBy;
            }
        }

        drawBottom(canvas);
        drawRenders(canvas);
        drawValues(canvas);
        drawLeft(canvas);
        drawDebug(canvas);
    }

    @Override
    public void onValueClick(@NonNull GraphValueSet graphValueSet, @NonNull GraphValue graphValue) {
        if (onValueClickListener != null) {
            onValueClickListener.onValueClick(graphValueSet, graphValue);
        }
    }

    private void drawDebug(@NonNull Canvas canvas) {
        if (debug) {
            canvas.drawLine(clickLineAtX, 0, clickLineAtX, getHeight(), debugPaint);
            canvas.drawLine(0, clickLineAtY, getWidth(), clickLineAtY, debugPaint);
        }
    }

    private void onClick(float x, float y) {
        clickLineAtX = x;
        clickLineAtY = y;
        int selectedRender = -1;

        for (int i = 0; i < renders.size(); i++) {
            Render render = renders.get(i);

            if (render.isRendering() && render.onClick(x, y)) {
                selectedRender = i;
                break;
            }
        }

        if (selectedRender >= 0) {
            for (int i = 0; i < renders.size(); i++) {
                if (i != selectedRender) {
                    renders.get(i).deselectClicked();
                }
            }
        }

        invalidate();
    }

    private void createRenders() {
        if (graphValues.isEmpty()) {
            Logger.d(TAG, "createRenders: Nothing to render");
            renders = new ArrayList<>();
            return;
        }

        Date startTime = graphValues.get(0).getValue().getTime();

        List<Render> renders = new ArrayList<>();
        List<KeyValue<GraphValueSet, GraphValue>> graphValues = new ArrayList<>();

        for (KeyValue<GraphValueSet, GraphValue> keyValue : this.graphValues) {
            Date time = keyValue.getValue().getTime();

            if (time.getTime() - startTime.getTime() > TIME_GAP) {
                while (time.getTime() - startTime.getTime() > TIME_GAP) {
                    Date endTime = new Date(startTime.getTime() + TIME_GAP);
                    renders.add(new Render(graphValues,
                            startTime,
                            endTime,
                            initializeText(TimeUtils.toTime(startTime)),
                            initializeText(TimeUtils.toTime(endTime)),
                            basePaint,
                            renders.size(),
                            maxValue,
                            minValue,
                            valueSize,
                            valueHitBoxSize,
                            debug).setOnValueClickListener(this));

                    if (debug) {
                        Logger.d(TAG, "createRenders: Render created with time %s", TimeUtils.toTime(keyValue.getValue().getTime()));
                    }

                    graphValues = new ArrayList<>();
                    startTime = new Date(startTime.getTime() + TIME_GAP);
                }

            }
            graphValues.add(keyValue);
        }

        if (!graphValues.isEmpty()) {
            Date endTime = new Date(startTime.getTime() + TIME_GAP);
            renders.add(new Render(graphValues,
                    startTime,
                    endTime,
                    initializeText(TimeUtils.toTime(startTime)),
                    initializeText(TimeUtils.toTime(new Date(startTime.getTime() + TIME_GAP))),
                    basePaint,
                    renders.size(),
                    maxValue,
                    minValue,
                    valueSize,
                    valueHitBoxSize,
                    debug).setLast(true)
                    .setOnValueClickListener(this));

            if (debug) {
                Logger.d(TAG, "createRenders: One more render created");
            }
        } else {
            renders.get(renders.size() - 1).setLast(true);
        }

        if (debug) {
            Logger.d(TAG, "createRenders: Created %s renders", renders.size());
        }

        this.renders = renders;
    }

    @NonNull
    private List<KeyValue<GraphValueSet, GraphValue>> toValueList(@Nullable List<? extends  GraphValueSet> graphValueSets) {
        if (graphValueSets == null) {
            return new ArrayList<>();
        }

        List<KeyValue<GraphValueSet, GraphValue>> keyValues = new ArrayList<>();

        for (GraphValueSet graphValueSet : graphValueSets) {
            List<? extends GraphValue> graphValues = graphValueSet.getGraphValues();

            for (GraphValue graphValue : graphValues) {
                keyValues.add(new KeyValue<>(graphValueSet, graphValue));
            }
        }

        keyValues.sort((o1, o2) -> {
            long o1Time = o1.getValue().getTime().getTime();
            long o2Time = o2.getValue().getTime().getTime();
            if (o1Time > o2Time) {
                return 1;
            } else if (o1Time == o2Time) {
                return 0;
            }

            return -1;
        });

        return keyValues;
    }

    private void drawRenders(@NonNull Canvas canvas) {
        for (int i = 0; i < renders.size(); i++) {
            Render render = renders.get(i);
            float offset = width * i - movedBy;
            render.setPosition(left + offset,
                    top,
                    right + offset,
                    bottom,
                    bottom + bottomMarkerHalfHeight,
                    movedBy);
            render.setGraphViewWidth(getWidth());
            render.draw(canvas);
        }
    }

    private void drawValues(@NonNull Canvas canvas) {
        float prevRenderMaxValue = 0f;

        for(Render render : renders) {
            prevRenderMaxValue = render.drawValues(canvas, prevRenderMaxValue);
        }
    }

    private void initializeTexts() {
        minValueText = initializeText(minValue + (valueSuffix == null ? "" : valueSuffix));
        maxValueText = initializeText(maxValue + (valueSuffix == null ? "" : valueSuffix));
    }

    private float findValueTextMaxWidth() {
        return Math.max(minValueText.getWidth(), maxValueText.getWidth());
    }

    private float findTimeTextMaxHeight() {
        return initializeText("00:00").getHeight();
    }

    private void drawLeft(@NonNull Canvas canvas) {
        canvas.drawRect(0,
                0,
                left,
                getHeight(),
                backgroundPain);

        canvas.drawRect(getWidth() - getPaddingRight() + basePaintHalfWidth,
                0,
                getWidth(),
                getHeight(),
                backgroundPain);

        float textLeft = left - basePaintHalfWidth;
        drawText(canvas,
                maxValueText,
                textLeft - maxValueText.getWidth(),
                top - maxValueText.getHeight() / 2f);

        drawText(canvas,
                minValueText,
                textLeft - minValueText.getWidth(),
                bottom - minValueText.getHeight() / 2f);
    }

    private void drawBottom(@NonNull Canvas canvas) {
        canvas.drawLine(left,
                bottom,
                right + basePaint.getStrokeWidth(),
                bottom,
                basePaint);
    }

    @NonNull
    private StaticLayout initializeText(@NonNull String text) {
        String[] lines = text.split("\n");

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        textPaint.setColor(basePaint.getColor());

        int width = 0;

        for (String line : lines) {
            int lineWidth = (int) textPaint.measureText(line);

            if (lineWidth > width) {
                width = lineWidth;
            }
        }

        StaticLayout.Builder builder = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, width);
        builder.setAlignment(Layout.Alignment.ALIGN_NORMAL);
        return builder.build();
    }

    public static void drawText(@NonNull Canvas canvas,
                          @Nullable StaticLayout staticLayout,
                          float x,
                          float y) {
        if (staticLayout != null) {
            canvas.save();
            canvas.translate(x, y);
            staticLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void findMaxValue(@Nullable List<? extends GraphValueSet> graphValueSets) {
        float maxValue = 0;

        if (graphValueSets == null) {
            return;
        }

        for (GraphValueSet graphValueSet : graphValueSets) {
            List<? extends GraphValue> graphValues = graphValueSet.getGraphValues();

            if (!graphValues.isEmpty()) {
                if (graphValueSet.sameValueForAllGraphValues()) {
                    maxValue += graphValues.get(0).getValue(graphValueSet) * graphValues.size();
                } else {
                    for (GraphValue graphValue : graphValues) {
                        maxValue += graphValue.getValue(graphValueSet);
                    }
                }
            }
        }

        this.maxValue = NumberUtils.roundUp(maxValue);
        this.maxValue = MAX_VALUE_MULTIPLE_OF * (this.maxValue / MAX_VALUE_MULTIPLE_OF + 1);
    }

    public void setGraphValueSets(@Nullable List<? extends GraphValueSet> graphValueSets) {
        this.graphValues = toValueList(graphValueSets);

        findMaxValue(graphValueSets);
        this.minValue = 0;
        createRenders();
        initializeTexts();
        invalidate();
    }

    public void selectValue(@Nullable GraphValue graphValue) {
        if (graphValue != null) {
            for (Render render : renders) {
                if (render.selectValue(graphValue)) {
                    if (debug) {
                        Logger.d(TAG, "selectValue: Found value in render %s", render.getPosition());
                    }

                    break;
                }
            }

            invalidate();
        } else if (debug) {
            Logger.d(TAG, "selectValue: Value is null");
        }
    }

    public void setOnValueClickListener(@Nullable OnValueClickListener onValueClickListener) {
        this.onValueClickListener = onValueClickListener;
    }

    @NonNull
    static Paint createDebugPaint() {
        Paint debugPaint = new Paint();
        debugPaint.setStrokeWidth(2);
        debugPaint.setColor(Color.RED);
        debugPaint.setTextSize(50);
        return debugPaint;
    }

    private static class PreviewGraphValueSet implements GraphValueSet {
        @ColorInt
        private final int color;
        @NonNull
        private final List<? extends GraphValue> graphValues;
        private final int pointValue;

        public PreviewGraphValueSet(@ColorInt int color,
                                    @NonNull List<? extends GraphValue> graphValues,
                                    int pointValue) {
            this.color = color;
            this.graphValues = graphValues;
            this.pointValue = pointValue;
        }

        @Override
        public int getColor() {
            return color;
        }

        @NonNull
        @Override
        public List<? extends GraphValue> getGraphValues() {
            return graphValues;
        }

        @Override
        public boolean sameValueForAllGraphValues() {
            return true;
        }

        public int getPointValue() {
            return pointValue;
        }
    }

    private static class PreviewGraphValue implements GraphValue {
        @NonNull
        private final Date time;

        public PreviewGraphValue(@NonNull Date time) {
            this.time = time;
        }

        @NonNull
        @Override
        public Date getTime() {
            return time;
        }

        @Override
        public float getValue(@NonNull GraphValueSet graphValueSet) {
            if (graphValueSet instanceof PreviewGraphValueSet) {
                return ((PreviewGraphValueSet) graphValueSet).getPointValue();
            }

            return 0;
        }
    }

    @NonNull
    private List<GraphValueSet> generatePreviewValues() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        List<List<GraphValue>> graphValues = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            graphValues.add(new ArrayList<>());
        }

        for (int i = 0; i < 50; i++) {
            calendar.add(Calendar.MINUTE, random.nextInt(25) + 10);
            graphValues.get(random.nextInt(3)).add(new PreviewGraphValue(calendar.getTime()));
        }

        List<GraphValueSet> sets = new ArrayList<>();
        int color = Color.RED;

        for (List<GraphValue> values : graphValues) {
            sets.add(new PreviewGraphValueSet(color, values, random.nextInt(20) + 10));
            color = ThemeUtils.getNextColor(color);
        }

        return sets;
    }
}
