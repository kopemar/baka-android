package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodState
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import java.time.ZonedDateTime

class PlanningViewModel : BaseViewModel() {

    init {
        fetchSchedulingPeriods()
    }

    private val _periods = MutableLiveData<List<SchedulingPeriod>>()
    val periods: LiveData<List<SchedulingPeriod>> = _periods

    private val _periodsMap = MediatorLiveData<Map<PeriodState, List<SchedulingPeriod>>>().apply {
        addSource(_periods) { value = it.groupBy { period -> period.state } }
    }
    val periodsMap: LiveData<Map<PeriodState, List<SchedulingPeriod>>> = _periodsMap

    fun fetchSchedulingPeriods() {
        working.work {
            planningRepository.getSchedulingPeriods(ZonedDateTime.now()).parseResponse(_periods)
        }
    }

}
