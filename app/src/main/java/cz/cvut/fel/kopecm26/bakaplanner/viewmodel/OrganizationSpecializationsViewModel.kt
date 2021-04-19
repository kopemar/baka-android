package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchSpecializationStrategy

class OrganizationSpecializationsViewModel : BaseViewModel() {

    private val _specializations = MutableLiveData<List<Specialization>>()

    val specializations: LiveData<List<Specialization>> = _specializations

    private val _strategy = MutableLiveData<FetchSpecializationStrategy>()
    val strategy: LiveData<FetchSpecializationStrategy> = _strategy

    fun setStrategy(type: FetchSpecializationStrategy) {
        _strategy.value = type
        fetchOrganizationSpecializations(type)
    }

    fun refreshSpecializations() {
        _strategy.value?.let(::fetchOrganizationSpecializations)
    }

    private fun fetchOrganizationSpecializations(type: FetchSpecializationStrategy) {
        working.work {
            when (type) {
                is FetchSpecializationStrategy.Employee -> specializationRepository.getEmployeeSpecializations(type.id)
                else -> specializationRepository.getSpecializations()
            }.parseResponse(_specializations)
        }
    }
}