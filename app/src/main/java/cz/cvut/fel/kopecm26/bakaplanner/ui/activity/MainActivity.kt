package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.setupWithNavController
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel

class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main,
    MainViewModel::class
) {

    private var currentNavController: LiveData<NavController>? = null

    override fun initUi() {
        with(PrefsUtils.getUser()) {
            binding.bnvMain.menu.findItem(R.id.shifts).isVisible =
                this?.manager != true && this?.agreement == true
            binding.bnvMain.menu.findItem(R.id.planning).isVisible = this?.manager == true
            binding.bnvMain.menu.findItem(R.id.schedule).isVisible = this?.manager != true
        }

        setupBottomNavigationBar()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnv_main)

        val navGraphIds = listOf(
            R.navigation.home,
            R.navigation.schedule,
            R.navigation.planning,
            R.navigation.shifts,
            R.navigation.profile
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment
        )

        currentNavController = controller
    }
}
