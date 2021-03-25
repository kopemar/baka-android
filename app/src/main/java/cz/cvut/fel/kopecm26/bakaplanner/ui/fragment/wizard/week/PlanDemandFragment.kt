package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanDemandBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDaysCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListTemplateDemandBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.WrappedAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDemandViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlanDemandFragment : ViewModelFragment<PlanDemandViewModel, FragmentPlanDemandBinding>(
    R.layout.fragment_plan_demand,
    PlanDemandViewModel::class
) {

    private val periodDaysViewModel: PeriodDaysViewModel by sharedViewModel()

    private val demandSuccessObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(PLAN_DEMAND_CONSUMER)
            if (it.canBeConsumed(PLAN_DEMAND_CONSUMER) && it.consume(PLAN_DEMAND_CONSUMER)) {
                viewModel.daySelection.value?.selected?.run {
                    viewModel.fetchShiftsInUnit(item.id)
                }
            }
        }
    }

    private val periodDaysObserver by lazy {
        Observer<List<Selection<PeriodDay>>> {
            binding.rvUnits.adapter = BaseListAdapter<Selection<PeriodDay>>(
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

    private val observer by lazy {
        Observer<Map<String, List<ShiftTemplate>>> {
            val concatAdapter = ConcatAdapter()
            it.forEach {
                concatAdapter.addAdapter(WrappedAdapter<String, ShiftTemplate, Nothing> (
                    { layoutInflater, viewGroup, attachToRoot ->
                        HeaderTimeBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    { text, binding, _ ->
                        (binding as HeaderTimeBinding).text = text
                    },
                    { layoutInflater, viewGroup, attachToRoot ->
                        ListTemplateDemandBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    { template, binding, _ ->
                        (binding as ListTemplateDemandBinding).template = template
                    },
                    { template, _ ->
                        findNavController().navigate(PlanDemandFragmentDirections.showDemandDialog(template))
                        Logger.d(template)
                    },
                    { old, new -> old.id == new.id },
                    { old, new -> old == new },
                    onHeaderClick = {
                        it.value.firstOrNull { value -> value.specialization.isNullOrEmpty() }?.let { template ->
                            findNavController().navigate(PlanDemandFragmentDirections.showSpecializations(template))
                        }
                    }
                ).apply {
                    header = it.key
                    setItems(it.value)
                })
            }
            binding.rvUnitShifts.adapter = concatAdapter
        }
    }

    override fun initUi() {
        periodDaysViewModel.period.value?.id?.let {
            viewModel.fetchUnits(it)
        }

        sharedViewModel.setDemandSuccess.observe(this, demandSuccessObserver)

        viewModel.daySelection.observe(this, periodDaysObserver)
        viewModel.units.observe(this, observer)
    }

    private fun selectDay(day: Selection<PeriodDay>) {
        viewModel.daySelection.value?.select(day) { newIndex, originalIndex ->
            binding.rvUnits.adapter?.apply {
                notifyItemChanged(newIndex)
                notifyItemChanged(originalIndex)
            }
            viewModel.fetchShiftsInUnit(day.item.id)
        }
    }

    companion object {
        const val PLAN_DEMAND_CONSUMER = "PLAN_DEMAND_CONSUMER"
    }
}
