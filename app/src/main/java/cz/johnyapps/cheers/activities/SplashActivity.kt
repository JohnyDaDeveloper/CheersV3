package cz.johnyapps.cheers.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import cz.johnyapps.cheers.SharedPrefsNames
import cz.johnyapps.cheers.tools.SharedPrefsUtils
import cz.johnyapps.cheers.views.CounterView

class SplashActivity: BaseActivity() {
    private var migrationsFinished = false
    private var counterViewMeasured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnMigrationFinishedListener {
            if (counterViewMeasured) {
                startNextAndExit()
            }
            migrationsFinished = true
        }
        executeMigrations()
        measureCounter()
    }

    private fun measureCounter() {
        val layout = LinearLayout(this)
        val counterView = CounterView(this)
        counterView.setOnSizeChangedListener { _: Int, height: Int ->
            if (height > 0) {
                SharedPrefsUtils.getGeneralPrefs(this)
                    .edit()
                    .putInt(SharedPrefsNames.COUNTER_HEIGHT, height)
                    .apply()
            }
            if (migrationsFinished && !counterViewMeasured) {
                startNextAndExit()
            }
            counterViewMeasured = true
        }
        counterView.visibility = View.INVISIBLE
        layout.addView(counterView)
        setContentView(layout)
    }

    private fun startNextAndExit() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}