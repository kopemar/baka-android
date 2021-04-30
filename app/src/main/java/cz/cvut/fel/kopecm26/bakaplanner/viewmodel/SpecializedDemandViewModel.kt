package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization

class SpecializedDemandViewModel : BaseViewModel() {
    private val _template = MutableLiveData<ShiftTemplate>()

    private val _specializations = MediatorLiveData<List<Specialization>>().apply {
        addSource(_template) { fetchSpecializations() }
    }

    val specializations: LiveData<List<Specialization>> = _specializations

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun setTemplate(template: ShiftTemplate) {
        _template.value = template
    }

    fun createSpecializedShift(specialization: Specialization) {
        working.work {
            _template.value?.id?.let { id ->
                specializationRepository.createSpecializedShift(id, specialization).parseResponse {
                    _success.value = it != null
                }
            }
        }
    }

    private fun fetchSpecializations() {
        working.work {
            _template.value?.let {
                specializationRepository.getSpecializations(it)
                    .parseResponse(_specializations)
            }
        }
    }
}
