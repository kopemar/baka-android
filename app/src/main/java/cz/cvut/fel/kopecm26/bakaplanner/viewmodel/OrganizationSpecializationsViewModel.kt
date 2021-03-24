package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization

class OrganizationSpecializationsViewModel : BaseViewModel() {
    init {
        fetchOrganizationSpecializations()
    }

    private val _specializations = MutableLiveData<List<Specialization>>()
    val specializations: LiveData<List<Specialization>> = _specializations

    fun fetchOrganizationSpecializations() {
        working.work {
            specializationRepository.fetchSpecializations().parseResponse(_specializations)
        }
    }
}