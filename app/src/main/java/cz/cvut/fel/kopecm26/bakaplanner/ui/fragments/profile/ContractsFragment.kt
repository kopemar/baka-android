package cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.profile

import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityContractsBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragments.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractsFragment : ViewModelFragment<ContractsViewModel, ActivityContractsBinding>(
    R.layout.activity_contracts,
    ContractsViewModel::class
) {
    override fun initUi() {
        binding.toolbar.toolbar.run {
            setNavigationIcon(R.drawable.ic_mdi_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

    }
}