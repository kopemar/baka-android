package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import java.time.ZonedDateTime

class HistoryViewModel : BaseViewModel() {

    init {
        fetchShifts()
    }

    private val _shifts = MutableLiveData<List<Shift>>()
    val shifts: LiveData<List<Shift>> = _shifts

    private fun fetchShifts() = working.work {
        shiftRepository.getShiftsBefore(ZonedDateTime.now()).run { parseResponse(_shifts) }
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).run { parseResponse(_shifts) }
        }
    }

}
