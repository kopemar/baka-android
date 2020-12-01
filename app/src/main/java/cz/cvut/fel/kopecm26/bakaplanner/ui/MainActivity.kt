package cz.cvut.fel.kopecm26.bakaplanner.ui

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main, MainViewModel::class) {

    override fun initUi() {
        super.initUi()
        viewModel.init()
    }

}