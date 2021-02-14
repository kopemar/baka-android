package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.planning

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPeriodBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListSchedulingUnitBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.PlanWeekActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodViewModel

class PeriodFragment: ViewModelFragment<PeriodViewModel, FragmentPeriodBinding>(R.layout.fragment_period, PeriodViewModel::class) {
    override val toolbar: Toolbar get() = binding.planningToolbar.toolbar
    override var navigateUp = true
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val args by navArgs<PeriodFragmentArgs>()

    private val observer by lazy {
        Observer<List<SchedulingUnit>> {
            binding.rvUnits.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvUnits.adapter = BaseListAdapter<SchedulingUnit>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListSchedulingUnitBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { unit, binding, _ -> (binding as ListSchedulingUnitBinding).unit = unit },
                { unit -> findNavController().navigate(PeriodFragmentDirections.navigateToTemplatesFragment(unit)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.units.observe(viewLifecycleOwner, observer)
        viewModel.setPeriod(args.period)

        binding.emptyWeekMessage.btnPlanShift.setOnClickListener {
            startActivityForResult<PlanWeekActivity>(0)
        }
    }

}