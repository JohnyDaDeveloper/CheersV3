package cz.johnyapps.cheers.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.card.MaterialCardView;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.database.tasks.UpdateCounterCountTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.TextUtils;
import cz.johnyapps.cheers.tools.TimeUtils;

public class CounterView extends LinearLayout {
    private MaterialCardView counterCardView;

    private AppCompatTextView nameTextView;
    private AppCompatTextView valueTextView;
    private AppCompatTextView plusTextView;
    private AppCompatTextView minusTextView;

    @Nullable
    private CounterWithBeverage counterWithBeverage;
    @Nullable
    private OnPassClickListener onPassClickListener;
    @Nullable
    private OnValueChangeListener onValueChangeListener;
    @Nullable
    private OnSizeChangedListener onSizeChangedListener;
    private boolean passClicks = false;
    @Nullable
    private String titleText = null;
    private int count = 0;

    public CounterView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public CounterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CounterView(@NonNull Context context, @Nullable AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CounterView, theme, 0);

            titleText = array.getString(R.styleable.CounterView_titleText);

            array.recycle();
        }

        View root = LayoutInflater.from(getContext()).inflate(R.layout.view_counter, this, true);

        counterCardView = root.findViewById(R.id.counterCardView);

        nameTextView = root.findViewById(R.id.nameTextView);
        nameTextView.setText(titleText);
        valueTextView = root.findViewById(R.id.valueTextView);
        valueTextView.setText(String.valueOf(count));

        plusTextView = root.findViewById(R.id.plusTextView);
        plusTextView.setOnLongClickListener(v -> {
            if (onPassClickListener != null) {
                onPassClickListener.onClick(v);
            }

            return false;
        });

        minusTextView = root.findViewById(R.id.minusTextView);
        minusTextView.setOnLongClickListener(v -> {
            if (onPassClickListener != null) {
                onPassClickListener.onClick(v);
            }

            return false;
        });

        setPassClicks(passClicks);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(w, h);
        }
    }

    public void setTitleText(@StringRes int titleResId) {
        this.titleText = getResources().getString(titleResId);

        if (nameTextView != null) {
            nameTextView.setText(titleResId);
        }
    }

    public void setCount(int count) {
        this.count = count;

        if (valueTextView != null) {
            valueTextView.setText(String.valueOf(count));
        }
    }

    public void setOnPassClickListener(@Nullable OnPassClickListener onPassClickListener) {
        this.onPassClickListener = onPassClickListener;

        if (plusTextView != null) {
            plusTextView.setOnLongClickListener(v -> {
                if (this.onPassClickListener != null) {
                    this.onPassClickListener.onClick(v);
                }

                return false;
            });
        }

        if (minusTextView != null) {
            minusTextView.setOnLongClickListener(v -> {
                if (this.onPassClickListener != null) {
                    this.onPassClickListener.onClick(v);
                }

                return false;
            });
        }
    }

    private void changeCounterValue(int add) {
        if (this.counterWithBeverage != null) {
            Counter counter = this.counterWithBeverage.getCounter();

            int count = counter.getCount() + add;

            if (add > 0) {
                counter.addCounterEntry();
                this.count = count;
                updateCounter(this.counterWithBeverage);
            } else if (count >= 0) {
                counter.removeLastCounterEntry();
                this.count = count;
                updateCounter(this.counterWithBeverage);
            }
        } else {
            int count = this.count + add;

            if (count >= 0) {
                this.count = count;
            }
        }

        valueTextView.setText(String.valueOf(count));

        if (onValueChangeListener != null) {
            onValueChangeListener.onChange(count);
        }
    }

    private void updateCounter(@NonNull CounterWithBeverage counterWithBeverage) {
        UpdateCounterCountTask task = new UpdateCounterCountTask(getContext());
        task.execute(counterWithBeverage.getCounter());
    }

    public void setCounter(@NonNull CounterWithBeverage counterWithBeverage) {
        this.counterWithBeverage = counterWithBeverage;
        fillCounter(counterWithBeverage);
    }

    @Nullable
    public CounterWithBeverage getCounter() {
        return counterWithBeverage;
    }

    private void fillCounter(@NonNull CounterWithBeverage counterWithBeverage) {
        Beverage beverage = counterWithBeverage.getBeverage();
        Counter counter = counterWithBeverage.getCounter();
        int textColor = beverage.getTextColor();

        nameTextView.setTextColor(textColor);
        valueTextView.setTextColor(textColor);
        plusTextView.setTextColor(textColor);
        minusTextView.setTextColor(textColor);

        counterCardView.setCardBackgroundColor(beverage.getColor());
        nameTextView.setText(String.format(TimeUtils.getLocale(),
                "%s %s%s",
                beverage.getName(),
                TextUtils.decimalToStringWithTwoDecimalDigits(counter.getVolume()),
                getContext().getResources().getString(R.string.liter)));

        valueTextView.setText(String.valueOf(counter.getCount()));
    }

    public void setPassClicks(boolean passClicks) {
        this.passClicks = passClicks;

        if (passClicks) {
            plusTextView.setOnClickListener(v -> {
                if (onPassClickListener != null) {
                    onPassClickListener.onClick(v);
                }

                performClick();
            });
            minusTextView.setOnClickListener(v -> {
                if (onPassClickListener != null) {
                    onPassClickListener.onClick(v);
                }

                performClick();
            });
        } else {
            plusTextView.setOnClickListener(v -> changeCounterValue(1));
            minusTextView.setOnClickListener(v -> changeCounterValue(-1));
        }
    }

    public void setOnValueChangeListener(@Nullable OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    public void setOnSizeChangedListener(@Nullable OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public interface OnPassClickListener {
        void onClick(@NonNull View v);
    }

    public interface OnValueChangeListener {
        void onChange(int value);
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int width, int height);
    }
}
