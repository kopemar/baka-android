package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.vvalidator.util.hide
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderSchedulingPeriodBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.WrappedAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ScheduleViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScheduleFragment : ViewModelFragment<ScheduleViewModel, FragmentScheduleBinding>(
    R.layout.fragment_schedule,
    ScheduleViewModel::class
) {

    private val sharedVM: SharedViewModel by sharedViewModel()

    private val removeObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(CONSUMER_TAG)
            if (it.canBeConsumed(CONSUMER_TAG) && it.consume(CONSUMER_TAG)) {
                viewModel.refreshShifts()

                showSnackBar(R.string.successfully_removed).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
            }
        }
    }

    private val signUpObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(CONSUMER_TAG)
            if (it.canBeConsumed(CONSUMER_TAG) && it.consume(CONSUMER_TAG)) {
                viewModel.refreshShifts()
            }
        }
    }

    private val observer by lazy {
        Observer<Map<SchedulingPeriod?, List<Shift>>> {
            val adapter = ConcatAdapter()
            it?.forEach { (key, value) ->
                val a = WrappedAdapter<SchedulingPeriod, Shift, Any>(
                    { layoutInflater, viewGroup, attachToRoot ->
                        ListShiftBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    { shift, binding, _ ->
                        (binding as ListShiftBinding).shift = shift
                    },
                    { shift, _ ->
                        if (key != null) {
                            findNavController().navigate(ScheduleFragmentDirections.navigateToShiftDetail(shift))
                        }
                    },
                    { old, new -> old.id == new.id },
                    { old, new -> old == new },
                    inflateHeader = { layoutInflater, viewGroup, attachToRoot ->
                        HeaderSchedulingPeriodBinding.inflate(
                            layoutInflater,
                            viewGroup,
                            attachToRoot
                        )
                    },
                    bindHeader = { state, binding, _ ->
                        (binding as HeaderSchedulingPeriodBinding).state = state
                    }
                ).apply {
                    this.header = key
                    setItems(value)
                }
                adapter.addAdapter(a)
            }
            binding.rvShifts.adapter = adapter
        }
    }

//    private val observer by lazy {
//        Observer<List<Shift>> {
//            binding.rvShifts.layoutManager = LinearLayoutManager(binding.root.context)
//            binding.rvShifts.adapter = BaseListAdapter<Shift>(
//                { layoutInflater, viewGroup, attachToRoot ->
//                    ListShiftBinding.inflate(
//                        layoutInflater,
//                        viewGroup,
//                        attachToRoot
//                    )
//                },
//                { shift, binding, _ -> (binding as ListShiftBinding).shift = shift },
//                { shift, _ ->
//                    findNavController().navigate(ScheduleFragmentDirections.navigateToShiftDetail(shift))
//                },
//                { old, new -> old.id == new.id },
//                { old, new -> old == new }
//            ).apply { setItems(it) }
//        }
//    }

    override fun initUi() {
        initObservers()
    }

    private fun initObservers() {
        viewModel.groups.observe(viewLifecycleOwner, observer)

        binding.rvShifts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.mainToolbar.appBarLayout.elevation =
                    if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
            }
        })

        sharedVM.signUpSuccess.observe(this, signUpObserver)
        sharedVM.removeSuccess.observe(this, removeObserver)
    }

    companion object {
        private const val CONSUMER_TAG = "ScheduleFragmentConsumer"
    }
}
