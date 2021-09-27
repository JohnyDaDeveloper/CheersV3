package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.beverage.Beverage;

public class BeveragesAdapter extends FilterableAdapter<BeveragesAdapter.BeverageViewHolder, Beverage> {
    public BeveragesAdapter(@NonNull Context context,
                            @Nullable List<Beverage> beverages) {
        super(context, beverages);
    }

    @Override
    protected boolean filterItem(@NonNull CharSequence constraint, @NonNull Beverage beverage) {
        return beverage.getName().toLowerCase().contains(constraint.toString().toLowerCase());
    }

    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeverageViewHolder(getInflater().inflate(R.layout.item_beverage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageViewHolder holder, int position, boolean selected) {
        Beverage beverage = getItem(position);
        holder.nameTextView.setText(beverage.getName());
    }

    public class BeverageViewHolder extends SelectableAdapter<BeverageViewHolder, Beverage, List<Beverage>>.SelectableViewHolder {
        AppCompatTextView nameTextView;

        public BeverageViewHolder(@NonNull View root) {
            super(root);

            nameTextView = root.findViewById(R.id.nameTextView);
        }
    }
}
