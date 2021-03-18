package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AutoScheduleViewModel: BaseViewModel() {

    private val _period = MutableLiveData<SchedulingPeriod>()
    val period: LiveData<SchedulingPeriod> = _period

    fun setPeriod(schedulingPeriod: SchedulingPeriod) {
        _period.value = schedulingPeriod
    }

    private val _scheduleState = MutableStateFlow<ResponseModel<Boolean>?>(null)
    val scheduleState: StateFlow<ResponseModel<Boolean>?> = _scheduleState

    fun callAutoSchedule() {
        _period.value?.id?.let(::callAutoScheduleWithId)
    }

    private fun callAutoScheduleWithId(periodId: Int) {
        working.work {
            planningRepository.callAutoScheduler(periodId).parseResponse(_scheduleState)
        }
    }
}
