package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.views.CounterView;

public class CountersAdapter extends SelectableAdapter<CountersAdapter.CounterViewHolder, CounterWithBeverage, List<CounterWithBeverage>> {
    @NonNull
    private List<CounterWithBeverage> countersWithBeverages;
    private boolean allCountersDisabled = false;
    @Nullable
    private OnCounterClickListener onCounterClickListener;
    @Nullable
    private CounterView.OnSizeChangedListener onSizeChangedListener;

    public CountersAdapter(@NonNull Context context,
                           @Nullable List<CounterWithBeverage> countersWithBeverages) {
        super(context);
        this.countersWithBeverages = countersWithBeverages == null ? new ArrayList<>() : countersWithBeverages;
    }

    @NonNull
    @Override
    public CounterWithBeverage getItem(int position) {
        return countersWithBeverages.get(position);
    }

    @Override
    protected void onUpdate(@Nullable List<CounterWithBeverage> countersWithBeverages) {
        this.countersWithBeverages = countersWithBeverages == null ? new ArrayList<>() : countersWithBeverages;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CounterViewHolder(getInflater().inflate(R.layout.item_counter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CounterViewHolder holder, int position, boolean selected) {
        CounterWithBeverage counterWithBeverage = countersWithBeverages.get(position);
        holder.counterView.setCounter(counterWithBeverage);
        holder.counterView.setPassClicks(allCountersDisabled);
        holder.counterView.setOnPassClickListener(v -> {
            if (isAllowSelection()) {
                selectPosition(position);
            } else {
                if (onCounterClickListener != null) {
                    onCounterClickListener.onClick(holder.counterView.getCounter(), position);
                }
            }
        });
        holder.counterView.setOnLongClickListener(v -> {
            holder.itemView.performLongClick();
            return true;
        });
        holder.counterView.setOnClickListener(v -> {
            if (onCounterClickListener != null) {
                onCounterClickListener.onClick(holder.counterView.getCounter(), position);
            }

            holder.itemView.callOnClick();
        });
    }

    @Override
    public int getItemCount() {
        return countersWithBeverages.size();
    }

    public void setAllCountersDisabled(boolean allCountersDisabled) {
        this.allCountersDisabled = allCountersDisabled;
        notifyDataSetChanged();
    }

    public boolean isAllCountersDisabled() {
        return allCountersDisabled;
    }

    public void moveToTop(int position) {
        if (position > 0) {
            CounterWithBeverage counter = countersWithBeverages.get(position);
            countersWithBeverages.remove(position);
            countersWithBeverages.add(0, counter);
            notifyDataSetChanged();
        }
    }

    public void setOnSizeChangedListener(@Nullable CounterView.OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public void setOnCounterClickListener(@Nullable OnCounterClickListener onCounterClickListener) {
        this.onCounterClickListener = onCounterClickListener;
    }

    public interface OnCounterClickListener {
        void onClick(@Nullable CounterWithBeverage counterWithBeverage, int position);
    }

    public class CounterViewHolder extends SelectableAdapter<CounterViewHolder, CounterWithBeverage, List<CounterWithBeverage>>.SelectableViewHolder {
        CounterView counterView;

        public CounterViewHolder(@NonNull View root) {
            super(root);
            counterView = root.findViewById(R.id.counterView);

            int position = getAdapterPosition();
            if (position == 0 || position == RecyclerView.NO_POSITION) {
                counterView.setOnSizeChangedListener((width, height) -> {
                    if (onSizeChangedListener != null) {
                        onSizeChangedListener.onSizeChanged(width, height);
                    }
                });
            }
        }
    }
}
