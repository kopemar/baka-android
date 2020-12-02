package cz.cvut.fel.kopecm26.bakaplanner.ui

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivitySplashBinding
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override val statusBarTransparent = true

    override fun initUi() {
        GlobalScope.launch {
            delay(1500)
            finish()
            PrefsUtils.getPrefsStringOrNull(Constants.Prefs.USER)
                ?.let { startActivity<MainActivity>() } ?: startActivity<UrlActivity>()
        }
    }
}