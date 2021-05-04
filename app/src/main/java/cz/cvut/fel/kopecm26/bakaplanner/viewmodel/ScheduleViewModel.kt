package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isBefore

class ScheduleViewModel : BaseViewModel() {

    private val _shifts = MutableLiveData<List<Shift>>()
    val shifts: LiveData<List<Shift>> = _shifts

    private val _periods = MutableLiveData<List<SchedulingPeriod>>()

    private val _groups = MutableLiveData<Map<SchedulingPeriod?, List<Shift>>>()
    val groups: LiveData<Map<SchedulingPeriod?, List<Shift>>> = _groups

    init {
        refreshShifts()
    }

    fun fetchShiftsRemote() {
        working.work {
            shiftRepository.refreshAllShifts().let(::saveShifts)
        }
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.getAllPeriods().parseResponse(_periods)
            shiftRepository.getUpcomingShifts().let(::saveShifts)
            _groups.value = _shifts.value?.groupBy {
                _periods.value?.firstOrNull { period ->
                    period.start_date.isBefore(it.start_time) && it.start_time.isBefore("${period.end_date}T23:59:59.999Z")
                }
            }
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        response.parseResponse(_shifts)
    }
}
