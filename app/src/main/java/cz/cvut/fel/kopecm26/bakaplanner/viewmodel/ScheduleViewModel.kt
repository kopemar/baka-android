package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()

    init {
        fetchShifts()
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshAllShifts().let(::saveShifts)
        }
    }

    fun fetchShifts() {
        working.work {
            shiftRepository.getUpcomingShifts().let(::saveShifts)
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        response.parseResponse(shifts)
    }

}