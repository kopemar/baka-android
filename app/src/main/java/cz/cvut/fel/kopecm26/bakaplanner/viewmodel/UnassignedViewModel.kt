package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isBefore
import java.time.ZonedDateTime

class UnassignedViewModel : BaseViewModel() {

    private val _groups = MutableLiveData<Map<SchedulingPeriod?, List<ShiftTemplate>>>()
    val groups: LiveData<Map<SchedulingPeriod?, List<ShiftTemplate>>> = _groups

    private val _periods = MutableLiveData<List<SchedulingPeriod>>()

    private val _shifts = MutableLiveData<List<ShiftTemplate>>()
    val shifts: LiveData<List<ShiftTemplate>> = _shifts

    init {
        refresh()
    }

    fun refresh() {
        working.work {
            shiftRepository.getUnassigned().parseResponse(_shifts)
            planningRepository.getSchedulingPeriods(ZonedDateTime.now()).parseResponse(_periods)
            _groups.value = _shifts.value?.groupBy {
                _periods.value?.firstOrNull { period ->
                    Logger.d("isBefore: ${period.start_date.isBefore(it.start_time) && it.start_time.isBefore(period.end_date)}")
                    period.start_date.isBefore(it.start_time) && it.start_time.isBefore("${period.end_date}T23:59:59.999Z")
                }
            }
        }
    }
}
