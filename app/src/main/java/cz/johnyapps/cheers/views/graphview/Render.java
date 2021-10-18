package cz.johnyapps.cheers.views.graphview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import cz.johnyapps.cheers.entities.KeyValue;
import cz.johnyapps.cheers.tools.Logger;

public class Render {
    private static final String TAG = "Render";

    @NonNull
    private final List<KeyValue<GraphValueSet, GraphValue>> graphValues;
    @NonNull
    private final Date startTime;
    @NonNull
    private final Date endTime;
    @NonNull
    private final StaticLayout startText;
    @NonNull
    private final StaticLayout endText;

    @NonNull
    private final Paint basePaint;
    @NonNull
    private final Paint debugPaint = new Paint();
    @NonNull
    private final Paint valuePaint = new Paint();

    private final int position;
    private boolean last = false;
    private final float maxValue;
    private final float minValue;
    private final boolean debug;

    private float left;
    private float top;
    private float right;
    private float bottom;
    private float textPosition;
    private float movedBy;
    private float graphViewWidth;
    private final float basePaintHalfWidth;

    private float timeMarkerHalfHeight;

    private int renderingState = FIRST_TIME;
    private final static int FIRST_TIME = 0;
    private final static int NOT_RENDERING = 1;
    private final static int RENDERING = 2;

    public Render(@NonNull List<KeyValue<GraphValueSet, GraphValue>> graphValues,
                  @NonNull Date startTime,
                  @NonNull Date endTime,
                  @NonNull StaticLayout startText,
                  @NonNull StaticLayout endText,
                  @NonNull Paint basePaint,
                  int position,
                  float maxValue,
                  float minValue,
                  boolean debug) {

        this.graphValues = graphValues;
        this.startTime = startTime;
        this.endTime = endTime;
        this.basePaint = basePaint;
        this.position = position;
        this.startText = startText;
        this.endText = endText;
        this.basePaintHalfWidth = basePaint.getStrokeWidth() / 2f;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.debug = debug;

        debugPaint.setStrokeWidth(2);
        debugPaint.setColor(Color.RED);
        debugPaint.setTextSize(50);
    }

    public void setPosition(float left,
                            float top,
                            float right,
                            float bottom,
                            float textPosition,
                            float movedBy) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.textPosition = textPosition;
        this.movedBy = movedBy;
        this.timeMarkerHalfHeight = basePaint.getStrokeWidth() * 2;
    }

    public void setGraphViewWidth(float graphViewWidth) {
        this.graphViewWidth = graphViewWidth;
    }

    public void draw(@NonNull Canvas canvas) {
        if (right > (-startText.getWidth() / 2f) &&
                left < (graphViewWidth + endText.getWidth() / 2f)) {
            if (renderingState == FIRST_TIME || renderingState == NOT_RENDERING) {
                Logger.d(TAG, "draw: Render %s \tis rendering", position);
                renderingState = RENDERING;
            }

            drawTimeMarkers(canvas);
            drawTimeTexts(canvas);
            drawDebug(canvas);
        } else if (renderingState == FIRST_TIME || renderingState == RENDERING) {
            Logger.d(TAG, "draw: Render %s \tis NOT rendering", position);
            renderingState = NOT_RENDERING;
        }
    }

    public float drawValues(@NonNull Canvas canvas, float prevRenderMaxValue) {
        float renderMaxValue = prevRenderMaxValue;

        if (right > (-startText.getWidth() / 2f) &&
                left < (graphViewWidth + endText.getWidth() / 2f)) {
            float timeDiff = endTime.getTime() - startTime.getTime();
            float width = right - left;
            float height = bottom - top;

            for (KeyValue<GraphValueSet, GraphValue> keyValue : graphValues) {
                GraphValueSet graphValueSet = keyValue.getKey();
                GraphValue graphValue = keyValue.getValue();
                long timeFromStart = graphValue.getTime().getTime() - startTime.getTime();
                renderMaxValue += graphValue.getValue(graphValueSet);

                valuePaint.setColor(graphValueSet.getColor());
                canvas.drawCircle(left + width * (timeFromStart / timeDiff),
                        bottom - height * ((renderMaxValue - minValue) / (maxValue - minValue)),
                        15,
                        valuePaint);
            }
        } else {
            for (KeyValue<GraphValueSet, GraphValue> keyValue : graphValues) {
                GraphValueSet graphValueSet = keyValue.getKey();
                GraphValue graphValue = keyValue.getValue();
                renderMaxValue += graphValue.getValue(graphValueSet);
            }
        }

        return renderMaxValue;
    }

    private void drawTimeTexts(@NonNull Canvas canvas) {
        float endTextRight = right - endText.getWidth() / 2f + basePaintHalfWidth;

        if (position == 0) {
            GraphView.drawText(canvas,
                    startText,
                    left - basePaintHalfWidth,
                    textPosition);

            float adjustedEndTextRight = endTextRight - endText.getWidth() / 2f + movedBy;
            if (adjustedEndTextRight < endTextRight) {
                endTextRight = adjustedEndTextRight;
            }
        } else if (last) {
            endTextRight -= endText.getWidth() / 2f;
        }

        GraphView.drawText(canvas,
                endText,
                endTextRight,
                textPosition);
    }

    private void drawDebug(@NonNull Canvas canvas) {
        if (debug) {
            canvas.drawLine(left, top, right, top, debugPaint);
            canvas.drawLine(left, bottom, right, bottom, debugPaint);
            canvas.drawLine(left, top, left, bottom, debugPaint);
            canvas.drawLine(right, top, right, bottom, debugPaint);

            canvas.drawText(String.valueOf(position), left + 10, top + 50, debugPaint);
        }
    }

    private void drawTimeMarkers(@NonNull Canvas canvas) {
        float startMarkerLeft = left;
        canvas.drawLine(startMarkerLeft,
                bottom - timeMarkerHalfHeight,
                startMarkerLeft,
                bottom + timeMarkerHalfHeight,
                basePaint);

        float endMarkerRight = right;
        canvas.drawLine(endMarkerRight,
                bottom - timeMarkerHalfHeight,
                endMarkerRight,
                bottom + timeMarkerHalfHeight,
                basePaint);
    }

    @NonNull
    public Render setLast(boolean last) {
        this.last = last;
        return this;
    }
}
