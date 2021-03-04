package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

class EmployeeDialogViewModel : BaseViewModel() {

    private val _employee = MutableLiveData<Employee>()
    val employee: LiveData<Employee> = _employee

    fun setEmployeeValue(employee: Employee) {
        _employee.value = employee
    }
}
