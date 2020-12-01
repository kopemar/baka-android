package cz.cvut.fel.kopecm26.bakaplanner.ui

import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityUrlBinding
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isUrl

class UrlActivity : BindingActivity<ActivityUrlBinding>(R.layout.activity_url) {

    override fun initUi() {
        form {
            input(binding.etUrl.id) {
                assert(getString(R.string.must_be_url)) {
                    it.editableText.isUrl()
                }
            }

            submitWith(binding.btnSetUp.id) {
                Logger.d("submit")
            }
        }
    }

}