package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

class PlanningViewModel: BaseViewModel() {
    val periods = MutableLiveData<List<SchedulingPeriod>>()

    init {
        fetchSchedulingPeriods()
    }

    fun fetchSchedulingPeriods() {
        working.work {
            schedulingPeriodRepository.getSchedulingPeriods().parseResponse(periods)
        }
    }
}