package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftTemplateEmployeesBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.PagingAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.binding.onRefresh
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeListViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ShiftEmployeesFragment<VM : EmployeeListViewModel>(
    clazz: KClass<VM>,
    private val viewModelStoreOwner: ViewModelStoreOwner? = null
) : ViewModelFragment<VM, FragmentShiftTemplateEmployeesBinding>(
    R.layout.fragment_shift_template_employees,
    clazz
) {
    override val viewModelOwner: ViewModelStoreOwner? get() = viewModelStoreOwner ?: activity

    var itemLongPressListener: ((employee: Employee) -> Unit)? = null

    private var job: Job? = null

    private val adapter by lazy {
        PagingAdapter<Employee>(
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
        )
    }

    override fun initUi() {
        binding.rvEmployees.adapter = adapter

        binding.swipeRefresh.onRefresh { refresh() }

        collectLatest()
    }

    private fun collectLatest() {
        job?.cancel()

        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { adapter.submitData(it) }
        }
    }

    fun refresh() {
        adapter.refresh()
    }
}
