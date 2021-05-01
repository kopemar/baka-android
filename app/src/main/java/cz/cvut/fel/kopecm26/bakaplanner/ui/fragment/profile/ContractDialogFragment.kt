package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractDialogFragment :
    ViewModelBottomSheetFragment<ContractsViewModel, DialogContractBinding>(
        R.layout.dialog_contract,
        ContractsViewModel::class
    ) {


    override fun initUi() {
    }
}
