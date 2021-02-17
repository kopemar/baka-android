package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import android.widget.AdapterView
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanDaysBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListBreakBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListHoursBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListIntBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array.BaseArrayAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values.Break
import cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values.ShiftHours
import cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values.ShiftsPerDay
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.onClickOrFocus
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.setupAdapter
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel

class PlanDaysFragment: ViewModelFragment<PlanDaysViewModel, FragmentPlanDaysBinding>(
    R.layout.fragment_plan_days,
    PlanDaysViewModel::class
) {
    override val viewModelOwner get() = activity

    private val breakSelected =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            Logger.d("onItemSelected")
            viewModel.breakMinutes.value = Break.breakMinutes[position]
        }

    private val hoursSelected =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            viewModel.shiftHours.value = ShiftHours.all[position]
        }

    private val shiftsCountSelected =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            viewModel.shiftsPerDay.value = ShiftsPerDay.all[position]
        }

    override fun initUi() {
        setupForm()
    }

    private fun setupForm() {
        setupTimePickers()
        setupBreakAdapter()
        setupShiftHoursAdapter()
        setupShiftPerDayAdapter()
    }

    private fun setupTimePickers() {
        binding.actvStartTime.onClickOrFocus {
            setUpTimePicker(viewModel.startTime)
        }

        binding.actvEndTime.onClickOrFocus {
            setUpTimePicker(viewModel.endTime)
        }
    }

    private fun setupBreakAdapter() {
        val adapter = BaseArrayAdapter<Int, ListBreakBinding>(
            requireContext(),
            R.layout.list_break,
            { item, binding, _ -> binding.minutes = item },
            Break.breakMinutes
        )

        binding.actvBreak.setupAdapter(
            adapter,
            breakSelected
        )
    }

    private fun setupShiftHoursAdapter() {
        val adapter = BaseArrayAdapter<Int, ListHoursBinding>(
            requireContext(),
            R.layout.list_hours,
            { item, binding, _ -> binding.hours = item },
            ShiftHours.all
        )

        binding.actvShiftHours.setupAdapter(
            adapter,
            hoursSelected
        )
    }

    private fun setupShiftPerDayAdapter() {
        val adapter = BaseArrayAdapter<Int, ListIntBinding>(
            requireContext(),
            R.layout.list_int,
            { item, binding, _ -> binding.number = item.toString() },
            ShiftsPerDay.all
        )

        binding.actvShiftsPerDay.setupAdapter(
            adapter,
            shiftsCountSelected
        )
    }

}