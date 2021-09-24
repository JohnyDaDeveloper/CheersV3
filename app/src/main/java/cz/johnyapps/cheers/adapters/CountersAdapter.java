package cz.johnyapps.cheers.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.views.CounterView;

public class CountersAdapter extends SelectableAdapter<CountersAdapter.CounterViewHolder, CounterWithBeverage, List<CounterWithBeverage>> {
    @NonNull
    private List<CounterWithBeverage> countersWithBeverages;

    public CountersAdapter(@NonNull Context context,
                           @Nullable List<CounterWithBeverage> countersWithBeverages,
                           @NonNull OnSelectListener<CounterWithBeverage> onSelectListener) {
        super(context, onSelectListener);
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
        holder.counterView.setOnPassClickListener(v -> selectPosition(position));
        holder.counterView.setPassClicks(selected);
    }

    @Override
    public int getItemCount() {
        return countersWithBeverages.size();
    }

    public class CounterViewHolder extends SelectableAdapter<CounterViewHolder, CounterWithBeverage, List<CounterWithBeverage>>.SelectableViewHolder {
        CounterView counterView;

        public CounterViewHolder(@NonNull View root) {
            super(root);
            counterView = root.findViewById(R.id.counterView);
        }
    }
}
