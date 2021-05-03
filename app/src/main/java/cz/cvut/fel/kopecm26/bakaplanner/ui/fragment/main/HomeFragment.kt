package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.vvalidator.util.hide
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentHomeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDayTimeBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeCircleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.HorizontalSpaceItemDecoration
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HomeViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : ViewModelFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home,
    HomeViewModel::class
) {

    override val viewModel: HomeViewModel by navGraphViewModels(R.id.home)

    private val sharedVM: SharedViewModel by sharedViewModel()

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
        Observer<EmployeeListResponse> {
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
            ).apply { setItems(it.data) }
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

        sharedVM.removeSuccess.observe(viewLifecycleOwner) {
            it.addConsumer(HOME_FRAGMENT_CONSUMER)
            if (it.canBeConsumed(HOME_FRAGMENT_CONSUMER) && it.consume(HOME_FRAGMENT_CONSUMER)) {
                showSnackBar(R.string.successfully_removed).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
                viewModel.refresh()
            }
        }

        sharedVM.signUpSuccess.observe(viewLifecycleOwner) {
            it.addConsumer(HOME_FRAGMENT_CONSUMER)
            if (it.canBeConsumed(HOME_FRAGMENT_CONSUMER) && it.consume(HOME_FRAGMENT_CONSUMER)) {
                showSnackBar(R.string.successfully_signed_up).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
                viewModel.refresh()
            }
        }
    }

    private fun openShiftDetail(shift: Shift) {
        findNavController().navigate(HomeFragmentDirections.navigateToShiftDetail(shift))
    }

    companion object {
        private const val HOME_FRAGMENT_CONSUMER = "HOME_FRAGMENT_CONSUMER"
    }
}
