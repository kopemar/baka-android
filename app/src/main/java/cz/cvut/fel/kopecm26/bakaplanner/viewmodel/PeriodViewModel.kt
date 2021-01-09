package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit

class PeriodViewModel: BaseViewModel() {
    val units = MutableLiveData<List<SchedulingUnit>>()
    val period = MutableLiveData<SchedulingPeriod>()

    fun fetchSchedulingUnits() {
        working.work {
            period.value?.id?.let { planningRepository.getSchedulingUnits(it).parseResponse(units) }
        }
    }
}