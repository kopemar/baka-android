package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Priority

class DemandDialogViewModel: BaseViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun updateDemand(templateId: Int, priority: Priority) {
        working.work {
            specializationRepository.updateDemand(templateId, priority).parseResponse(_success)
        }
    }
}