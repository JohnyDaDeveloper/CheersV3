package cz.johnyapps.cheers.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.views.ColorPickerWheelView;

public class ColorPickerDialog {
    private static final String TAG = "ColorPickerDialog";

    private final Context context;
    private AlertDialog dialog;

    private Float[] hsv = new Float[3];
    private int brightness = 0;
    private int maxBrightness = 0;

    public ColorPickerDialog(@NonNull Context context) {
        this.context = context;

        setDefaultColor();
    }

    public void show(@NonNull OnColorPickedListener onColorPickedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_color_picker)
                .setPositiveButton(R.string.pick, (dialog, which) -> {
                    float[] hsv = getHSV();
                    onColorPickedListener.onPicker(Color.HSVToColor(getHSV()), hsv[2]);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});

        dialog = builder.create();
        dialog.show();

        ColorPickerWheelView colorPickerWheelView = dialog.findViewById(R.id.colorPickerWheelView);
        colorPickerWheelView.getColorObservable().observe(floats -> {
            hsv = floats;
            updateColorView();
        });

        AppCompatSeekBar seekBar = dialog.findViewById(R.id.brightnessSeekBar);
        seekBar.setProgress(seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
                updateColorView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxBrightness = seekBar.getMax();
        brightness = maxBrightness;
        updateColorView();
    }

    private void setDefaultColor() {
        float[] hvs = new float[3];
        Color.colorToHSV(Color.WHITE, hvs);

        for (int i = 0; i < this.hsv.length; i++) {
            this.hsv[i] = hvs[i];
        }
    }

    private void updateColorView() {
        if (dialog == null) {
            Logger.w(TAG, "updateColorView: dialog is null");
            return;
        }

        View colorView = dialog.findViewById(R.id.colorView);
        colorView.setBackgroundColor(Color.HSVToColor(getHSV()));
    }

    private float[] getHSV() {
        float[] hsv = new float[this.hsv.length];
        for (int i = 0; i < this.hsv.length; i++) {
            hsv[i] = this.hsv[i];
        }

        hsv[2] = brightness / (float) maxBrightness;
        return hsv;
    }

    public interface OnColorPickedListener {
        void onPicker(int color, float brightness);
    }
}
