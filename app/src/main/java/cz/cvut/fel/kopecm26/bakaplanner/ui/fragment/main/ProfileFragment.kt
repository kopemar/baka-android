package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentProfileBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.DispatchActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchContractsStrategy
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
                startActivity<DispatchActivity>()
            }
        }
    }

    override fun initUi() {
        viewModel.signedOut.observe(this, signedOutObserver)

        binding.btnHistory.root.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToHistory())
        }

        binding.btnContracts.root.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToContracts(FetchContractsStrategy.General))
        }

        binding.btnSettings.root.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navigateToSettings())
        }
    }
}
