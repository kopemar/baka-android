package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee

class OrganizationEmployeesViewModel : BaseViewModel() {

    private val _employees = MutableLiveData<List<Employee>>()
    val employees: LiveData<List<Employee>> = _employees

    fun fetchOrganizationEmployees(organizationId: Int) {
        working.work {
            userRepository.getOrganizationEmployees().parseResponse(_employees)
        }
    }
}
