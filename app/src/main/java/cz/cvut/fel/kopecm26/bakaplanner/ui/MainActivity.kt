package cz.cvut.fel.kopecm26.bakaplanner.ui

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityMainBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main, MainViewModel::class) {

    override fun initUi() {
        super.initUi()
        viewModel.init()

        // todo learn about coroutines!
        GlobalScope.launch {
//            val user = viewModel.signIn("jannovak", "1235678")
//            Logger.d(user)
        }
    }

}