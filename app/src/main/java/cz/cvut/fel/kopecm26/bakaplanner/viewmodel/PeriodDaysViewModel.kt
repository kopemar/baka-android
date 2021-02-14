package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

class PeriodDaysViewModel: BaseViewModel() {
    /* Period days setup */
    private val _period = MutableLiveData<SchedulingPeriod>()

    private val _periodDays = MutableLiveData<List<PeriodDay>>()
    val periodDays: LiveData<List<PeriodDay>> = _periodDays

    fun setPeriod(period: SchedulingPeriod?) {
        _period.value = period
        fetchDays()
    }

    fun fetchDays() {
        _period.value?.id?.let {
            working.work {
                planningRepository.getPeriodDays(it).parseResponse(_periodDays)
            }
        }
    }
}