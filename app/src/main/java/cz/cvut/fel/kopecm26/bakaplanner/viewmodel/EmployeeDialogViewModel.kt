package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

class EmployeeDialogViewModel : BaseViewModel() {

    private val _removalSuccess = MutableLiveData<Boolean>()
    val removalSuccess: LiveData<Boolean> = _removalSuccess

    private val _employee = MutableLiveData<Employee>()
    val employee: LiveData<Employee> = _employee

    fun setEmployeeValue(employee: Employee) {
        _employee.value = employee
    }

    fun removeFromShift() {
        working.work {
            _employee.value?.shift_id?.let {
                shiftRepository.removeShiftFromSchedule(it).parseResponse(_removalSuccess)
            }
        }
    }
}
