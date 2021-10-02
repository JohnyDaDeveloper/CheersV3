package cz.johnyapps.cheers.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.NumberUtils;
import cz.johnyapps.cheers.tools.TextUtils;

public class EditBeverageDialog {
    private static final String TAG = "EditBeverageDialog";

    @NonNull
    private final Context context;

    public EditBeverageDialog(@NonNull Context context) {
        this.context = context;
    }

    public void show(@NonNull Beverage beverage,
                     @NonNull OnEditCompleteListener onEditCompleteListener) {
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.setTitle(R.string.dialog_edit_beverage_title)
                .setView(R.layout.dialog_edit_beverage)
                .setCancelable(false)
                .setPositiveButton(R.string.save, (dialog, which) ->
                        save((AlertDialog) dialog, beverage, onEditCompleteListener))
                .setNeutralButton(R.string.cancel, (dialog, which) -> {});

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        fillBeverage(alertDialog, beverage);

        View colorView = alertDialog.findViewById(R.id.colorView);
        assert colorView != null;
        colorView.setOnClickListener(v -> {
            ColorPickerDialog colorPickerDialog = new ColorPickerDialog(context);
            colorPickerDialog.show((color, brightness1) -> v.setBackgroundColor(color));
        });
    }

    private void fillBeverage(@NonNull AlertDialog alertDialog, @NonNull Beverage beverage) {
        AppCompatEditText nameEditText = alertDialog.findViewById(R.id.nameEditText);
        assert nameEditText != null;
        nameEditText.setText(beverage.getName());

        AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
        assert alcoholEditText != null;

        if (beverage.getAlcohol() > 0) {
            alcoholEditText.setText(TextUtils.decimalToStringWithTwoDecimalDigits(beverage.getAlcohol()));
        } else {
            alcoholEditText.setText(null);
        }

        View colorView = alertDialog.findViewById(R.id.colorView);
        assert colorView != null;
        colorView.setBackgroundColor(beverage.getColor());
    }

    private void save(@NonNull AlertDialog alertDialog,
                      @NonNull Beverage beverage,
                      @NonNull OnEditCompleteListener onEditCompleteListener) {
        AppCompatEditText nameEditText = alertDialog.findViewById(R.id.nameEditText);
        assert nameEditText != null;
        String name = nameEditText.getText() == null ? null : nameEditText.getText().toString();

        AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
        assert alcoholEditText != null;
        String strAlcohol = alcoholEditText.getText() == null ? null : alcoholEditText.getText().toString().replaceAll(",", ".");
        float alcohol = NumberUtils.toFloat(strAlcohol, -1);

        if (name != null) {
            beverage.setName(name);
        }

        if (alcohol > 0) {
            beverage.setAlcohol(alcohol);
        }

        View colorView = alertDialog.findViewById(R.id.colorView);
        assert colorView != null;
        Drawable background = colorView.getBackground();

        if (background instanceof ColorDrawable) {
            int color = ((ColorDrawable) background).getColor();
            beverage.setColor(color);
            beverage.setTextColor(Color.luminance(color)  < 0.21f ? Color.WHITE : Color.BLACK);
        } else {
            Logger.w(TAG, "save: Background is not instance on ColorDrawable (instead it's '%s')", background.getClass());
        }

        if (name == null || name.isEmpty()) {
            show(beverage, onEditCompleteListener);
        } else {
            beverage.setName(name);
            beverage.setAlcohol(alcohol);
            onEditCompleteListener.onComplete(beverage);
        }
    }

    public interface OnEditCompleteListener {
        void onComplete(@NonNull Beverage beverage);
    }
}
