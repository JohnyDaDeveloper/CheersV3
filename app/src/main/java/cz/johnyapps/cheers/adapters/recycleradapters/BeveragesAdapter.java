package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.GetBeverageDescriptionTask;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.TextUtils;

public class BeveragesAdapter extends ExpandableItemAdapter<BeveragesAdapter.BeverageViewHolder, Beverage> {
    public BeveragesAdapter(@NonNull Context context,
                            @Nullable List<Beverage> beverages) {
        super(context, beverages);
        setMultiSelection(false);
    }

    @NonNull
    @Override
    protected String getItemTextForFilter(@NonNull Beverage beverage) {
        return beverage.getName();
    }

    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeverageViewHolder(getInflater().inflate(R.layout.item_beverage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageViewHolder holder, int position, boolean selected, boolean expanded) {
        Beverage beverage = getItem(position);
        holder.nameTextView.setText(beverage.getName());

        if (expanded) {
            GetBeverageDescriptionTask task = new GetBeverageDescriptionTask(getContext());
            task.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<GetBeverageDescriptionTask.Result>() {
                @Override
                public void onSuccess(@Nullable GetBeverageDescriptionTask.Result result) {
                    if (holder.getAdapterPosition() == position) {
                        fillDetails(holder.descriptionLayout.findViewById(R.id.descriptionTextView),
                                getItem(position),
                                result);
                    }
                }

                @Override
                public void onFailure(@Nullable Exception e) {

                }

                @Override
                public void onComplete() {

                }
            });
            task.execute(beverage);
        }
    }

    private void fillDetails(@NonNull AppCompatTextView descriptionTextView,
                             @NonNull Beverage beverage,
                             @Nullable GetBeverageDescriptionTask.Result result) {
        String text = "";

        if (beverage.getAlcohol() > 0) {
            text += getContext().getResources().getString(R.string.beverage_recycler_view_item_description_alcohol,
                    TextUtils.decimalToStringWithTwoDecimalDigits(beverage.getAlcohol()));
            text += "\n";
        }

        if (result != null) {
            text += getContext().getResources().getString(R.string.beverage_recycler_view_item_description_rest,
                    TextUtils.decimalToStringWithTwoDecimalDigits(result.getLastVisit()),
                    TextUtils.decimalToStringWithTwoDecimalDigits(result.getLastVisitVolume()),
                    TextUtils.decimalToStringWithTwoDecimalDigits(result.getTotal()));
        } else {
            text += getContext().getResources().getString(R.string.beverage_recycler_view_item_description_no_data);
        }

        descriptionTextView.setText(text);
    }

    public class BeverageViewHolder extends ExpandableItemAdapter<BeverageViewHolder, Beverage>.ExpandableItemViewHolder {
        AppCompatTextView nameTextView;
        View descriptionLayout;

        public BeverageViewHolder(@NonNull View root) {
            super(root);

            nameTextView = root.findViewById(R.id.nameTextView);
            descriptionLayout = root.findViewById(R.id.descriptionLayout);
        }

        @NonNull
        @Override
        public View getExpandView(@NonNull View root) {
            return descriptionLayout;
        }
    }
}
