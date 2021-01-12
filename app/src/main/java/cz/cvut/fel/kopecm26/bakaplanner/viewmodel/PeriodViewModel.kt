package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit

class PeriodViewModel : BaseViewModel() {
    private val _period = MutableLiveData<SchedulingPeriod>()
    val period: LiveData<SchedulingPeriod> = _period

    private val _units = MediatorLiveData<List<SchedulingUnit>>().apply {
        addSource(_period) { fetchSchedulingUnits() }
    }
    val units: LiveData<List<SchedulingUnit>> = _units

    fun setPeriod(period: SchedulingPeriod) {
        if (_period.value != period) _period.value = period
    }

    fun fetchSchedulingUnits() {
        working.work {
            _period.value?.id?.let {
                planningRepository.getSchedulingUnits(it).parseResponse(_units)
            }
        }
    }
}