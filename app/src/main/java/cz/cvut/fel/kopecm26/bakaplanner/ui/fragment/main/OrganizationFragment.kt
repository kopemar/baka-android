package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchSpecializationStrategy
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.OrganizationEmployeesViewModel

class OrganizationFragment :
    ViewModelFragment<OrganizationEmployeesViewModel, FragmentOrganizationBinding>(R.layout.fragment_organization, OrganizationEmployeesViewModel::class) {

    override fun initUi() {
        binding.btnAllEmployees.setOnClickListener {
            findNavController().navigate(OrganizationFragmentDirections.navigateToEmployees())
        }

        binding.specializations.root.setOnClickListener {
            findNavController().navigate(OrganizationFragmentDirections.navigateToSpecializations(FetchSpecializationStrategy.General))
        }
    }
}
