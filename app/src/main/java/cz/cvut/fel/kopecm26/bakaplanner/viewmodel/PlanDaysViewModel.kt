package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatter
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.mergeWithHours
import java.time.LocalDate
import java.time.LocalTime

class PlanDaysViewModel : BaseViewModel() {
    val shiftHours = MutableLiveData<Int?>()

    private val _startTime = MutableLiveData<LocalTime>()
    val startTime: LiveData<LocalTime> = _startTime

    fun setStartTime(time: LocalTime) {
        _startTime.value = time
    }

    private val _startTimeF = MediatorLiveData<String?>().apply {
        addSource(_startTime) { this.value = it.format(formatter(DateTimeFormats.HOURS_MINUTES)) }
    }
    val startTimeF: LiveData<String?> = _startTimeF


    private val _endTime = MutableLiveData<LocalTime>()
    val endTime: LiveData<LocalTime> = _endTime

    fun setEndTime(time: LocalTime) {
        _endTime.value = time
    }

    private val _endTimeF = MediatorLiveData<String?>().apply {
        addSource(_endTime) { this.value = it.format(formatter(DateTimeFormats.HOURS_MINUTES)) }
    }
    val endTimeF: LiveData<String?> = _endTimeF


    private val _shiftStartTime = MutableLiveData<LocalTime>()
    val shiftStartTime: LiveData<LocalTime> = _shiftStartTime
    private val _shiftStartTimeF = MediatorLiveData<String>().apply {
        addSource(_shiftStartTime) {
            this.value = it.format(formatter(DateTimeFormats.HOURS_MINUTES))
        }
    }
    val shiftStartTimeF: LiveData<String?> = _shiftStartTimeF

    fun setShiftStartTime(time: LocalTime) {
        _shiftStartTime.value = time
    }

    val breakMinutes = MediatorLiveData<Int>().apply {
        this.addSource(shiftHours) {
            if (it != null && this.value == null) this.value = 30
        }
    }

    val shiftsPerDay = MutableLiveData(1)

    val nightShift = MutableLiveData(false)

    @JvmField
    val is24Hours = MutableLiveData(false)

    private val _shiftTimeCalculations = MutableLiveData<List<ShiftTimeCalculation>>()
    val shiftTimeCalculations: LiveData<List<ShiftTimeCalculation>> = _shiftTimeCalculations

    val mappedShiftHours = MediatorLiveData<String>().apply {
        this.addSource(shiftTimeCalculations) { list ->
            this.value = list.joinToString(", ") { "${it.startTime} – ${it.endTime}" }
        }
    }

    fun fetchShiftTimeCalculations(periodId: Int) {
        ifNotNull(
            startTime.value ?: shiftStartTime.value,
            endTime.value ?: shiftStartTime.value,
            shiftHours.value,
            breakMinutes.value,
            shiftsPerDay.value
        ) {
            working.work {
                planningRepository.getShiftTimeCalculations(
                    periodId,
                    startTime.value.toString(),
                    endTime.value.toString(),
                    shiftHours.value!!,
                    breakMinutes.value!!,
                    shiftsPerDay.value!!,
                    nightShift.value == true,
                    nightShift.value == true && is24Hours.value == true,
                    shiftStartTime.value?.toString()
                ).parseResponse(_shiftTimeCalculations)
            }
        }
    }

    fun validateStartBeforeEnd(): Boolean {
        val today = LocalDate.now()
        val nightShift = nightShift.value ?: false
        val start = startTime.value
        val count = shiftsPerDay.value ?: 1
        val end = endTime.value
        val hours = shiftHours.value?.toLong()
        val minutes = breakMinutes.value?.toLong()
        var value = false
        ifNotNull(
            nightShift,
            start,
            end,
            count,
            hours,
            minutes
        ) {
            value = today.mergeWithHours(start!!)
                ?.isBefore(today.mergeWithHours(end!!, nightShift)) == true
        } ?: run {
            // Do not display errors if hours not filled...
            value = hours == null
        }
        return value
    }

    fun validateMax24Hours(): Boolean {
        val today = LocalDate.now()
        val nightShift = nightShift.value ?: false
        val is24Hours = is24Hours.value ?: false
        val start = startTime.value
        val count = shiftsPerDay.value ?: 1
        val end = endTime.value
        val hours = shiftHours.value?.toLong()
        val minutes = breakMinutes.value?.toLong()
        var value = false
        ifNotNull(
            nightShift,
            start,
            end,
            count,
            hours,
            minutes
        ) {
            value = !nightShift || is24Hours || today.mergeWithHours(start!!)?.plusDays(1)
                ?.isAfter(today.mergeWithHours(end!!, nightShift)) == true
        } ?: run {
            // Do not display errors if hours not filled...
            value = is24Hours || hours == null
        }
        return value
    }

    fun validateNotTooLong(fromFirstStartField: Boolean): Boolean {
        val today = LocalDate.now()
        val nightShift = nightShift.value ?: false
        val is24Hours = is24Hours.value ?: false
        val shiftStartTime = shiftStartTime.value
        val start = startTime.value ?: shiftStartTime
        val count = shiftsPerDay.value ?: 1
        val end = endTime.value
        val hours = shiftHours.value?.toLong()
        val minutes = breakMinutes.value?.toLong()
        var value = false
        ifNotNull(
            nightShift,
            start,
            end,
            count,
            hours,
            minutes
        ) {
            val startPlusAllShifts = today.mergeWithHours(start!!)?.plusHours(hours!! * count)
                ?.plusMinutes(minutes!! * count)
            value = (is24Hours &&
                    startPlusAllShifts?.isBefore(today.mergeWithHours(start, true)) != true) ||
                    startPlusAllShifts?.isEqual(today.mergeWithHours(end!!, nightShift)) == true ||
                    startPlusAllShifts?.isBefore(today.mergeWithHours(end!!, nightShift)) != true
        } ?: run {
            // Do not display errors if hours not filled...
            value = fromFirstStartField && !is24Hours || hours == null
        }
        return value
    }

    fun validateNotTooShort(): Boolean {
        val today = LocalDate.now()
        val nightShift = nightShift.value ?: false
        val is24Hours = is24Hours.value ?: false
        val start = startTime.value
        val count = shiftsPerDay.value ?: 1
        val end = endTime.value
        val hours = shiftHours.value?.toLong()
        val minutes = breakMinutes.value?.toLong()
        var value = false
        ifNotNull(
            nightShift,
            start,
            end,
            count,
            hours,
            minutes
        ) {
            val startPlusOneShift =
                today.mergeWithHours(start!!)?.plusHours(hours!!)?.plusMinutes(minutes!!)
            value = startPlusOneShift?.isAfter(today.mergeWithHours(end!!, nightShift)) != true
        } ?: run {
            // Do not display errors if hours not filled...
            value = hours == null
        }
        return value
    }
}
