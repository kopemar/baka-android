package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull

class PlanDaysViewModel : BaseViewModel() {
    val shiftHours = MutableLiveData<Int?>()

    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    private val _startTimeF = MediatorLiveData<String?>().apply {
        addSource(startTime) { this.value = it.hoursAndMinutes() }
    }
    val startTimeF: LiveData<String?> = _startTimeF

    private val _endTimeF = MediatorLiveData<String?>().apply {
        addSource(endTime) { this.value = it.hoursAndMinutes() }
    }
    val endTimeF: LiveData<String?> = _endTimeF

    val breakMinutes = MediatorLiveData<Int>().apply {
        this.addSource(shiftHours) {
            if (it != null && this.value == null) this.value = 30
        }
    }

    val shiftsPerDay = MutableLiveData(1)

    private val _shiftTimeCalculations = MutableLiveData<List<ShiftTimeCalculation>>()
    val shiftTimeCalculations: LiveData<List<ShiftTimeCalculation>> = _shiftTimeCalculations

    val mappedShiftHours = MediatorLiveData<String>().apply {
        this.addSource(shiftTimeCalculations) {
            this.value = it.joinToString(", ") { "${it.startTime} – ${it.endTime}" }
        }
    }

    fun fetchShiftTimeCalculations(periodId: Int) {
        ifNotNull(
            startTime.value,
            endTime.value,
            shiftHours.value,
            breakMinutes.value,
            shiftsPerDay.value
        ) {
            working.work {
                planningRepository.getShiftTimeCalculations(
                    periodId,
                    startTime.value!!,
                    endTime.value!!,
                    shiftHours.value!!,
                    breakMinutes.value!!,
                    shiftsPerDay.value!!
                ).parseResponse(_shiftTimeCalculations)
            }
        }
    }
}
