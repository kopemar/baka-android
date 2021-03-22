package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AddSpecializationViewModel : BaseViewModel() {

    val name = MutableLiveData<String>()

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun submit() {
        name.value?.let(::launchSubmit)
    }

    private fun launchSubmit(value: String) {
        working.work {
            if (value.isNotEmpty()) organizationRepository.createSpecialization(value).parseResponse(_success)
        }
    }
}