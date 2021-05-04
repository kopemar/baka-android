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

    private val _iterations = MutableLiveData(2)
    val iterations: LiveData<Int> = _iterations
    fun setIterations(value: Int) {
        _iterations.value = value
    }

    private val _noEmptyShifts = MutableLiveData(80)
    val noEmptyShifts: LiveData<Int> = _noEmptyShifts
    fun setNoEmptyShifts(value: Int) {
        _noEmptyShifts.value = value
    }

    private val _demandFulfill = MutableLiveData(100)
    val demandFulfill: LiveData<Int> = _demandFulfill
    fun setDemandFulfill(value: Int) {
        _demandFulfill.value = value
    }

    private val _specialized = MutableLiveData(50)
    val specialized: LiveData<Int> = _specialized
    fun setSpecialized(value: Int) {
        _specialized.value = value
    }

    private val _freeDays = MutableLiveData(200)
    val freeDays: LiveData<Int> = _freeDays
    fun setFreeDays(value: Int) {
        _freeDays.value = value
    }

    private val _scheduleState = MutableLiveData(false)
    val success: LiveData<Boolean> = _scheduleState

    fun callAutoSchedule() {
        _period.value?.id?.let(::callAutoScheduleWithId)
    }

    /*
        iterations: Int = 2,
        noEmpty: Int = 80,
        demandFulfill: Int = 100,
        specializedPreferred: Int = 60,
        freeDays: Int = 30
     */
    private fun callAutoScheduleWithId(periodId: Int) {
        working.work {
            planningRepository.callAutoScheduler(
                periodId,
                _iterations.value ?: 2,
                _noEmptyShifts.value ?: 100,
                _demandFulfill.value ?: 80,
                _specialized.value ?: 50,
                _freeDays.value ?: 100,
            ).parseResponse(_scheduleState)
        }
    }
}
