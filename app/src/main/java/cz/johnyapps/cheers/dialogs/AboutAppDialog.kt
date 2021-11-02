package cz.johnyapps.cheers.dialogs

import android.content.Context
import android.content.DialogInterface
import cz.johnyapps.cheers.dialogs.customdialogbuilder.CustomDialogBuilder
import cz.johnyapps.cheers.R
import androidx.appcompat.widget.AppCompatTextView
import cz.johnyapps.cheers.BuildConfig
import cz.johnyapps.cheers.tools.TextUtils
import cz.johnyapps.cheers.tools.TimeUtils

class AboutAppDialog(private val context: Context) {
    fun show() {
        val builder = CustomDialogBuilder(context)
        builder.showLargeHeader()
            .setView(R.layout.dialog_about_app)
            .setPositiveButton(R.string.close) { _: DialogInterface?, _: Int -> }

        val alertDialog = builder.create()
        alertDialog.show()

        val versionTextView = alertDialog.findViewById<AppCompatTextView>(R.id.versionTextView)!!
        versionTextView.text = TextUtils.fromHtml("<b>${context.resources.getString(R.string.about_app_version)}: </b> ${BuildConfig.VERSION_NAME}")

        val buildTextView = alertDialog.findViewById<AppCompatTextView>(R.id.buildTextView)!!
        buildTextView.text = TextUtils.fromHtml("<b>${context.resources.getString(R.string.about_app_build)}: </b> ${BuildConfig.VERSION_CODE}")

        val buildTimeTextView = alertDialog.findViewById<AppCompatTextView>(R.id.buildTimeTextView)!!
        buildTimeTextView.text = TextUtils.fromHtml("<b>${context.resources.getString(R.string.about_app_build_time)}: </b> ${TimeUtils.toDateAndTime(BuildConfig.BUILD_TIME)}")
    }
}