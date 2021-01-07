package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main,
    MainViewModel::class
) {

    override fun initUi() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        with (PrefsUtils.getUser()) {
            binding.bnvMain.menu.findItem(R.id.menu_shifts).isVisible = this?.manager != true && this?.agreement == true
            binding.bnvMain.menu.findItem(R.id.menu_planning).isVisible = this?.manager == true
            binding.bnvMain.menu.findItem(R.id.menu_schedule).isVisible = this?.manager != true
        }

        binding.bnvMain.setupWithNavController(navController)
    }

}