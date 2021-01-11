package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

class HistoryViewModel : BaseViewModel() {

    init {
        Logger.d(LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
        fetchShifts(ZonedDateTime.now())
    }

    private val _shifts = MutableLiveData<List<Shift>>()
    val shifts: LiveData<List<Shift>> = _shifts

    fun fetchShifts(to: ZonedDateTime) = working.work {
        shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).run { parseResponse(_shifts) }
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).run { parseResponse(_shifts) }
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            _shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let(::parseError)
        }
    }
}