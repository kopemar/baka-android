package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization

class SpecializationViewModel: EmployeeListViewModel()  {
    private val _specialization = MutableLiveData<Specialization>()
    val specialization: LiveData<Specialization> = _specialization

    fun setSpecializationValue(specialization: Specialization) {
        _specialization.value = specialization

        fetchEmployees()
    }

    override fun fetchEmployees() {
        working.work {
            specialization.value?.id?.let {
                specializationRepository.getSpecializationEmployees(it).parseResponse(_employees)
            }
        }
    }
}
