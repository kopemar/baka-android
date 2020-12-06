package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.main

import androidx.lifecycle.viewModelScope
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentProfileBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SignOutModel
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.SplashActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.UrlActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
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
                    Prefs.remove(Constants.Prefs.USER)
                    delay(500)

                    requireActivity().run {
                        finishAffinity()
                        startActivity<SplashActivity>()
                    }

                } else if (this is ResponseModel.ERROR<SignOutModel>) {
                    this.errorType?.messageRes?.let { showSnackBar(it) }
                }

            }

        }
    }
}