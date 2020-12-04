package cz.cvut.fel.kopecm26.bakaplanner.ui

import android.view.View
import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityUrlBinding
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isUrl
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity

class UrlActivity : BindingActivity<ActivityUrlBinding>(R.layout.activity_url) {

    override val statusBarTransparent = true

    override fun initUi() {
        binding.root.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) hideKeyboard()
        }
        binding.defaultUrl = PrefsUtils.getPrefsStringOrNull(Constants.Prefs.BASE_URL)

        form {
            input(binding.etUrl.id) {
                assert(getString(R.string.must_be_url)) {
                    it.text.isUrl()
                }
            }

            submitWith(binding.btnSetUp.id) {
                hideKeyboard()
                Logger.d("Saving url to Prefs ${binding.etUrl.text}")
                Prefs.putString(Constants.Prefs.BASE_URL, binding.etUrl.text.toString())

                PrefsUtils.getPrefsStringOrNull(Constants.Prefs.USER)?.let {
                    finish()
                } ?: run {
                    startActivity<LoginActivity>()
                }
            }
        }
    }

}