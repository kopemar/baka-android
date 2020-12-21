package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentProfileBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.SplashActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ProfileViewModel

class ProfileFragment : ViewModelFragment<ProfileViewModel, FragmentProfileBinding>(
    R.layout.fragment_profile,
    ProfileViewModel::class
) {

    private val signedOutObserver = Observer<Boolean> {
        if (it) {
            requireActivity().run {
                finishAffinity()
                startActivity<SplashActivity>()
            }
        }
    }

    private val errorMessageObserver = Observer<Int> {
        showSnackBar(it)
    }

    override fun initUi() {
        viewModel.signedOut.observe(this, signedOutObserver)

        binding.btnUrl.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToUrl())
        }

        binding.btnHistory.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToHistory())
        }

        binding.btnContracts.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToContracts())
        }

        binding.btnLogOut.setOnClickListener { viewModel.signOut() }
    }

}