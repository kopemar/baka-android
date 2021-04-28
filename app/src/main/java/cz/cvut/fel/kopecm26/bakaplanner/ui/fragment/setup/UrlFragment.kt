package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import android.view.View
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentUrlBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.BindingFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isUrl

class UrlFragment : BindingFragment<FragmentUrlBinding>(R.layout.fragment_url) {

    override fun initUi() {
        binding.root.onFocusChangeListener = View.OnFocusChangeListener { _, b ->
            if (!b) activity?.hideKeyboard()
        }
        binding.defaultUrl = PrefsUtils.getPrefsStringOrNull(Constants.Prefs.BASE_URL)

        form {
            input(binding.urlEt.id) {
                assert(getString(R.string.must_be_url)) {
                    it.text.isUrl()
                }
            }

            submitWith(binding.btnSetUp.id) {
                activity?.hideKeyboard()
                Logger.d("Saving url to Prefs ${binding.urlEt.text}")
                PrefsUtils.putString(Constants.Prefs.BASE_URL, binding.urlEt.text.toString())
                Logger.d("URL set up")

                PrefsUtils.getPrefsStringOrNull(Constants.Prefs.USER)?.let {
                    activity?.finishAffinity()
                    findNavController().navigate(UrlFragmentDirections.openMainActivity())
                } ?: run {
                    findNavController().navigateUp()
                }
            }
        }
    }
}
