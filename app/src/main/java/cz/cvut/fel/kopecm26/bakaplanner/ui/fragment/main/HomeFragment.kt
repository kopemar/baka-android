package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentHomeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDayTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HomeViewModel

class HomeFragment : ViewModelFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeViewModel::class
) {

    private val observer by lazy {
        Observer<List<Shift>> {
            binding.rvDateTime.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvDateTime.adapter = BaseListAdapter<Shift>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListDayTimeBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { shift, binding, index ->
                    run {
                        (binding as ListDayTimeBinding).shift = shift
                        binding.last = index == it.size - 1
                    }
                },
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.nextWeekDays.observe(viewLifecycleOwner, observer)
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshShifts() }

        binding.currentShift.setOnClickListener {
            viewModel.currentShift.value?.id?.let(::openShiftDetail)
        }

        binding.nextShift.setOnClickListener {
            viewModel.nextShift.value?.id?.let(::openShiftDetail)
        }
    }

    private fun openShiftDetail(shiftId: Int) {
        findNavController().navigate(ScheduleFragmentDirections.navigateToShiftDetail(shiftId))
    }
}