package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

class PlanningViewModel : BaseViewModel() {

    init {
        fetchSchedulingPeriods()
    }

    private val _periods = MutableLiveData<List<SchedulingPeriod>>()
    val periods: LiveData<List<SchedulingPeriod>> = _periods

    fun fetchSchedulingPeriods() {
        working.work {
            planningRepository.getSchedulingPeriods().parseResponse(_periods)
        }
    }
}
