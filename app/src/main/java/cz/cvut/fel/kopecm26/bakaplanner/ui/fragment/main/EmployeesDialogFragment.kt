package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.HorizontalSpaceItemDecoration
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HomeViewModel

class EmployeesDialogFragment: ViewModelBottomSheetFragment<HomeViewModel, DialogEmployeesBinding>(
    R.layout.dialog_employees,
    HomeViewModel::class
) {
    override val viewModel: HomeViewModel by navGraphViewModels(R.id.home)

    private val employeeObserver by lazy {
        Observer<List<Employee>> {
            binding.rvEmployees.adapter = BaseListAdapter<Employee>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListEmployeeBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { employee, binding, _ ->
                    run {
                        (binding as ListEmployeeBinding).employee = employee
                    }
                },
                { item, _ -> EmployeesDialogFragmentDirections.navigateToEmployeeDetail(item) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
            binding.rvEmployees.addItemDecoration(HorizontalSpaceItemDecoration(16))
        }
    }

    override fun initUi() {
        viewModel.currentEmployees.observe(viewLifecycleOwner, employeeObserver)
    }
}
