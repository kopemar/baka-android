package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentAdjustShiftsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListAdjustTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDaysCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SelectionList
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AdjustShiftsViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AdjustShiftsFragment : ViewModelFragment<PlanDaysViewModel, FragmentAdjustShiftsBinding>(
    R.layout.fragment_adjust_shifts,
    PlanDaysViewModel::class
) {
    override val viewModelOwner get() = activity

    private val periodDaysViewModel: PeriodDaysViewModel by sharedViewModel()
    private val adjustViewModel: AdjustShiftsViewModel by sharedViewModel()

    private val shiftsAdapter by lazy {
        BaseListAdapter<Selection<ShiftTimeCalculation>>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListAdjustTimeBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { item, binding, _ -> (binding as ListAdjustTimeBinding).calculation = item },
            { item, _ -> selectCalc(item) },
            { old, new -> old.item.id == new.item.id },
            { old, new -> old == new }
        )
    }

    private val shiftsObserver by lazy {
        Observer<Map<PeriodDay, SelectionList<Selection<ShiftTimeCalculation>>>> {
            it[adjustViewModel.selectedDay?.item]?.let { items -> shiftsAdapter.setItems(items) }
        }
    }

    private val periodDaysObserver by lazy {
        Observer<List<Selection<PeriodDay>>> {
            binding.rvDays.adapter = BaseListAdapter<Selection<PeriodDay>>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListDaysCircleBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { day, binding, _ -> (binding as ListDaysCircleBinding).day = day },
                { day, _ -> selectDay(day) },
                { old, new -> old == new },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        adjustViewModel.daySelection.observe(this, periodDaysObserver)
        adjustViewModel.daysMap.observe(this, shiftsObserver)
        showShiftCalculations()
    }

    private fun selectDay(day: Selection<PeriodDay>) {
        adjustViewModel.daySelection.value?.select(day) { newIndex, originalIndex ->
            binding.rvDays.adapter?.apply {
                notifyItemChanged(newIndex)
                notifyItemChanged(originalIndex)
            }
        }
        showShiftCalculations()
    }

    // TODO animator
    private fun selectCalc(day: Selection<ShiftTimeCalculation>) {
        adjustViewModel.daysMap.value?.get(adjustViewModel.selectedDay?.item)?.apply {
            if (day.selected) unselect(day) {
                Logger.d("Unselect")
                binding.rvShifts.adapter?.notifyItemChanged(it)
            } else select(day) {
                Logger.d("Select")
                binding.rvShifts.adapter?.notifyItemChanged(it)
            }
        }
    }

    private fun showShiftCalculations() {
        binding.rvShifts.adapter = shiftsAdapter.apply {
            adjustViewModel.daysMap.value?.get(adjustViewModel.selectedDay?.item)?.let(::setItems)
        }
    }
}
