package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanningBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListPeriodBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanningViewModel

class PlanningFragment: ViewModelFragment<PlanningViewModel, FragmentPlanningBinding>(R.layout.fragment_planning, PlanningViewModel::class) {
    private val observer by lazy {
        Observer<List<SchedulingPeriod>> {
            binding.rvPeriods.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvPeriods.adapter = BaseListAdapter<SchedulingPeriod>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListPeriodBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { period, binding, _ -> (binding as ListPeriodBinding).period = period },
                { period -> findNavController().navigate(PlanningFragmentDirections.navigateToPeriodFragment(period)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.periods.observe(viewLifecycleOwner, observer)
    }
}