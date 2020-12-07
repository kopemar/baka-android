package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.HomeFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.ProfileFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.ScheduleFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.setFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main, MainViewModel::class) {

    private val homeFragment by lazy { HomeFragment() }
    private val scheduleFragment by lazy { ScheduleFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    override fun initUi() {
        setFragment(binding.frameLayout.id, HomeFragment())

        binding.bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> setFragment(binding.frameLayout.id, homeFragment)
                R.id.menu_profile -> setFragment(binding.frameLayout.id, profileFragment)
                R.id.menu_schedule -> setFragment(binding.frameLayout.id, scheduleFragment)
            }
            true
        }
    }



}