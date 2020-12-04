package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments

import androidx.lifecycle.viewModelScope
import com.jakewharton.processphoenix.ProcessPhoenix
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentProfileBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.UrlActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileFragment : ViewModelFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile, ProfileViewModel::class) {

    override fun initUi() {
        binding.btnUrl.setOnClickListener { requireActivity().startActivity<UrlActivity>() }
        binding.btnLogOut.setOnClickListener { logout() }
    }

    fun logout() {
        viewModel.viewModelScope.launch {
            if (viewModel.signOut()) {
                ProcessPhoenix.triggerRebirth(requireContext())
            }
        }
    }
}