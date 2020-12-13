package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityContractsBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractsActivity: ViewModelActivity<ContractsViewModel, ActivityContractsBinding>(R.layout.activity_contracts, ContractsViewModel::class) {
    override fun initUi() {
        setUpToolbar(binding.toolbar.toolbar)
    }
}