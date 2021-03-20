package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftTemplateEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftTemplateViewModel

class ShiftEmployeesFragment :
    ViewModelFragment<ShiftTemplateViewModel, FragmentShiftTemplateEmployeesBinding>(
        R.layout.fragment_shift_template_employees,
        ShiftTemplateViewModel::class
    ) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    var itemLongPressListener: ((employee: Employee) -> Unit)? = null

    private val observer by lazy {
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
                    (binding as ListEmployeeBinding).employee = employee
                },
                { employee, _ ->
                    itemLongPressListener?.invoke(employee)
                },
                { old, new -> old.id == new.id },
                { old, new -> old == new },
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.employees.observe(viewLifecycleOwner, observer)
    }
}
