package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import androidx.navigation.navGraphViewModels
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationSetupBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.NewOrganizationViewModel

class OrganizationSetupFragment : ViewModelFragment<NewOrganizationViewModel, FragmentOrganizationSetupBinding>(
    R.layout.fragment_organization_setup, NewOrganizationViewModel::class
) {
    override val viewModel: NewOrganizationViewModel by navGraphViewModels(R.id.new_organization)

    override fun initUi() {
        binding.clRoot.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                requireActivity().hideKeyboard()
            }
        }
    }
}
