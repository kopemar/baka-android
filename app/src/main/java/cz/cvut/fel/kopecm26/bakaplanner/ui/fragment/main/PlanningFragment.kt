package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanningBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderPeriodsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListPeriodBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodState
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.WrappedAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanningViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlanningFragment : ViewModelFragment<PlanningViewModel, FragmentPlanningBinding>(
    R.layout.fragment_planning,
    PlanningViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val sharedVM: SharedViewModel by sharedViewModel()

    private val observer by lazy {
        Observer<Map<PeriodState, List<SchedulingPeriod>>> {
            val adapter = ConcatAdapter()
            it.forEach { (key, u) ->
                val wrapped = WrappedAdapter<PeriodState, SchedulingPeriod, Nothing>(
                    { layoutInflater, viewGroup, attachToRoot ->
                        ListPeriodBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    { period, binding, _ -> (binding as ListPeriodBinding).period = period },
                    { period, _ ->
                        findNavController().navigate(
                            PlanningFragmentDirections.navigateToPeriodFragment(
                                period
                            )
                        )
                    },
                    { old, new -> old.id == new.id },
                    { old, new -> old == new },
                    inflateHeader = { layoutInflater, viewGroup, attachToRoot ->
                        HeaderPeriodsBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    bindHeader = { state, binding, _ ->
                        (binding as HeaderPeriodsBinding).state = state
                    }
                ).apply {
                    header = key
                    setItems(u)
                }

                adapter.addAdapter(wrapped)
            }
            binding.rvPeriods.adapter = adapter
        }
    }

    private val submitConsumer by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(SUBMIT_CONSUMER)
            if (it.canBeConsumed(SUBMIT_CONSUMER) && it.consume(SUBMIT_CONSUMER)) {
                viewModel.fetchSchedulingPeriods()
            }
        }
    }

    override fun initUi() {
        sharedVM.periodChanged.observe(viewLifecycleOwner, submitConsumer)
        viewModel.periodsMap.observe(viewLifecycleOwner, observer)
    }

    companion object {
        private const val SUBMIT_CONSUMER = "SUBMIT_CONSUMER"
    }
}
