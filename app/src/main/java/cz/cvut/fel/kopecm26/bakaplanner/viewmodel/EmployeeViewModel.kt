package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class EmployeeViewModel : BaseViewModel() {
    private val _employee = MutableLiveData<Employee>()
    val employee: LiveData<Employee> = _employee

    fun setEmployeeValue(employee: Employee) {
        _employee.value = employee
    }

    private val _shifts = MediatorLiveData<List<Shift>>().apply {
        addSource(_employee) {
            Logger.d(it)
            if (it != null) { fetchShifts() }
        }
    }

    val shifts: LiveData<List<Shift>> = _shifts

    fun fetchShifts() {
        working.work {
            employee.value?.id?.let { userRepository.getEmployeeShifts(it).parseResponse(_shifts) }
        }
    }
}
