package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationNameBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.NewOrganizationViewModel

class OrganizationNameFragment: ViewModelFragment<NewOrganizationViewModel, FragmentOrganizationNameBinding>(
    R.layout.fragment_organization_name, NewOrganizationViewModel::class
) {
    override val viewModel: NewOrganizationViewModel by navGraphViewModels(R.id.new_organization)

    override fun initUi() {
        binding.clRoot.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                requireActivity().hideKeyboard()
            }
        }

        setupForm()
    }

    private fun setupForm() {
        form {
            inputLayout(binding.inputOrganizationName) {
                isNotEmpty()
            }
            
            submitWith(binding.btnNext) {
                findNavController().navigate(OrganizationNameFragmentDirections.navigateToOrganizationSetup())
            }
        }
    }

}
