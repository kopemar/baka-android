package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

class AutoScheduleViewModel: BaseViewModel() {

    private val _period = MutableLiveData<SchedulingPeriod>()
    val period: LiveData<SchedulingPeriod> = _period

    fun setPeriod(schedulingPeriod: SchedulingPeriod) {
        _period.value = schedulingPeriod
    }

    private val _scheduleState = MutableLiveData(false)
    val success: LiveData<Boolean> = _scheduleState

    fun callAutoSchedule() {
        _period.value?.id?.let(::callAutoScheduleWithId)
    }

    private fun callAutoScheduleWithId(periodId: Int) {
        working.work {
            planningRepository.callAutoScheduler(periodId).parseResponse(_scheduleState)
        }
    }
}
