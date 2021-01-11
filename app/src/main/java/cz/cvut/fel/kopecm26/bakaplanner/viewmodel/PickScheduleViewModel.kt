package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Schedule
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class PickScheduleViewModel : BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    private val _schedules = MutableLiveData<List<Schedule>>()
    val schedules: LiveData<List<Schedule>> = _schedules

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun fetchSchedules(shiftId: Int) {
        working.work {
            scheduleRepository.getSchedulesForShift(shiftId).parseResponse(_schedules)
        }
    }

    fun addToSchedule(schedule: Schedule) {
        working.work {
            shift.value?.id?.let { shiftRepository.addShiftToSchedule(it, schedule.id) }
                ?.let(::parseResponse)
        }
    }

    private fun parseResponse(response: ResponseModel<Shift>) {
        if (response is ResponseModel.ERROR) response.errorType?.let(::parseError)
        else if (response is ResponseModel.SUCCESS) _success.value = true
    }
}