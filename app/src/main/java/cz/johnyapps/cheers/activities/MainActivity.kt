package cz.johnyapps.cheers.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.ActivityMainBinding
import cz.johnyapps.cheers.dialogs.AboutAppDialog
import cz.johnyapps.cheers.fragments.BackOptionFragment

class MainActivity: NavigationActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMenuItems()
    }

    override fun onBackPressed() {
        val fragment = currentFragment
        if (fragment is BackOptionFragment && (fragment as BackOptionFragment).onBackPressed()) {
            return
        }

        super.onBackPressed()
    }

    private fun setupMenuItems() {
        val navMenu = navigationView.menu
        navMenu.findItem(R.id.aboutAppMenuItem).setOnMenuItemClickListener { item: MenuItem? ->
            val aboutAppDialog = AboutAppDialog(this)
            aboutAppDialog.show()
            drawerLayout.close()
            false
        }
    }
}