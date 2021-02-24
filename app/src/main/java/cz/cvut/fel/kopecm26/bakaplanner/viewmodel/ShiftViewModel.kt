package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleLiveEvent

class ShiftViewModel : BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    private val _removed = SingleLiveEvent<Boolean>()
    val removed: LiveData<Boolean> = _removed

    fun removeFromSchedule() {
        working.work {
            shift.value?.id?.let { shiftRepository.removeShiftFromSchedule(it) }?.let(::handleResponse)
        }
    }

    private fun handleResponse(response: ResponseModel<Shift>) {
        if (response is ResponseModel.SUCCESS) _removed.value = true
        else if (response is ResponseModel.ERROR) response.errorType?.let(::parseError)
    }
}
