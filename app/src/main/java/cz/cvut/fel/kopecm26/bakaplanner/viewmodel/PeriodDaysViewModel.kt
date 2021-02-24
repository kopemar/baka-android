package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod

class PeriodDaysViewModel : BaseViewModel() {
    /* Period days setup */
    private val _period = MutableLiveData<SchedulingPeriod>()
    val period: LiveData<SchedulingPeriod> = _period

    private val _periodDays = MutableLiveData<List<PeriodDay>>()
    val periodDays: LiveData<List<PeriodDay>> = _periodDays

    fun setPeriod(period: SchedulingPeriod?) {
        _period.value = period
        fetchDays()
    }

    fun mapWorkingDays(): String? =
        periodDays.value?.filter { it.checked }?.joinToString(", ") { it.dayOfWeek }

    fun mapWorkingDayIdsToList() = periodDays.value?.filter { it.checked }?.map { it.id } ?: listOf()

    fun fetchDays() {
        _period.value?.id?.let {
            working.work {
                planningRepository.getPeriodDays(it).parseResponse(_periodDays)
            }
        }
    }
}
