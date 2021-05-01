package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ShiftViewModel : BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    private val _removed = MutableLiveData<Boolean>()
    val removed: LiveData<Boolean> = _removed

    fun removeFromSchedule() {
        working.work {
            shift.value?.id?.let { shiftRepository.removeShiftFromSchedule(it) }?.parseResponse(_removed)
        }
    }
}
