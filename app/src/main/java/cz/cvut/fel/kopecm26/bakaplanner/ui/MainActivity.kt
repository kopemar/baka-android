package cz.cvut.fel.kopecm26.bakaplanner.ui

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.HomeFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.ProfileFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.ScheduleFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.setFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main, MainViewModel::class) {

    override fun initUi() {
        super.initUi()
        viewModel.init()

        setFragment(binding.frameLayout.id, HomeFragment())

        binding.bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> setFragment(binding.frameLayout.id, HomeFragment())
                R.id.menu_profile -> setFragment(binding.frameLayout.id, ProfileFragment())
                R.id.menu_schedule -> setFragment(binding.frameLayout.id, ScheduleFragment())
            }
            true
        }
    }



}