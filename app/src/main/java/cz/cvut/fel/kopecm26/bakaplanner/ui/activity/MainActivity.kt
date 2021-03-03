package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.user.UserRole
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.SessionUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.setupWithNavController
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel

class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main,
    MainViewModel::class
) {

    private var currentNavController: LiveData<NavController>? = null

    override fun initUi() {
        setupBottomNavigationBar()

        if (SessionUtils.getUserRole() == UserRole.AGREEMENT) {
            Logger.d(SessionUtils.getUserRole())
            binding.bnvMain.menu.findItem(R.id.shifts).isVisible = true
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bnvMain

        bottomNavigationView.inflateMenu(
            if (PrefsUtils.getUser()?.manager == true) {
                R.menu.main_manager
            }
            else {
                R.menu.main_employee
            }
        )

        val navGraphIds = if (PrefsUtils.getUser()?.manager == true) {
            listOf(
                R.navigation.home,
                R.navigation.planning,
                R.navigation.organization,
                R.navigation.profile,
            )
        } else {
            listOf(
                R.navigation.home,
                R.navigation.schedule,
                R.navigation.shifts,
                R.navigation.profile,
            )
        }

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment
        )

        currentNavController = controller
    }
}
