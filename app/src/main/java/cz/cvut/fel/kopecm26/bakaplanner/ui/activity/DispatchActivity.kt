package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivitySplashBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.BindingActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity

class DispatchActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override val statusBarTransparent = true

    override fun initUi() {
        PrefsUtils.getPrefsStringOrNull(Constants.Prefs.USER)
            ?.let { startActivity<MainActivity>() } ?: startActivity<SetupActivity>()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
