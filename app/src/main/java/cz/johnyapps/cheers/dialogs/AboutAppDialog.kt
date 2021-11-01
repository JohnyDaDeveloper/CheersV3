package cz.johnyapps.cheers.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import cz.johnyapps.cheers.BuildConfig;
import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.dialogs.customdialogbuilder.CustomDialogBuilder;
import cz.johnyapps.cheers.tools.TextUtils;
import cz.johnyapps.cheers.tools.TimeUtils;

public class AboutAppDialog {
    @NonNull
    private final Context context;

    public AboutAppDialog(@NonNull Context context) {
        this.context = context;
    }

    public void show() {
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.showLargeHeader()
                .setView(R.layout.dialog_about_app)
                .setNeutralButton(R.string.close, (dialog, which) -> {});

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        AppCompatTextView versionTextView = alertDialog.findViewById(R.id.versionTextView);
        assert versionTextView != null;
        versionTextView.setText(TextUtils.fromHtml(String.format("<b>%s:</b> %s",
                context.getResources().getString(R.string.about_app_version),
                BuildConfig.VERSION_NAME)));

        AppCompatTextView buildTextView = alertDialog.findViewById(R.id.buildTextView);
        assert buildTextView != null;
        buildTextView.setText(TextUtils.fromHtml(String.format("<b>%s:</b> %s",
                context.getResources().getString(R.string.about_app_build),
                BuildConfig.VERSION_CODE)));

        AppCompatTextView buildTimeTextView = alertDialog.findViewById(R.id.buildTimeTextView);
        assert buildTimeTextView != null;
        buildTimeTextView.setText(TextUtils.fromHtml(String.format("<b>%s:</b> %s",
                context.getResources().getString(R.string.about_app_build_time),
                TimeUtils.toDateAndTime(BuildConfig.BUILD_TIME))));
    }
}
