package cz.cvut.fel.kopecm26.bakaplanner.ui

import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityUrlBinding
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isUrl
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity

class UrlActivity : BindingActivity<ActivityUrlBinding>(R.layout.activity_url) {

    override val statusBarTransparent = true

    override fun initUi() {
        form {
            input(binding.etUrl.id) {
                assert(getString(R.string.must_be_url)) {
                    it.text.isUrl()
                }
            }

            submitWith(binding.btnSetUp.id) {
                Logger.d("Saving url to Prefs ${binding.etUrl.text}")
                Prefs.putString(Constants.Prefs.BASE_URL, binding.etUrl.text.toString())
                startActivity<LoginActivity>()
            }
        }
    }

}