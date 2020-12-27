package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Schedule
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class PickScheduleViewModel : BaseViewModel() {
    val shift = MutableLiveData<Shift>()
    val schedules = MutableLiveData<List<Schedule>>()
    val success = MutableLiveData<Boolean>()

    fun fetchSchedules(shiftId: Int) {
        working.work {
            scheduleRepository.getSchedulesForShift(shiftId).parseResponse(schedules)
        }
    }

    fun addToSchedule(schedule: Schedule) {
        working.work {
            shift.value?.id?.let { scheduleRepository.addShiftToSchedule(schedule.id, it) }
                ?.let(::parseResponse)
        }
    }

    private fun parseResponse(response: ResponseModel<Shift>) {
        if (response is ResponseModel.ERROR) response.errorType?.let(::parseError)
        else if (response is ResponseModel.SUCCESS) success.value = true
    }
}