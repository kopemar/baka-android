package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

abstract class EmployeeListViewModel: BaseViewModel() {

    protected val _employees = MediatorLiveData<List<Employee>>()

    val employees: LiveData<List<Employee>> = _employees

    abstract fun fetchEmployees()
}
