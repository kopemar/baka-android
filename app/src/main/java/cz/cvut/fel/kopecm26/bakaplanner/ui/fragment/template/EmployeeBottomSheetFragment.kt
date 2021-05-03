package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogEmployeeDetailBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodState
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.binding.visible
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeDialogViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EmployeeBottomSheetFragment :
    ViewModelBottomSheetFragment<EmployeeDialogViewModel, DialogEmployeeDetailBinding>(
        R.layout.dialog_employee_detail,
        EmployeeDialogViewModel::class
    ) {

    private val args by navArgs<EmployeeBottomSheetFragmentArgs>()
    private val sharedVM: SharedViewModel by sharedViewModel()

    override fun initUi() {
        Logger.d(args.employee)
        viewModel.setEmployeeValue(args.employee)

        viewModel.removalSuccess.observe(viewLifecycleOwner) {
            if (it) {
                sharedVM.removeSuccess.value = Consumable(true)
                findNavController().navigateUp()
            }
        }

        binding.showProfile.root.setOnClickListener {
            findNavController().navigate(EmployeeBottomSheetFragmentDirections.navigateToEmployeeDetail(args.employee))
        }

        if (args.period.state == PeriodState.SUBMITTED) {
            binding.removeFromShift.root.visible(false)
        }
        binding.removeFromShift.root.setOnClickListener {
            viewModel.removeFromShift()
        }
    }
}
