package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentHomeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDayTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.HorizontalSpaceItemDecoration
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HomeViewModel

class HomeFragment : ViewModelFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeViewModel::class
) {

    override val viewModel: HomeViewModel by navGraphViewModels(R.id.home)

    private val shiftsObserver by lazy {
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
                { shift, binding, _ ->
                    run {
                        (binding as ListDayTimeBinding).shift = shift
                        binding.last = shift.ended
                    }
                },
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    private val employeeObserver by lazy {
        Observer<List<Employee>> {
            binding.rvEmployees.adapter = BaseListAdapter<Employee>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListEmployeeCircleBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { employee, binding, _ ->
                    run {
                        (binding as ListEmployeeCircleBinding).employee = employee
                    }
                },
                { _, _ -> findNavController().navigate(HomeFragmentDirections.showEmployeeDialog()) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
            binding.rvEmployees.addItemDecoration(HorizontalSpaceItemDecoration(16))
        }
    }

    override fun initUi() {
        viewModel.nextWeekDays.observe(viewLifecycleOwner, shiftsObserver)
        viewModel.currentEmployees.observe(viewLifecycleOwner, employeeObserver)

        binding.currentShift.setOnClickListener {
            viewModel.currentShift.value?.let(::openShiftDetail)
        }

        binding.nextShift.setOnClickListener {
            viewModel.nextShift.value?.let(::openShiftDetail)
        }
    }

    private fun openShiftDetail(shift: Shift) {
        findNavController().navigate(HomeFragmentDirections.navigateToShiftDetail(shift))
    }
}
