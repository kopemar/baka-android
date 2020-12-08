package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.annotation.IdRes
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.HomeFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.ProfileFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main.ScheduleFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main,
    MainViewModel::class
) {
    private var selectedFragment = -1

    override fun initUi() {
        setFragment(R.id.menu_home)
        binding.bnvMain.setOnNavigationItemSelectedListener {
            it.itemId.let(::setFragment)
            true
        }
    }

    private fun setFragment(@IdRes id: Int) {
        Logger.d(id)
        if (selectedFragment == id) return
        supportFragmentManager.beginTransaction()
            .run { supportFragmentManager.findFragmentByTag(selectedFragment.toString())?.let { hide(it) } ?: this }
            .run {
                supportFragmentManager.findFragmentByTag(id.toString())?.let(::show)
                    ?: this.add(binding.frameLayout.id, createFragment(id), id.toString())
            }.commit()
        selectedFragment = id
    }

    private fun createFragment(@IdRes id: Int) = when (id) {
        R.id.menu_home -> HomeFragment()
        R.id.menu_profile -> ProfileFragment()
        R.id.menu_schedule -> ScheduleFragment()
        else -> HomeFragment()
    }

}