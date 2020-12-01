package cz.cvut.fel.kopecm26.bakaplanner.ui

import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityUrlBinding
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isUrl

class UrlActivity : BindingActivity<ActivityUrlBinding>(R.layout.activity_url) {

    override fun initUi() {
        form {
            input(R.id.et_url) {
                assert(getString(R.string.must_be_url)) {
                    it.editableText.isUrl()
                }
            }

            submitWith(R.id.btn_set_up) {
                Logger.d("submit")
            }
        }
    }

//    fun setUp(button: View) {
//
//    }
}