package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ScheduleViewModel : BaseViewModel() {

    private val _shifts = MutableLiveData<List<Shift>>()
    val shifts: LiveData<List<Shift>> = _shifts

    init {
        refreshShifts()
    }

    fun fetchShiftsRemote() {
        working.work {
            shiftRepository.refreshAllShifts().let(::saveShifts)
        }
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.getUpcomingShifts().let(::saveShifts)
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        response.parseResponse(_shifts)
    }

}