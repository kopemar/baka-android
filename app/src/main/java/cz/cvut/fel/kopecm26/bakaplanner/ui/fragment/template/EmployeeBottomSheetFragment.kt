package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogEmployeeDetailBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeDialogViewModel

class EmployeeBottomSheetFragment :
    ViewModelBottomSheetFragment<EmployeeDialogViewModel, DialogEmployeeDetailBinding>(
        R.layout.dialog_employee_detail,
        EmployeeDialogViewModel::class
    ) {

    private val args by navArgs<EmployeeBottomSheetFragmentArgs>()

    override fun initUi() {
        Logger.d(args.employee)
        viewModel.setEmployeeValue(args.employee)
    }

}
