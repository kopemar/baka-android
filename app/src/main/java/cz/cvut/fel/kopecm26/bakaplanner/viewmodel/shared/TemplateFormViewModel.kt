package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Priority
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isTimeBefore
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.mergeWithHours
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class TemplateFormViewModel : BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()

    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()
    val priority = MutableLiveData<Priority>()

    val breakMinutes = MediatorLiveData<Int>().apply {
        this.addSource(startTime) {
            if (this.value == null && endTime.value != null) this.value = 30
        }

        this.addSource(endTime) {
            if (this.value == null && startTime.value != null) this.value = 30
        }
    }

    private val _duration = MediatorLiveData<Float>().apply {
        this.addSource(startTime) {
            this.value = countDuration()
        }

        this.addSource(endTime) {
            this.value = countDuration()
        }

        this.addSource(breakMinutes) {
            this.value = countDuration()
        }
    }
    val duration: LiveData<Float> = _duration

    private val _durationError = MutableLiveData(true)
    val durationError: LiveData<Boolean> = _durationError

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    val start: String
        get() = unit.value?.start_time?.mergeWithHours(startTime.value.toString()).toString()

    val end: String
        get() = unit.value?.start_time?.mergeWithHours(endTime.value.toString(),
            startTime.value?.let { endTime.value?.isTimeBefore(it) } == true
        ).toString()

    fun submitTemplate() {
        if (durationError.value != false) {
            _errorMessage.value = if (duration.value?.let { it > 0 } == true) R.string.duration_less_12_hours
            else R.string.break_too_long
            return
        }
        working.work {
            planningRepository.addShiftTemplate(
                ShiftTemplate(
                    null,
                    start,
                    end,
                    breakMinutes.value ?: 0,
                    priority.value?.integerValue ?: 0,
                    duration.value
                )
            ).let(::parseResponse)
        }
    }

    fun parseResponse(response: ResponseModel<ShiftTemplate>) {
        if (response is ResponseModel.SUCCESS) {
            _success.value = true
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let { parseError(it) }
        }
    }

    private fun countDuration(): Float? = try {
        ChronoUnit.MINUTES.between(
            ZonedDateTime.parse(start),
            ZonedDateTime.parse(end)
        ).toFloat().minus(breakMinutes.value?.toFloat() ?: 0F).run {
            _durationError.value = this.div(Constants.Time.MINUTES_IN_HOUR) !in (0.0..12.0)
            this.div(Constants.Time.MINUTES_IN_HOUR)
        }
    } catch (e: DateTimeParseException) {
        _durationError.value = true
        null
    }

}