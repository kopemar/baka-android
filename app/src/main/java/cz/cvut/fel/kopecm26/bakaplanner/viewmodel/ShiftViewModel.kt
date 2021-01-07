package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleLiveEvent

class ShiftViewModel : BaseViewModel() {
    val shift = MutableLiveData<Shift>()
    val removed = SingleLiveEvent<Boolean>()

    fun removeFromSchedule() {
        working.work {
            shift.value?.id?.let { shiftRepository.removeShiftFromSchedule(it) }?.let(::handleResponse)
        }
    }

    private fun handleResponse(response: ResponseModel<Shift>) {
        if (response is ResponseModel.SUCCESS) removed.value = true
        else if (response is ResponseModel.ERROR) response.errorType?.let(::parseError)
    }
}