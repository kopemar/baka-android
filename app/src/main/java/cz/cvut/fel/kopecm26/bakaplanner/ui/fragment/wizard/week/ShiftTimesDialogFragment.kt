package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogShiftTimesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.BaseBottomSheetDialogFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel

class ShiftTimesDialogFragment :
    BaseBottomSheetDialogFragment<PlanDaysViewModel, DialogShiftTimesBinding>(
        R.layout.dialog_shift_times,
        PlanDaysViewModel::class
    ) {
    override val viewModelOwner get() = activity

    private val observer = Observer<List<ShiftTimeCalculation>> {
        binding.rvShiftTimes.adapter = BaseListAdapter<ShiftTimeCalculation>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListShiftTimeBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { calculation, binding, _ -> (binding as ListShiftTimeBinding).shiftTime = calculation },
            null,
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        ).apply { setItems(it) }
    }

    override fun initUi() {
        viewModel.shiftTimeCalculations.observe(this, observer)
    }
}
