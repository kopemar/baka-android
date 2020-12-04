package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments

import androidx.lifecycle.viewModelScope
import com.jakewharton.processphoenix.ProcessPhoenix
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentProfileBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.ui.UrlActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : ViewModelFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile, ProfileViewModel::class) {

    override fun initUi() {
        binding.btnUrl.setOnClickListener { requireActivity().startActivity<UrlActivity>() }
        binding.btnLogOut.setOnClickListener { logout() }
    }

    fun logout() {
        viewModel.viewModelScope.launch {
            viewModel.signOut().run {
                if (this is ResponseModel.SUCCESS<SignOutModel>){
                    Logger.d(PrefsUtils.getPrefsStringOrNull(Constants.Prefs.USER))
                    Prefs.remove(Constants.Prefs.USER)
                } else if (this is ResponseModel.ERROR<SignOutModel>) {
                    this.errorType?.messageRes?.let { showSnackBar(it) }
                }

                delay(500)
                ProcessPhoenix.triggerRebirth(requireContext())
            }

        }
    }
}