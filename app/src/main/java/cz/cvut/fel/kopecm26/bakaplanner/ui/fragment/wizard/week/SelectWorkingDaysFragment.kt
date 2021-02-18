package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentSelectWorkingDaysBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListPeriodDayBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel

class SelectWorkingDaysFragment :
    ViewModelFragment<PeriodDaysViewModel, FragmentSelectWorkingDaysBinding>(
        R.layout.fragment_select_working_days,
        PeriodDaysViewModel::class
    ) {
    override val viewModelOwner get() = activity

    private val observer = Observer<List<PeriodDay>> {
        binding.rvPeriodDays.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvPeriodDays.adapter = BaseListAdapter<PeriodDay>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListPeriodDayBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { periodDay, binding, _ -> (binding as ListPeriodDayBinding).day = periodDay },
            { periodDay, binding ->
                periodDay.checked = !periodDay.checked
                binding.invalidateAll()
            },
            { old, new -> old.id == new.id },
            { old, new -> old == new }
        ).apply { setItems(it) }
    }

    override fun initUi() {
        viewModel.periodDays.observe(viewLifecycleOwner, observer)
    }
}