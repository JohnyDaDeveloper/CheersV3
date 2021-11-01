package cz.johnyapps.cheers.views.graphview;

import static cz.johnyapps.cheers.views.graphview.GraphView.createDebugPaint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.johnyapps.cheers.entities.KeyValue;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.ThemeUtils;

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
    private final Paint debugPaint;
    @NonNull
    private final Paint debugHighlightPaint = new Paint();
    @NonNull
    private final Paint valuePaint = new Paint();
    @NonNull
    private final Paint valueHighlightPaint = new Paint();

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
    private final int valueSize;
    private final int valueHitBoxSize;

    private float timeMarkerHalfHeight;

    private int renderingState = FIRST_TIME;
    private final static int FIRST_TIME = 0;
    private final static int NOT_RENDERING = 1;
    private final static int RENDERING = 2;

    @Nullable
    private Value clickedValue = null;

    @NonNull
    private List<Value> values = new ArrayList<>();
    @Nullable
    private OnValueClickListener onValueClickListener;

    public Render(@NonNull List<KeyValue<GraphValueSet, GraphValue>> graphValues,
                  @NonNull Date startTime,
                  @NonNull Date endTime,
                  @NonNull StaticLayout startText,
                  @NonNull StaticLayout endText,
                  @NonNull Paint basePaint,
                  int position,
                  float maxValue,
                  float minValue,
                  int valueSize,
                  int valueHitBoxSize,
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
        this.valueSize = valueSize;
        this.valueHitBoxSize = valueHitBoxSize;

        debugPaint = createDebugPaint();
        debugHighlightPaint.setColor(ThemeUtils.addAlpha(125, debugPaint.getColor()));
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

    public boolean onClick(float x, float y) {
        for (Value value : values) {
            if (distanceFrom(x, y, value.getX(), value.getY()) <= valueHitBoxSize) {
                if (onValueClickListener != null) {
                    onValueClickListener.onValueClick(value.getGraphValueSet(), value.getGraphValue());
                }

                clickedValue = value;
                valueHighlightPaint.setColor(ThemeUtils.addAlpha(125, value.getGraphValueSet().getColor()));
                return true;
            }
        }

        return false;
    }

    public double distanceFrom(float ax, float ay, float bx, float by) {
        float dx = Math.abs(ax - bx);
        float dy = Math.abs(ay - by);

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void deselectClicked() {
        clickedValue = null;
    }

    public void draw(@NonNull Canvas canvas) {
        if (right > (-startText.getWidth() / 2f) &&
                left < (graphViewWidth + endText.getWidth() / 2f)) {
            if (renderingState == FIRST_TIME || renderingState == NOT_RENDERING) {
                if (debug) {
                    Logger.d(TAG, "draw: Render %s \tis rendering", position);
                }

                renderingState = RENDERING;
            }

            drawTimeMarkers(canvas);
            drawTimeTexts(canvas);
            drawDebug(canvas);
        } else if (renderingState == FIRST_TIME || renderingState == RENDERING) {
            if (debug) {
                Logger.d(TAG, "draw: Render %s \tis NOT rendering", position);
            }

            renderingState = NOT_RENDERING;
        }
    }

    public float drawValues(@NonNull Canvas canvas, float prevRenderMaxValue) {
        float renderMaxValue = prevRenderMaxValue;
        values = new ArrayList<>();

        if (right > (-startText.getWidth() / 2f) &&
                left < (graphViewWidth + endText.getWidth() / 2f)) {

            float timeDiff = endTime.getTime() - startTime.getTime();
            float width = right - left;
            float height = bottom - top;
            int posInList = 0;

            for (KeyValue<GraphValueSet, GraphValue> keyValue : graphValues) {
                GraphValueSet graphValueSet = keyValue.getKey();
                GraphValue graphValue = keyValue.getValue();
                long timeFromStart = graphValue.getTime().getTime() - startTime.getTime();
                renderMaxValue += graphValue.getValue(graphValueSet);

                float x = left + width * (timeFromStart / timeDiff);
                float y = bottom - height * ((renderMaxValue - minValue) / (maxValue - minValue));
                values.add(new Value(x, y, posInList, graphValueSet, graphValue));

                if (debug) {
                    canvas.drawCircle(x, y, valueHitBoxSize, debugHighlightPaint);
                }

                if (clickedValue != null && clickedValue.getPosInList() == posInList) {
                    canvas.drawCircle(x,
                            y,
                            valueSize * 1.7f,
                            valueHighlightPaint);
                }

                valuePaint.setColor(graphValueSet.getColor());
                canvas.drawCircle(x, y, valueSize, valuePaint);
                posInList++;
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
            //Render box
            canvas.drawLine(left, top, right, top, debugPaint);
            canvas.drawLine(left, bottom, right, bottom, debugPaint);
            canvas.drawLine(left, top, left, bottom, debugPaint);
            canvas.drawLine(right, top, right, bottom, debugPaint);

            //Number of render
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

    @NonNull
    public Render setOnValueClickListener(@Nullable OnValueClickListener onValueClickListener) {
        this.onValueClickListener = onValueClickListener;
        return this;
    }

    public boolean isRendering() {
        return renderingState == RENDERING;
    }
}
