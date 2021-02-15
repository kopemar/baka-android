package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentAdjustShiftsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDaysCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AdjustShiftsFragment : ViewModelFragment<PlanDaysViewModel, FragmentAdjustShiftsBinding>(
    R.layout.fragment_adjust_shifts,
    PlanDaysViewModel::class
) {
    override val viewModelOwner get() = activity

    private val periodDaysViewModel: PeriodDaysViewModel by sharedViewModel()

    private val periodDaysObserver = Observer<List<PeriodDay>> {
        binding.rvDays.adapter = BaseListAdapter<PeriodDay>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListDaysCircleBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { day, binding, _ -> (binding as ListDaysCircleBinding).day = day },
            { _, _ -> run {} },
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        ).apply { setItems(it) }
    }

    override fun initUi() {
        periodDaysViewModel.periodDays.observe(this, periodDaysObserver)
    }
}