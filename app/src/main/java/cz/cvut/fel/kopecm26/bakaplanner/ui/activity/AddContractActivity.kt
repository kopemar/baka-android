package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddContractViewModel

class AddContractActivity: ViewModelActivity<AddContractViewModel, ActivityAddContractBinding>(
    R.layout.activity_add_contract,
    AddContractViewModel::class
) {

}
