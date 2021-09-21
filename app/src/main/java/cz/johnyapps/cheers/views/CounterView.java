package cz.johnyapps.cheers.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.card.MaterialCardView;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.database.tasks.UpdateCounterCountTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.TextUtils;
import cz.johnyapps.cheers.tools.TimeUtils;

public class CounterView extends LinearLayout {
    private static final String TAG = "CounterView";

    private MaterialCardView counterCardView;

    private AppCompatTextView nameTextView;
    private AppCompatTextView valueTextView;
    private AppCompatTextView plusTextView;
    private AppCompatTextView minusTextView;

    @Nullable
    private CounterWithBeverage counterWithBeverage;

    @Nullable
    private OnLongClickListener onPlusMinusLongClickListener = null;

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
        View root = LayoutInflater.from(getContext()).inflate(R.layout.view_counter, this, true);

        counterCardView = root.findViewById(R.id.counterCardView);

        nameTextView = root.findViewById(R.id.nameTextView);
        valueTextView = root.findViewById(R.id.valueTextView);

        plusTextView = root.findViewById(R.id.plusTextView);
        plusTextView.setOnClickListener(v -> changeCounterValue(1));
        plusTextView.setOnLongClickListener(onPlusMinusLongClickListener);

        minusTextView = root.findViewById(R.id.minusTextView);
        minusTextView.setOnClickListener(v -> changeCounterValue(-1));
        minusTextView.setOnLongClickListener(onPlusMinusLongClickListener);
    }

    public void setOnPlusMinusLongClickListener(@Nullable OnLongClickListener onPlusMinusLongClickListener) {
        this.onPlusMinusLongClickListener = onPlusMinusLongClickListener;

        if (plusTextView != null) {
            plusTextView.setOnLongClickListener(onPlusMinusLongClickListener);
        }

        if (minusTextView != null) {
            minusTextView.setOnLongClickListener(onPlusMinusLongClickListener);
        }
    }

    private void changeCounterValue(int add) {
        if (this.counterWithBeverage != null) {
            Counter counter = this.counterWithBeverage.getCounter();

            int count = counter.getCount() + add;

            if (count >= 0) {
                counter.setCount(count);
                valueTextView.setText(String.valueOf(count));

                updateCounter(this.counterWithBeverage);
            }
        } else {
            Logger.w(TAG, "changeCounterValue: CounterWithBeverage is null");
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

    private void fillCounter(@NonNull CounterWithBeverage counterWithBeverage) {
        Beverage beverage = counterWithBeverage.getBeverage();
        Counter counter = counterWithBeverage.getCounter();

        if (beverage != null) {
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
        }

        valueTextView.setText(String.valueOf(counter.getCount()));
    }
}
