package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchEmployeesStrategy
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SelectionList
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class SelectEmployeesViewModel : BaseViewModel() {
    private val employees = MutableLiveData<List<Employee>>()

    private val _employeeSelection = MediatorLiveData<SelectionList<Selection<Employee>>>().apply {
        addSource(employees) { mapEmployees(it) }
    }
    val employeeSelection: LiveData<SelectionList<Selection<Employee>>> = _employeeSelection

    private val _selectedCount = MutableLiveData<Int>()
    val selectedCount: LiveData<Int> = _selectedCount

    fun countSelected(): Int? = employeeSelection.value?.getAllSelectedCount()?.also { _selectedCount.value = it }

    private fun mapEmployees(
        employees: List<Employee>
    ) {
        val selections = SelectionList<Selection<Employee>>()
        employees.forEach { selections.add(Selection(it)) }
        _employeeSelection.value = selections
    }

    // TODO more strategies
    fun fetchEmployees(strategy: FetchEmployeesStrategy) {
        if (_employeeSelection.value != null) return
        working.work {
            userRepository.getCurrentUser()?.organization_id?.let {
                userRepository.getOrganizationEmployees(it).parseResponse(employees)
            }
        }
    }
}