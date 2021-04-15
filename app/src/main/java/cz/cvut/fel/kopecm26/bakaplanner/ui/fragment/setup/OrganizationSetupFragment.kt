package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationSetupBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.NewOrganizationViewModel

class OrganizationSetupFragment :
    ViewModelFragment<NewOrganizationViewModel, FragmentOrganizationSetupBinding>(
        R.layout.fragment_organization_setup, NewOrganizationViewModel::class
    ) {
    override val viewModel: NewOrganizationViewModel by navGraphViewModels(R.id.new_organization)

    override val onNavigateUp = {
        if (viewModel.working.value != true) {
            super.onNavigateUp()
        }
    }

    private val observer by lazy {
        Observer<CreateOrganizationResponse> {
            if (it.success) {
                findNavController().popBackStack(R.id.loginFragment, false)
            }
        }
    }

    override fun initUi() {
        viewModel.working.observe(viewLifecycleOwner) {
            if (it) requireActivity().hideKeyboard()
        }

        viewModel.response.observe(viewLifecycleOwner, observer)

        binding.clRoot.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                requireActivity().hideKeyboard()
            }
        }
    }
}
