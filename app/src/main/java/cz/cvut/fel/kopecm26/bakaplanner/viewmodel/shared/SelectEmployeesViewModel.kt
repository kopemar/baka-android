package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeePresenter
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchEmployeesStrategy
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SelectionList
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class SelectEmployeesViewModel : BaseViewModel() {
    private val _presenters = MutableLiveData<List<EmployeePresenter>>()

    private val _employeeSelection =
        MediatorLiveData<SelectionList<Selection<EmployeePresenter>>>().apply {
            addSource(_presenters) { mapEmployeePresenters(it) }
        }
    val employeeSelection: LiveData<SelectionList<Selection<EmployeePresenter>>> =
        _employeeSelection

    private val _selectedCount = MutableLiveData<Int>()
    val selectedCount: LiveData<Int> = _selectedCount

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun countSelected(): Int? =
        employeeSelection.value?.getAllSelectedCount()?.also { _selectedCount.value = it }

    fun submit(strategy: FetchEmployeesStrategy) {
        working.work {
            submitFromStrategy(strategy)
        }
    }

    fun fetchEmployees(strategy: FetchEmployeesStrategy) {
        if (_employeeSelection.value != null) return
        working.work {
            fetchFromStrategy(strategy)
        }
    }

    private fun mapEmployeePresenters(
        employees: List<EmployeePresenter>
    ) {
        val selections = SelectionList<Selection<EmployeePresenter>>()
        employees.forEach { selections.add(Selection(it)) }
        _employeeSelection.value = selections
    }

    private suspend fun fetchFromStrategy(strategy: FetchEmployeesStrategy) {
        when (strategy) {
            is FetchEmployeesStrategy.Specialization -> {
                specializationRepository.getSpecializationEmployeesPossibilities(strategy.id)
                    .parseResponse(_presenters)
            }
            is FetchEmployeesStrategy.Template -> {
                scheduleRepository.getSchedulesForShift(strategy.id).parseResponse { response ->
                    _presenters.value =
                        response?.map { EmployeePresenter(it.id, it.first_name ?: "", it.last_name ?: "") }
                }
            }
            else -> {
                userRepository.getOrganizationEmployees().parseResponse { response ->
                    _presenters.value =
                        response?.data?.map { EmployeePresenter(it.id, it.first_name, it.last_name) }
                }
            }
        }
    }

    private suspend fun submitFromStrategy(strategy: FetchEmployeesStrategy) {
        val selected = _employeeSelection.value?.getAllSelected()?.map { it.item.id } ?: listOf()
        when (strategy) {
            is FetchEmployeesStrategy.Specialization -> {
                specializationRepository.putSpecializationEmployees(strategy.id, selected)
                    .parseResponse(_success)
            }
            is FetchEmployeesStrategy.Template -> {
                scheduleRepository.addShiftToSchedules(strategy.id, selected).parseResponse {
                    _success.value = it != null
                }
            }
            else -> {
                // TODO
            }
        }
    }
}
